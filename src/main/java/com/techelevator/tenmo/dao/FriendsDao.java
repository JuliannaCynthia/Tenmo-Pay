package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Friends;
import com.techelevator.tenmo.model.FriendsDTO;

import java.security.Principal;
import java.util.List;

public interface FriendsDao {

    List<FriendsDTO> getAcceptedFriends(Principal principal);

    Friends addFriend(Principal principal, String foreignUserName);
    List<FriendsDTO> pendingFriendships(Principal principal);

    void approveFriend(Principal principal, String foreignUserName);
    void denyFriend(Principal principal, String foreignUserName);
    void deleteFriend(Principal principal, String foreignUserName);

}
