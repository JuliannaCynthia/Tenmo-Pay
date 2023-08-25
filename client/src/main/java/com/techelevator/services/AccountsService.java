package com.techelevator.services;

import com.techelevator.model.Account;
import com.techelevator.model.AccountDTO;
import com.techelevator.model.UserToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AccountsService {

    public static String TENMO_BASE_URL = "http://localhost:8080";
    private RestTemplate restTemplate = new RestTemplate();
    private UserToken userToken;

    public AccountsService(UserToken userToken){
        this.userToken = userToken;
    }

    public List<AccountDTO> getUserAccounts() {
        AccountDTO[] userAccounts = null;
        try { ResponseEntity<AccountDTO[]> response =
                restTemplate.exchange(TENMO_BASE_URL + "/account/all", HttpMethod.GET, makeAuthEntity(), AccountDTO[].class );
            userAccounts = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            //TODO: add a logger here. log(e.getMessage)
        }
        return Arrays.stream(userAccounts).collect(Collectors.toList());
    }

    public Account createAccount(){
        Account newAccount = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(TENMO_BASE_URL + "/account/create", HttpMethod.POST, makeAuthEntity(), Account.class);
            newAccount = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            //TODO: add a logger here. log(e.getMessage)
        }
        return newAccount;
    }

//    public Account getAccount(int accountNumber){
//        Account account = null;
//        HttpEntity<String> accountNumEntity = makeAccountEntity(accountNumber);
//
//        try {
//            account = restTemplate.postForObject(TENMO_BASE_URL + "/account", accountNumEntity, Account.class );
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            //TODO: add a logger here. log(e.getMessage)
//        }
//        return account;
//    }

    public Account deleteAccount(){
        Account newAccount = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(TENMO_BASE_URL + "/account/create", HttpMethod.POST, makeAuthEntity(), Account.class);
            newAccount = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            //TODO: add a logger here. log(e.getMessage)
        }
        return newAccount;
    }


    private HttpEntity<String> makeAccountEntity(int accountId){
        String body = "{\"accountId\" : \"" + accountId + "\" }";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken.getToken());
        HttpEntity<String> accountNumEntity = new HttpEntity<>(body, headers);
        return accountNumEntity;

    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken.getToken());
        return new HttpEntity<>(headers);
    }
}
