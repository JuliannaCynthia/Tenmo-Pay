package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Friends;
import com.techelevator.tenmo.model.FriendsDTO;

import java.security.Principal;
import java.util.List;

public interface FriendsDao {

    List<FriendsDTO> getAcceptedFriends(String Username);

    int addFriend(String userName, String foreignUserName);
    List<FriendsDTO> pendingFriendships(String username);

    int approveFriend(String username, String foreignUserName);
    int denyFriend(String username, String foreignUserName);
    int deleteFriend(String username, String userNameRequest);

}
