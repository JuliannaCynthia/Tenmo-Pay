package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Friends;
import com.techelevator.tenmo.model.FriendsDTO;

import java.security.Principal;
import java.util.List;

public interface FriendsDao {

    List<FriendsDTO> getAcceptedFriends(String Username);

    Friends addFriend(String userName, String foreignUserName);
    List<FriendsDTO> pendingFriendships(String username);

    void approveFriend(String username, String foreignUserName);
    void denyFriend(String username, String foreignUserName);
    void deleteFriend(String username, String foreignUserName);

}
