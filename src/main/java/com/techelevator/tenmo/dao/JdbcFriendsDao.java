package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.DaoException;
import com.techelevator.tenmo.model.Friends;
import com.techelevator.tenmo.model.FriendsDTO;
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
    public List<FriendsDTO> getAcceptedFriends(Principal principal) {
        List<FriendsDTO> friendsDTOS = null;
        String sqlUser = "select user_id from tenmo_user where username = ?";
        String sql = "select user_id_request, user_id_receive from user_friends where user_id_request = ? OR user_id_receive = ? AND approved = true;";
        try{
            friendsDTOS = new ArrayList<>();
            int userId = jdbcTemplate.queryForObject(sqlUser,int.class,principal.getName(),principal.getName());
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
    public Friends addFriend(Principal principal, String foreignUserName) {
        String userSql = "select user_id from tenmo_user where username = ?;";
        String sql = "insert into user_friends (user_id_request, user_id_receive, approved) values (?,?,?);";
        String sqlMake = "select * from user_friend where user_id_request = ? and user_id_receive = ?";
        Friends friends = null;
        try{
            int userid = jdbcTemplate.queryForObject(userSql, int.class, principal.getName());
            int secId = jdbcTemplate.queryForObject(userSql, int.class, foreignUserName);
            jdbcTemplate.update(sql,principal.getName(),foreignUserName,false);
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlMake, userid,secId);
            if(rowSet.next()){
                friends = mapRowToFriends(rowSet);
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return friends;
    }

    @Override
    public List<FriendsDTO> pendingFriendships(Principal principal) {
        List<FriendsDTO> friends = null;
        String sql = "select user_id from tenmo_user where username = ?;";
        String newSql ="select tenmo_user.username from tenmo_user join user_friends on tenmo_user.user_id = user_friends.user_id where user_id_receive = ?;";
        try{
            friends = new ArrayList<>();
            int userid = jdbcTemplate.queryForObject(sql, int.class,principal.getName());
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
    public void approveFriend(Principal principal, String foreignUserName) {
        String sql = "select user_id from tenmo_user where username = ?;";
        String bigSql = "update user_friends set user_id_request = ?, user_id_receive = ?, approved = ?;";
        String neSql = "select user_id_request from user_friends where user_id_request = ? OR user_id_request = ? AND user_id receive =? OR user_id_receive = ?;";
        try{
            int userRequestId = jdbcTemplate.queryForObject(neSql, int.class, principal.getName(),foreignUserName,principal.getName(),foreignUserName);
            int userId = jdbcTemplate.queryForObject(sql, int.class, principal.getName());
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
    public void denyFriend(Principal principal, String foreignUserName) {
        String sql = "select user_id from tenmo_user where username = ?;";
        String bigSql = "update user_friends set user_id_request = ?, user_id_receive = ?, approved = ?;";
        String neSql = "select user_id_request from user_friends where user_id_request = ? OR user_id_request = ? AND user_id receive =? OR user_id_receive = ?;";
        try{
            int userRequestId = jdbcTemplate.queryForObject(neSql, int.class, principal.getName(),foreignUserName,principal.getName(),foreignUserName);
            int userId = jdbcTemplate.queryForObject(sql, int.class, principal.getName());
            int secId = jdbcTemplate.queryForObject(sql, int.class,foreignUserName);
            if(userRequestId==userId){
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
            }else{
                int rowsAffected =jdbcTemplate.update(bigSql,userId,secId,false);
                if(rowsAffected!=1){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
                deleteFriend(principal,foreignUserName);
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public void deleteFriend(Principal principal, String foreignUserName) {
        String principalSql = "select user_id from tenmo_user where username = ?;";
        String sql = "delete from user_friends where user_id_request = ? OR user_id_request = ? AND user_id receive =? OR user_id_receive = ?;";
        try{
            int userid = jdbcTemplate.queryForObject(principalSql, int.class,principal.getName());
            int secId = jdbcTemplate.queryForObject(principalSql, int.class,foreignUserName);
            int rowsAffected= jdbcTemplate.update(sql,principal.getName(),foreignUserName,principal.getName(),foreignUserName);
            if(rowsAffected!= 1){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    public FriendsDTO mapToFriendsDto(SqlRowSet rowSet){
        FriendsDTO friendsDTO = new FriendsDTO();
        String secondUser;
        try{
            String principalSql = "select username from tenmo_user where user_id = ?;";
            secondUser = jdbcTemplate.queryForObject(principalSql, String.class, rowSet.getInt("user_id_receive"));
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        friendsDTO.setUserNameReceive(secondUser);
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        friends.setUserNameReceived(secondUser);
        friends.setUserNameRequest(firstUser);
        friends.setApproved(rowSet.getBoolean("approved"));
        return friends;
    }
}
