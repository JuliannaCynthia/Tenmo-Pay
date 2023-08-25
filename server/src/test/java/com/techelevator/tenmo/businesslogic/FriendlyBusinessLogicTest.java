package com.techelevator.tenmo.businesslogic;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Friends;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.Principal;

import static org.junit.Assert.*;

public class FriendlyBusinessLogicTest extends BaseDaoTests {
    private FriendsDao sut;
    private UserDao userDao;
    Principal principal;
    FriendlyBusinessLogic FBL;
    private final Friends FRIEND_CONNECTION_ONE = new Friends("Test1","Test2",true);
    private final Friends FRIEND_CONNECTION_TWO = new Friends("user","Test1",false);
    private final Friends FRIEND_CONNECTION_THREE = new Friends("Test1","Test1",true);

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcFriendsDao(jdbcTemplate);
        userDao=new JdbcUserDao(jdbcTemplate);
        FBL = new FriendlyBusinessLogic(jdbcTemplate);
        principal = () -> "Test1";

        userDao.create("Test1", "password");
        userDao.create("Test2", "password");
        userDao.create("bub", "password");

        sut.addFriend("Test1", "Test2");
        sut.addFriend("bub", "Test2");
        sut.approveFriend("Test2", "Test1");
        sut.approveFriend("Test2", "bub");
    }

    @Test
    public void checkLoggedInUserRecipient() {
        boolean actual = FBL.checkLoggedInUserRecipient("Test1",FRIEND_CONNECTION_TWO);
        assertTrue(actual);
        actual = FBL.checkLoggedInUserRecipient("user",FRIEND_CONNECTION_TWO);
        assertFalse(actual);
        actual = FBL.checkLoggedInUserRecipient("Test2",FRIEND_CONNECTION_ONE);
        assertTrue(actual);
        actual = FBL.checkLoggedInUserRecipient("Test2",FRIEND_CONNECTION_TWO);
        assertFalse(actual);
    }

    @Test
    public void checkLoggedInUserRequest() {
        boolean actual = FBL.checkLoggedInUserRequest("user",FRIEND_CONNECTION_TWO);
        assertTrue(actual);
        actual = FBL.checkLoggedInUserRequest("user",FRIEND_CONNECTION_TWO);
        assertTrue(actual);
        actual = FBL.checkLoggedInUserRequest("Test2",FRIEND_CONNECTION_ONE);
        assertFalse(actual);
        actual = FBL.checkLoggedInUserRequest("Test2",FRIEND_CONNECTION_TWO);
        assertFalse(actual);
    }

    @Test
    public void checkDifferentUsers() {
        boolean actual = FBL.checkDifferentUsers(FRIEND_CONNECTION_TWO);
        assertTrue(actual);
        actual = FBL.checkDifferentUsers(FRIEND_CONNECTION_THREE);
        assertFalse(actual);

    }

    @Test
    public void noSameFriendRequests() {
        boolean actual = FBL.noSameFriendRequests(FRIEND_CONNECTION_ONE);
        assertFalse(actual);
        actual = FBL.noSameFriendRequests(FRIEND_CONNECTION_TWO);
        assertTrue(actual);

    }

    @Test
    public void setToFalse() {
        boolean actual = FBL.setToFalse(FRIEND_CONNECTION_ONE);
        assertFalse(actual);
        actual = FBL.setToFalse(FRIEND_CONNECTION_TWO);
        assertTrue(actual);
        actual = FBL.setToFalse(FRIEND_CONNECTION_THREE);
        assertFalse(actual);
    }
}