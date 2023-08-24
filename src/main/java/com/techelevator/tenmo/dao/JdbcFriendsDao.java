package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.DaoException;
import com.techelevator.tenmo.model.Friends;
import com.techelevator.tenmo.model.FriendsDTO;
import jdk.jfr.StackTrace;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;



@Component
public class JdbcFriendsDao implements FriendsDao{

    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;

    public JdbcFriendsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userDao = new JdbcUserDao(jdbcTemplate);
    }

    @Override
    public List<FriendsDTO> getAcceptedFriends(String userName) {
        List<FriendsDTO> friendsDTOS;
        String sql = "select user_id_request, user_id_receive from user_friends where user_id_request = ? OR user_id_receive = ? AND approved = true;";
        try{
            friendsDTOS = new ArrayList<>();
            int userId = userDao.findIdByUsername(userName);
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,userId);
            while(rowSet.next()){
                friendsDTOS.add(mapToFriendsDto(rowSet));
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return friendsDTOS;
    }

    @Override
    public int addFriend(String userName, String foreignUserName) {
        String sql = "insert into user_friends (user_id_request, user_id_receive, approved) values (?,?,?);";
        int created;
        try{
            int userid = userDao.findIdByUsername(userName);
            int secId = userDao.findIdByUsername(foreignUserName);
            created= jdbcTemplate.update(sql,userid,secId,false);
        }catch(CannotGetJdbcConnectionException e ){
            throw new ResponseStatusException(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        } catch(DataIntegrityViolationException e){

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "wawawawa");
        }
        return created;
    }

    @Override
    public List<FriendsDTO> pendingFriendships(String userName) {
        List<FriendsDTO> friends;
        String newSql ="select username from tenmo_user " +
                "join user_friends on tenmo_user.user_id = user_friends.user_id_request " +
                "where user_id_request in (select user_id_request from user_friends where user_id_receive = ?) group by username;";
        try{
            friends = new ArrayList<>();
            int userid = userDao.findIdByUsername(userName);
            SqlRowSet results = jdbcTemplate.queryForRowSet(newSql,userid);
            while(results.next()){
                friends.add(mapToFriendsDto(results));
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return friends;
    }

    @Override
    public void approveFriend(String userName, String foreignUserName) {
        String bigSql = "update user_friends set user_id_request = ?, user_id_receive = ?, approved = ? where user_id_receive = ? and user_id_request = ?;";
        String neSql = "select user_id_request from user_friends where user_id_request = ? AND user_id_receive =?;";
        try{
            int userId = userDao.findIdByUsername(userName);
            int secId = userDao.findIdByUsername(foreignUserName);
            int userRequestId = jdbcTemplate.queryForObject(neSql, int.class, secId,userId);
            if(userRequestId==userId){
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
            }else{
                int rowsAffected =jdbcTemplate.update(bigSql,secId,userId,true,userId,secId);
                if(rowsAffected!=1){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void denyFriend(String userName, String foreignUserName) {
        String bigSql = "update user_friends set user_id_request = ?, user_id_receive = ?, approved = ?;";
        String neSql = "select user_id_request from user_friends where user_id_request = ? OR user_id_request = ? AND user_id receive =? OR user_id_receive = ?;";
        try{
            int userId = userDao.findIdByUsername(userName);
            int secId = userDao.findIdByUsername(foreignUserName);
            int userRequestId = jdbcTemplate.queryForObject(neSql, int.class, userId,secId,userId,secId);
            if(userRequestId==userId){
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
            }else{
                int rowsAffected =jdbcTemplate.update(bigSql,userId,secId,false);
                if(rowsAffected!=1){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
                deleteFriend(userName,foreignUserName);
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deleteFriend(String userName, String foreignUserName) {
        String sql = "delete from user_friends where user_id_request = ? OR user_id_request = ? AND user_id receive =? OR user_id_receive = ?;";
        try{
            int userid = userDao.findIdByUsername(userName);
            int secId = userDao.findIdByUsername(foreignUserName);
            int rowsAffected= jdbcTemplate.update(sql,userid,secId,userid,secId);
            if(rowsAffected!= 1){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public FriendsDTO mapToFriendsDto(SqlRowSet rowSet){
        FriendsDTO friendsDTO = new FriendsDTO();
        friendsDTO.setUsername(rowSet.getString("username"));
        return friendsDTO;
    }

    public Friends mapRowToFriends(SqlRowSet rowSet){
        Friends friends = new Friends();
        String firstUser;
        String secondUser;
        try{
            String principalSql = "select username from tenmo_user where user_id = ?;";
            secondUser = jdbcTemplate.queryForObject(principalSql, String.class, rowSet.getInt("user_id_receive"));
            firstUser = jdbcTemplate.queryForObject(principalSql, String.class, rowSet.getInt("user_id_request"));
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "wowowow");
        }
        friends.setUserNameReceived(secondUser);
        friends.setUserNameRequest(firstUser);
        friends.setApproved(rowSet.getBoolean("approved"));
        return friends;
    }
}
