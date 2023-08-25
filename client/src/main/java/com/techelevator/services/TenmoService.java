package com.techelevator.services;

import com.techelevator.model.LoginDTO;
import com.techelevator.model.RegisterUserDTO;
import com.techelevator.model.UserToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class TenmoService {

    public static String TENMO_BASE_URL = "http://localhost:8080";
    private RestTemplate restTemplate = new RestTemplate();
    private UserToken userToken;

    public UserToken login(LoginDTO user) {
        HttpEntity<LoginDTO> userHttpEntity = makeUserEntity(user);

        try {
            userToken = restTemplate.postForObject(TENMO_BASE_URL + "/login", userHttpEntity, UserToken.class);
        } catch (RestClientResponseException e) {
            String errorMessage = "Returned status: " + e.getRawStatusCode() + "\n" +
                    "Status message: " + e.getStatusText();
            System.out.println(errorMessage);
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return userToken;
    }

    public boolean register(RegisterUserDTO registerUserDTO) {
        HttpEntity<RegisterUserDTO> userHttpEntity = makeUserEntity(registerUserDTO);
        boolean isSuccessful = false;
        try {
            restTemplate.postForObject(TENMO_BASE_URL + "/register", userHttpEntity, HttpStatus.class);
            isSuccessful = true;
        } catch (RestClientResponseException e) {
            String errorMessage = "Returned status: " + e.getRawStatusCode() + "\n" +
                    "Status message: " + e.getStatusText();
            System.out.println(errorMessage);
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return isSuccessful;
    }

    private HttpEntity<LoginDTO> makeUserEntity(LoginDTO user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(user, headers);
    }

    private HttpEntity<RegisterUserDTO> makeUserEntity(RegisterUserDTO user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(user, headers);
    }
}
