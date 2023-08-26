package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.businesslogic.FriendlyBusinessLogic;

import com.techelevator.tenmo.dao.FriendsDao;

import com.techelevator.tenmo.model.Friends;
import com.techelevator.tenmo.model.FriendsDTO;
import com.techelevator.tenmo.model.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class FriendsController {

    File file = new File("friendshiplogs.txt");
    Logger log = new Logger(file);
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
        log.write(principal.getName() + " viewed available friends.");
        return jdbcFriend.getAcceptedFriends(principal.getName());
    }

    @RequestMapping(path = "/friends/new-addition", method = RequestMethod.POST)
    public int addFriend(Principal principal,@Valid @RequestBody Friends friends){
        boolean userCheck = FBL.checkLoggedInUserRequest(principal.getName(), friends);
        boolean sameCheck = FBL.noSameFriendRequests(friends);
        boolean diffCheck = FBL.checkDifferentUsers(friends);
        boolean falseCheck = FBL.setToFalse(friends);
        if(userCheck&&sameCheck&&diffCheck&&falseCheck){
            log.write(principal.getName() + " added a Friend. Added user -> "+ friends.getUserNameReceived());
            return jdbcFriend.addFriend(principal.getName(), friends.getUserNameReceived());
        }else{
            log.write(principal.getName()+ " experienced an issue. Error message (One or more issues with your request. Please try again.)");
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
            log.write(principal.getName()+ " experienced an issue. Error message (You cannot approve this request. Please sign in as the correct user.)");
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "You cannot approve this request. Please sign in as the correct user.");
        }
        boolean diffCheck = FBL.checkDifferentUsers(friends);
        if(diffCheck){
            log.write(principal.getName() + " approved a Friend. Approved user -> "+ friends.getUserNameRequest());
            return jdbcFriend.approveFriend(principal.getName(), friends.getUserNameRequest());
        }else{
            log.write(principal.getName()+ " experienced an issue. Error message (Please submit valid friend to approve.)");
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Please submit valid friend to approve.");
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "friends/pending", method = RequestMethod.DELETE)
    public int denyFriend(Principal principal, @Valid @RequestBody Friends friends){
        boolean bizCheck = FBL.checkLoggedInUserRecipient(principal.getName(), friends);
        if(!bizCheck){
            log.write(principal.getName()+ " experienced an issue. Error message (You cannot deny this request. Please sign in as the correct user.)");
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "You cannot deny this request. Please sign in as the correct user.");
        }
        boolean diffCheck = FBL.checkDifferentUsers(friends);
        boolean falseCheck = FBL.setToFalse(friends);
        if(diffCheck&&falseCheck){
            log.write(principal.getName() + " denied a Friend. Denied user -> "+ friends.getUserNameRequest());
            return jdbcFriend.denyFriend(principal.getName(), friends.getUserNameRequest());
        }else{
            log.write(principal.getName()+ " experienced an issue. Error message (Please submit valid friend.)");
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Please submit valid friend.");
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "friends/remove", method = RequestMethod.DELETE)
    public int removeFriend(Principal principal, @Valid @RequestBody Friends friends){
        boolean userCheck = FBL.checkLoggedInUserRecipient(principal.getName(), friends);
        if(!userCheck){
            log.write(principal.getName()+ " experienced an issue. Error message (You cannot approve this request. Please sign in as the correct user.)");
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "You cannot approve this request. Please sign in as the correct user.");
        }else {
            log.write(principal.getName() + " deleted a Friend. Deleted user -> "+ friends.getUserNameRequest());
            return jdbcFriend.deleteFriend(principal.getName(), friends.getUserNameRequest());
        }
    }
    public static double encryptAccountId(int id){
        double encrypt = id;
        encrypt = encrypt/24;
        encrypt += 42;
        return encrypt;
    }

}
