package com.techelevator.services;

import com.techelevator.model.*;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class LoginRegisterService {
    private static File file = new File("Logs","log.txt");
    private static Logger log = new Logger(file);
    public static String TENMO_BASE_URL = "http://localhost:8080";
    private RestTemplate restTemplate = new RestTemplate();


    public UserToken login(LoginDTO user) {
        HttpEntity<LoginDTO> userHttpEntity = makeUserEntity(user);
        UserToken userToken = null;
        try {
            userToken = restTemplate.postForObject(TENMO_BASE_URL + "/login", userHttpEntity, UserToken.class);

        } catch (RestClientResponseException e) {
            String errorMessage = "Returned status: " + e.getRawStatusCode() + "\n" +
                    "Status message: " + e.getMessage();
            System.out.println(errorMessage);
            log.write(e.getMessage());
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
            log.write(e.getMessage());
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
            log.write(e.getMessage());
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
            log.write(e.getMessage());

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
