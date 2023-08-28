package com.techelevator.services;

import com.techelevator.model.Friends;
import com.techelevator.model.FriendsDTO;
import com.techelevator.model.Logger;
import com.techelevator.model.UserToken;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FriendService {
    private static File file = new File("Logs","log.txt");
    private static Logger log = new Logger(file);
    private final static String FRIEND_BASE_URL = "http://localhost:8080/friends";
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserToken userToken;
    private final String username;

    public FriendService(UserToken userToken, String username) {
        this.userToken = userToken;
        this.username = username;
    }

    public List<FriendsDTO> getFriends() {
        FriendsDTO[] friendsDTOS = null;

        try {
            ResponseEntity<FriendsDTO[]> response = restTemplate.exchange(FRIEND_BASE_URL, HttpMethod.GET, makeAuthEntity(), FriendsDTO[].class);
            friendsDTOS = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
          log.write(e.getMessage());
        }
        return Arrays.stream(friendsDTOS).collect(Collectors.toList());
    }

    public boolean sendFriendRequest(String friendUsername) {
        boolean requestSuccessful = false;
        Friends friendRequest = new Friends();
        friendRequest.setUserNameRequest(username);
        friendRequest.setUserNameReceived(friendUsername);

        HttpEntity<Friends> friendsHttpEntity = makeFriendsEntity(friendRequest);
        try {
            Integer rowsReturned = restTemplate.postForObject(FRIEND_BASE_URL + "new-addition", friendsHttpEntity, int.class);
            if (rowsReturned != null && rowsReturned != 0) {
                requestSuccessful = true;
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            log.write(e.getMessage());
        }
        return requestSuccessful;
    }

    public List<FriendsDTO> getPendingFriendRequests() {
        FriendsDTO[] friendsDTOS = null;

        try {
            ResponseEntity<FriendsDTO[]> response = restTemplate.exchange(FRIEND_BASE_URL + "/pending", HttpMethod.GET, makeAuthEntity(), FriendsDTO[].class);
            friendsDTOS = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            log.write(e.getMessage());
        }
        return Arrays.stream(friendsDTOS).collect(Collectors.toList());
    }

    public boolean approveFriendRequest(String friendUsername){
        boolean requestSuccessful = false;
        Friends friendRequest = new Friends();
        friendRequest.setUserNameRequest(friendUsername);
        friendRequest.setUserNameReceived(username);
        friendRequest.setApproved(true);

        HttpEntity<Friends> friendsHttpEntity = makeFriendsEntity(friendRequest);
        try {
            restTemplate.put(FRIEND_BASE_URL + "/pending", friendsHttpEntity);
            requestSuccessful = true;

        } catch (RestClientResponseException | ResourceAccessException e) {
            log.write(e.getMessage());
        }
        return requestSuccessful;
    }

    public boolean denyFriendRequest(String friendUsername){
        boolean requestSuccessful = false;
        Friends friendRequest = new Friends();
        friendRequest.setUserNameRequest(friendUsername);
        friendRequest.setUserNameReceived(username);
        friendRequest.setApproved(false);

        HttpEntity<Friends> friendsHttpEntity = makeFriendsEntity(friendRequest);
        try {
            restTemplate.delete(FRIEND_BASE_URL + "/pending", friendsHttpEntity);
            requestSuccessful = true;

        } catch (RestClientResponseException | ResourceAccessException e) {
            log.write(e.getMessage());
        }
        return requestSuccessful;
    }

    public boolean unfriendUser(String friendUsername){
        boolean requestSuccessful = false;
        Friends friendRequest = new Friends();
        friendRequest.setUserNameRequest(friendUsername);
        friendRequest.setUserNameReceived(username);
        friendRequest.setApproved(false);

        HttpEntity<Friends> friendsHttpEntity = makeFriendsEntity(friendRequest);
        try {
            restTemplate.delete(FRIEND_BASE_URL + "/remove", friendsHttpEntity);
            requestSuccessful = true;

        } catch (RestClientResponseException | ResourceAccessException e) {
            log.write(e.getMessage());
        }
        return requestSuccessful;
    }

    private HttpEntity<Friends> makeFriendsEntity(Friends friendRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken.getToken());
        return new HttpEntity<>(friendRequest, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken.getToken());
        return new HttpEntity<>(headers);
    }
}
