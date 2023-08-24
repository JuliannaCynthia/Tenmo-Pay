package com.techelevator.tenmo.businesslogic;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Friends;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.server.ResponseStatusException;

public class FriendlyBusinessLogic {

    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;

    public FriendlyBusinessLogic(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userDao = new JdbcUserDao(jdbcTemplate);
    }

    public boolean checkLoggedInUser(String username, Friends friends){
        if(!username.equals(friends.getUserNameReceived())){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkDifferentUsers(Friends friends){
        return !friends.getUserNameReceived().equals(friends.getUserNameRequest());
    }

    public boolean noSameUsers(Friends friends){
        String sql = "select * from user_friends where (user_id_request=? and user_id_receive=?) OR (user_id_request = ? and user_id_receive = ?);";
        try{
            int receive=userDao.findIdByUsername(friends.getUserNameReceived());
            int request=userDao.findIdByUsername(friends.getUserNameRequest());
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,request,receive,receive,request);
            if(rowSet.next()){
                return false;
            }else{
                return true;
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You are already Friends.");
        }
    }

    public boolean setToFalse(Friends friends){
        return !friends.isApproved();
    }



}
