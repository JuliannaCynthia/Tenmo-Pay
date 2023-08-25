package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Friends;
import com.techelevator.tenmo.model.FriendsDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.Assert.*;

public class JdbcFriendsDaoTest extends  BaseDaoTests {
    private FriendsDao sut;
    private UserDao userDao;
    private final Friends FRIEND_ONE = new Friends("Test1", "Test2", true);
    private final Friends FRIEND_TWO = new Friends("bub", "Test2", true);
    private final Friends FRIEND_THREE = new Friends("Test1", "bub", false);
    private final Friends FRIEND_FOUR = new Friends("Test2", "bub", false);

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcFriendsDao(jdbcTemplate);
        userDao = new JdbcUserDao(jdbcTemplate);
        userDao.create("Test1", "password");
        userDao.create("Test2", "password");
        userDao.create("bub", "password");

        sut.addFriend("Test1", "Test2");
        sut.addFriend("bub", "Test2");
        sut.approveFriend("Test2", "Test1");
        sut.approveFriend("Test2", "bub");

    }

    @Test
    public void getAcceptedFriends() {
        List<FriendsDTO> test = sut.getAcceptedFriends("Test2");
        assertEquals(2, test.size());
    }

    @Test
    public void addFriend() {
        int test = sut.addFriend("bub", "user");
        assertEquals(1, test);
    }

    @Test
    public void pendingFriendships() {
        List<FriendsDTO> test = sut.pendingFriendships("Test2");
        assertEquals(2, test.size());
    }

    @Test
    public void approveFriend() {
        sut.addFriend("bub", "Test1");
        int test = sut.approveFriend("Test1", "bub");
        assertEquals(1, test);
    }

    @Test
    public void denyFriend() {
        sut.addFriend("bub", "Test1");
        int test = sut.denyFriend("Test1", "bub");
        assertEquals(1, test);
    }

    @Test
    public void deleteFriend() {
        sut.addFriend("bub", "Test1");
        int test = sut.deleteFriend("Test1", "bub");
        assertEquals(1, test);
    }
}
