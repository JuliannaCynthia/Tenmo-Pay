package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.businesslogic.FriendlyBusinessLogic;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.FriendsDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Friends;
import com.techelevator.tenmo.model.FriendsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class FriendsController {
    private JdbcTemplate jdbcTemplate;
    private FriendlyBusinessLogic FBL;

    public FriendsController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        FBL = new FriendlyBusinessLogic(jdbcTemplate);
    }
    @Autowired
    FriendsDao jdbcFriend;
    @RequestMapping(path = "/friends", method = RequestMethod.GET)
    public List<FriendsDTO> getAcceptedFriends(Principal principal){
        return jdbcFriend.getAcceptedFriends(principal.getName());
    }

    @RequestMapping(path = "/friends/new-addition", method = RequestMethod.POST)
    public int addFriend(Principal principal,@Valid @RequestBody Friends friends){
        boolean userCheck = FBL.checkLoggedInUserRequest(principal.getName(), friends);
        boolean sameCheck = FBL.noSameFriendRequests(friends);
        boolean diffCheck = FBL.checkDifferentUsers(friends);
        boolean falseCheck = FBL.setToFalse(friends);
        if(userCheck&&sameCheck&&diffCheck&&falseCheck){
            return jdbcFriend.addFriend(principal.getName(), friends.getUserNameReceived());
        }else{
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "One or more issues with your request. Please try again.");
        }
    }
    @RequestMapping(path = "/friends/pending", method = RequestMethod.GET)
    public List<FriendsDTO> viewPendingFriends(Principal principal){
        return jdbcFriend.pendingFriendships(principal.getName());
    }
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "friends/pending", method = RequestMethod.PUT)
    public int approveFriend(Principal principal, @Valid @RequestBody Friends friends){
       boolean bizCheck = FBL.checkLoggedInUserRecipient(principal.getName(), friends);
        if(!bizCheck){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "You cannot approve this request. Please sign in as the correct user.");
        }
        boolean diffCheck = FBL.checkDifferentUsers(friends);
        boolean falseCheck = FBL.setToFalse(friends);
        if(diffCheck&&falseCheck){
            return jdbcFriend.approveFriend(principal.getName(), friends.getUserNameRequest());
        }else{
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Please submit valid friend to approve.");
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "friends/pending", method = RequestMethod.DELETE)
    public int denyFriend(Principal principal, @Valid @RequestBody Friends friends){
        boolean bizCheck = FBL.checkLoggedInUserRecipient(principal.getName(), friends);
        if(!bizCheck){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "You cannot deny this request. Please sign in as the correct user.");
        }
        boolean diffCheck = FBL.checkDifferentUsers(friends);
        boolean falseCheck = FBL.setToFalse(friends);
        if(diffCheck&&falseCheck){
            return jdbcFriend.denyFriend(principal.getName(), friends.getUserNameRequest());
        }else{
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Please submit valid friend to approve.");
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "friends/remove", method = RequestMethod.DELETE)
    public int removeFriend(Principal principal, @Valid @RequestBody Friends friends){
        boolean userCheck = FBL.checkLoggedInUserRecipient(principal.getName(), friends);
        if(!userCheck){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "You cannot approve this request. Please sign in as the correct user.");
        }else {
            return jdbcFriend.deleteFriend(principal.getName(), friends.getUserNameRequest());
        }
    }


}
