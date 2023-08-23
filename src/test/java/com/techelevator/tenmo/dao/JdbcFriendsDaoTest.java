package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Friends;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.*;

public class JdbcFriendsDaoTest extends  BaseDaoTests {
    private FriendsDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcFriendsDao(jdbcTemplate);

    }
    @Test
    public void getAcceptedFriends() {
    }

    @Test
    public void addFriend() {
         int test = sut.addFriend("bob", "user");
        assertEquals(1,test);
    }

    @Test
    public void pendingFriendships() {
    }

    @Test
    public void approveFriend() {
    }

    @Test
    public void denyFriend() {
    }

    @Test
    public void deleteFriend() {
    }

    @Test
    public void mapToFriendsDto() {
    }

    @Test
    public void mapRowToFriends() {
    }
}