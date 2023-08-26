package com.techelevator.services;

import com.techelevator.model.Account;
import com.techelevator.model.UserToken;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountsServiceTest {

    AccountsService accountsService;

    @Before
    public void setUp(){
        UserToken bobToken = new UserToken();
        bobToken.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJib2IiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjkzMDg4NDkxfQ.6C8DOmhS8NOESXhzs9hh-Bq_o6WncbUQaiOi1jbyBg_-QRwzEZER8z_JRkLIyi0_oWVPeQRTT6KBqUYY2ryung");
        accountsService = new AccountsService(bobToken);


    }
    @Test
    public void getUserAccounts() {
        Account account = new Account();
        account.setAccountId(2001);
        account = accountsService.getAccount(account);
        BigDecimal expected = new BigDecimal("1000.00");
        assertEquals("Account balance should return 1000", expected, account.getBalance());
    }

    @Test
    public void createAccount() {
    }

    @Test
    public void getAccount() {
    }

    @Test
    public void deleteAccount() {
    }
}