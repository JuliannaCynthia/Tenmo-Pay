package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
    @Autowired
    AccountDao jdbcAccount;


    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public List<Account> getAccount(Principal principal){
        return jdbcAccount.getAccountsByUser(principal.getName());
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/account", method = RequestMethod.POST)
    public Account newAccount(Principal principal){
        return jdbcAccount.createAccount(principal.getName());
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/account", method = RequestMethod.DELETE)
    public void delete(Account account){
        jdbcAccount.deleteAccount(account);
    }

    @RequestMapping(path = "/account", method = RequestMethod.PUT)
    public Account update(Account account){
        return jdbcAccount.updateAccount(account);
    }


}
