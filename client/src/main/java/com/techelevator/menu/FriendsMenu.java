package com.techelevator.menu;

import com.techelevator.model.FriendsDTO;
import com.techelevator.model.UserToken;
import com.techelevator.services.FriendService;
import com.techelevator.services.GraphicServices;
import com.techelevator.services.InputService;

import java.util.List;

public class FriendsMenu {

    private final FriendService friendService;
    private final GraphicServices graphicServices;
    private final InputService inputService;
    private List<FriendsDTO> friendsList;


    public FriendsMenu(String username, UserToken userToken, GraphicServices graphicServices, InputService inputService){
        this.graphicServices = graphicServices;
        this.inputService = inputService;
        this.friendService = new FriendService(userToken,username);
        friendsList = friendService.getFriends();
    }

    public void run(){
        int menuSelection = -1;
        while (menuSelection != 0){
            graphicServices.displayFriendsMenu();

            menuSelection = inputService.promptForMenuSelection();

            //display all friends
            if(menuSelection == 1 ){
                friendsList = friendService.getFriends();
                graphicServices.displayFriends(friendsList);
            }

            //add friend
            if(menuSelection == 2){
                String friendUsername = inputService.promptForUsername();
                boolean requestSent = friendService.sendFriendRequest(friendUsername);
                graphicServices.friendRequest(requestSent);
            }

            //view pending friend request
            if(menuSelection == 3 ){
                List<FriendsDTO> pendingFriends = friendService.getPendingFriendRequests();
                graphicServices.displayFriends(pendingFriends);
            }

            //approve friend request
            if(menuSelection == 4) {
                System.out.println("Please enter a friend request to approve:");
                String friendUsername = inputService.promptForUsername();
                boolean requestApproved = friendService.approveFriendRequest(friendUsername);
                graphicServices.friendApproval(requestApproved);
            }

            //deny friend request
            if(menuSelection == 5){
                System.out.println("Please enter a friend request to deny:");
                String friendRequestUsername = inputService.promptForUsername();
                boolean successful = friendService.denyFriendRequest(friendRequestUsername);
                graphicServices.friendDenial(successful);
            }

            //remove friend
            if(menuSelection == 6){
                System.out.println("Please enter a friend to remove:");
                String friendUsername = inputService.promptForUsername();
                boolean successful = friendService.denyFriendRequest(friendUsername);
               graphicServices.friendRemoval(successful);
            }
        }
    }
}
