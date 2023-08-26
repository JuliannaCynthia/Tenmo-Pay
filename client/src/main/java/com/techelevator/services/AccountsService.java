package com.techelevator.services;

import com.techelevator.model.Account;
import com.techelevator.model.AccountDTO;
import com.techelevator.model.UserToken;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AccountsService {

    public static String ACCOUNT_BASE_URL = "http://localhost:8080/account";
    private RestTemplate restTemplate = new RestTemplate();
    private UserToken userToken;

    public AccountsService(UserToken userToken){
        this.userToken = userToken;
    }

    public List<AccountDTO> getUserAccounts() {
        AccountDTO[] userAccounts = null;
        try { ResponseEntity<AccountDTO[]> response =
                restTemplate.exchange(ACCOUNT_BASE_URL + "/all", HttpMethod.GET, makeAuthEntity(), AccountDTO[].class );
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
                    restTemplate.exchange(ACCOUNT_BASE_URL + "/create", HttpMethod.POST, makeAuthEntity(), Account.class);
            newAccount = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            //TODO: add a logger here. log(e.getMessage)
        }
        return newAccount;
    }



    public Account getAccount(Account account){

        HttpEntity<Account> accountHttpEntity = makeAccountEntity(account);
        try {
            account = restTemplate.postForObject(ACCOUNT_BASE_URL , accountHttpEntity, Account.class );
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
            //TODO: add a logger here. log(e.getMessage)
        }
        return account;
    }

    public boolean deleteAccount(Account account){
        HttpEntity<Account> accountHttpEntity = makeAccountEntity(account);

        boolean successful = false;
        try {
            restTemplate.exchange(ACCOUNT_BASE_URL + "/delete", HttpMethod.DELETE, accountHttpEntity, Void.class);
            successful = true;
        }catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
            //TODO: add a logger here. log(e.getMessage)
        }
        return successful;
    }

    public Account updateAccount(Account account){
        HttpEntity<Account> accountHttpEntity = makeAccountEntity(account);

        try{
            ResponseEntity<Account> response = restTemplate.exchange(ACCOUNT_BASE_URL, HttpMethod.PUT, accountHttpEntity, Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
            //TODO: add a logger here. log(e.getMessage)
        }
        return account;
    }


    private HttpEntity<Account> makeAccountEntity(Account account){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken.getToken());
        HttpEntity<Account> accountNumEntity = new HttpEntity<>(account, headers);
        return accountNumEntity;

    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken.getToken());
        return new HttpEntity<>(headers);
    }
}
