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


    private JdbcTemplate jdbcTemplate;

    public JdbcFriendsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FriendsDTO> getAcceptedFriends(String userName) {
        List<FriendsDTO> friendsDTOS = null;
        String sqlUser = "select user_id from tenmo_user where username = ?";
        String sql = "select user_id_request, user_id_receive from user_friends where user_id_request = ? OR user_id_receive = ? AND approved = true;";
        try{
            friendsDTOS = new ArrayList<>();
            int userId = jdbcTemplate.queryForObject(sqlUser,int.class,userName,userName);
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
        String userSql = "select user_id from tenmo_user where username = ?;";
        String sql = "insert into user_friends (user_id_request, user_id_receive, approved) values (?,?,?);";
        String sqlMake = "select * from user_friends where user_id_request = ? and user_id_receive = ?";
        Friends friends = null;
        int tes = 0;
        try{
            int userid = jdbcTemplate.queryForObject(userSql, int.class, userName);
            int secId = jdbcTemplate.queryForObject(userSql, int.class, foreignUserName);
           tes= jdbcTemplate.update(sql,userid,secId,false);
//            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlMake, userid,secId);
//            if(rowSet.next()){
//                friends = mapRowToFriends(rowSet);
//            }
        }catch(CannotGetJdbcConnectionException e ){
            throw new ResponseStatusException(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        } catch(DataIntegrityViolationException e){

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "wawawawa");
        }
        return tes;
    }

    @Override
    public List<FriendsDTO> pendingFriendships(String userName) {
        List<FriendsDTO> friends = null;
        String sql = "select user_id from tenmo_user where username = ?;";
        String newSql ="select username from tenmo_user join user_friends on tenmo_user.user_id = user_friends.user_id_receive where user_id_receive = ?;";
        try{
            friends = new ArrayList<>();
            int userid = jdbcTemplate.queryForObject(sql, int.class,userName);
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
        String sql = "select user_id from tenmo_user where username = ?;";
        String bigSql = "update user_friends set user_id_request = ?, user_id_receive = ?, approved = ?;";
        String neSql = "select user_id_request from user_friends where user_id_request = ? OR user_id_request = ? AND user_id receive =? OR user_id_receive = ?;";
        try{
            int userRequestId = jdbcTemplate.queryForObject(neSql, int.class, userName,foreignUserName,userName,foreignUserName);
            int userId = jdbcTemplate.queryForObject(sql, int.class, userName);
            int secId = jdbcTemplate.queryForObject(sql, int.class,foreignUserName);
            if(userRequestId==userId){
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
            }else{
                int rowsAffected =jdbcTemplate.update(bigSql,userId,secId,true);
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
        String sql = "select user_id from tenmo_user where username = ?;";
        String bigSql = "update user_friends set user_id_request = ?, user_id_receive = ?, approved = ?;";
        String neSql = "select user_id_request from user_friends where user_id_request = ? OR user_id_request = ? AND user_id receive =? OR user_id_receive = ?;";
        try{

            int userId = jdbcTemplate.queryForObject(sql, int.class, userName);
            int secId = jdbcTemplate.queryForObject(sql, int.class,foreignUserName);
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
        String principalSql = "select user_id from tenmo_user where username = ?;";
        String sql = "delete from user_friends where user_id_request = ? OR user_id_request = ? AND user_id receive =? OR user_id_receive = ?;";
        try{
            int userid = jdbcTemplate.queryForObject(principalSql, int.class,userName);
            int secId = jdbcTemplate.queryForObject(principalSql, int.class,foreignUserName);
            int rowsAffected= jdbcTemplate.update(sql,userName,foreignUserName,userName,foreignUserName);
            if(rowsAffected!= 1){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    public FriendsDTO mapToFriendsDto(SqlRowSet rowSet){
        FriendsDTO friendsDTO = new FriendsDTO();
//        String secondUser;
//        try{
//            String principalSql = "select username from tenmo_user where user_id = ?;";
//            secondUser = jdbcTemplate.queryForObject(principalSql, String.class, rowSet.getInt("user_id_receive"));
//        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
        friendsDTO.setUserNameReceive(rowSet.getString("username"));
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
