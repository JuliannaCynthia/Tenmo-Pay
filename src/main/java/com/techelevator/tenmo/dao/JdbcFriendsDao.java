package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.DaoException;
import com.techelevator.tenmo.model.Friends;
import com.techelevator.tenmo.model.FriendsDTO;
import com.techelevator.tenmo.model.Logger;
import jdk.jfr.StackTrace;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;



@Component
public class JdbcFriendsDao implements FriendsDao{
    File file = new File("friendshiplogs.txt");
    Logger log = new Logger(file);
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;

    public JdbcFriendsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userDao = new JdbcUserDao(jdbcTemplate);
    }

    @Override
    public List<FriendsDTO> getAcceptedFriends(String userName) {
        List<FriendsDTO> friendsDTOS;
        String usernameGet = "select username from tenmo_user where user_id = ?;";
        String checkSql = "select user_id_request,user_id_receive from user_friends where user_id_request = ? or user_id_receive = ? and approved = true;";
        try{
            friendsDTOS = new ArrayList<>();
            int userId = userDao.findIdByUsername(userName);
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(checkSql,userId,userId);
            while(rowSet.next()){
                int request = rowSet.getInt("user_id_request");
                int receive = rowSet.getInt("user_id_receive");
                if(userId==request){
                    SqlRowSet name = jdbcTemplate.queryForRowSet(usernameGet, receive);
                    if(name.next()) {
                        friendsDTOS.add(mapToFriendsDto(name));
                    }
                }else if(userId==receive){
                    SqlRowSet name = jdbcTemplate.queryForRowSet(usernameGet, request);
                    if(name.next()) {
                        friendsDTOS.add(mapToFriendsDto(name));
                    }
                }else{
                    log.write(userName + " encountered an Error. Message -> (Error. Not valid user.)");
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Error. Not valid user.");
                }
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            log.write(userName + " encountered an Error. Data Error or Connection Error.");
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
            log.write(userName + " encountered an Error. Data Error or Connection Error.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch(DataIntegrityViolationException e){
            log.write(userName + " encountered an Error. Data Error or Connection Error.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "wawawawa");
        }
        return created;
    }

    @Override
    public List<FriendsDTO> pendingFriendships(String userName) {
        List<FriendsDTO> friends;
        String sql = "select username from tenmo_user where user_id =?;";
        String sqlFr = "select user_id_receive, user_id_request from user_friends where user_id_receive = ? OR user_id_request =? AND approved = false;";
        try{
            friends = new ArrayList<>();
            int userid = userDao.findIdByUsername(userName);
            SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFr,userid,userid);
            while(results.next()){
                int receive = results.getInt("user_id_receive");
                int request = results.getInt("user_id_request");
                if(userid == receive){
                    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,request);
                    if(rowSet.next()) {
                        friends.add(mapToFriendsDto(rowSet));
                    }
                }else{
                    SqlRowSet rowSet =jdbcTemplate.queryForRowSet(sql,receive);
                    if(rowSet.next()) {
                        friends.add(mapToFriendsDto(rowSet));
                    }
                }
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            log.write(userName + " encountered an Error. Data Error or Connection Error.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return friends;
    }

    @Override
    public int approveFriend(String userName, String foreignUserName) {
        String bigSql = "update user_friends set user_id_request = ?, user_id_receive = ?, approved = ? where user_id_receive = ? and user_id_request = ?;";
        String neSql = "select user_id_request from user_friends where user_id_request = ? AND user_id_receive =?;";
        try{
            int userId = userDao.findIdByUsername(userName);
            int secId = userDao.findIdByUsername(foreignUserName);
            int userRequestId = jdbcTemplate.queryForObject(neSql, int.class, secId,userId);
            if(userRequestId==userId){
                log.write(userName + " encountered an Error. Message -> (Friend requests must be approved by recipient.)");
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Friend requests must be approved by recipient.");
            }else{
                int rowsAffected =jdbcTemplate.update(bigSql,secId,userId,true,userId,secId);
                System.out.println(rowsAffected);
                if(rowsAffected!=1){
                    log.write(userName + " encountered an Error. Message -> (Not Found, No update performed)");
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
                return rowsAffected;
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            log.write(userName + " encountered an Error. Data Error or Connection Error.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public int denyFriend(String userName, String foreignUserName) {
        String bigSql = "update user_friends set user_id_request = ?, user_id_receive = ?, approved = ? where user_id_receive = ? and user_id_request = ?;";
        String neSql = "select user_id_request from user_friends where user_id_request = ? AND user_id_receive = ?;";
        try{
            int userId = userDao.findIdByUsername(userName);
            int secId = userDao.findIdByUsername(foreignUserName);
            int userRequestId = jdbcTemplate.queryForObject(neSql, int.class, secId,userId);
            if(userRequestId==userId){
                log.write(userName + " encountered an Error. Message -> (Friend requests must be denied by recipient.)");
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
            }else{
                int rowsAffected =jdbcTemplate.update(bigSql,secId,userId,false, userId,secId);
                if(rowsAffected!=1){
                    log.write(userName + " encountered an Error. Message -> (Not Found, No update performed)");
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
                return deleteFriend(userName,foreignUserName);
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            log.write(userName + " encountered an Error. Data Error or Connection Error.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public int deleteFriend(String userName, String foreignUserName) {
        String sql = "delete from user_friends where user_id_request = ? and user_id_receive = ?;";
        try{
          int userId = userDao.findIdByUsername(userName);
          int receiver = userDao.findIdByUsername(foreignUserName);
          int rowsAffected = jdbcTemplate.update(sql,receiver,userId);
            System.out.println(rowsAffected);
          if(rowsAffected != 1){
              log.write(userName + " encountered an Error. Message -> (Not Found, No update performed)");
              throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED);
          }
            return rowsAffected;
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            log.write(userName + " encountered an Error. Data Error or Connection Error.");
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
