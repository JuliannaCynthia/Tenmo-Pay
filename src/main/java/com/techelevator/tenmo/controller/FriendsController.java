package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.FriendsDao;
import com.techelevator.tenmo.model.Friends;
import com.techelevator.tenmo.model.FriendsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class FriendsController {

    @Autowired
    FriendsDao jdbcFriend;
    @RequestMapping(path = "/friends", method = RequestMethod.GET)
    public List<FriendsDTO> getAcceptedFriends(Principal principal){
        return jdbcFriend.getAcceptedFriends(principal.getName());
    }

    @RequestMapping(path = "/friends/new-addition", method = RequestMethod.POST)
    public int addFriend(Principal principal,@Valid @RequestBody Friends friends){
        return jdbcFriend.addFriend(principal.getName(), friends.getUserNameReceived());
    }
    @RequestMapping(path = "/friends/pending", method = RequestMethod.GET)
    public List<FriendsDTO> viewPendingFriends(Principal principal){
        return jdbcFriend.pendingFriendships(principal.getName());
    }
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "friends/pending", method = RequestMethod.PUT)
    public void approveFriend(Principal principal, @Valid @RequestBody Friends friends){
        jdbcFriend.approveFriend(principal.getName(), friends.getUserNameReceived());
    }


}
