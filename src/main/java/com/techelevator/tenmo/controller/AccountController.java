package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
    @Autowired
    AccountDao jdbcAccount;
    @Autowired
    UserDao jdbcUser;


    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public List<AccountDTO> getAccount(Principal principal){
        List<AccountDTO> finalList = new ArrayList<>();
        List<Account> prelist = jdbcAccount.getAccountsByUser(principal.getName());
        int counter=0;
        for(Account account: prelist){
            counter++;
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setUsername(principal.getName()+ " Account #"+counter);
            accountDTO.setAccountBalance(account.getBalance());
            finalList.add(accountDTO);
        }
        return finalList;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/account", method = RequestMethod.POST)
    public Account newAccount(Principal principal){
        return jdbcAccount.createAccount(principal.getName());
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/account", method = RequestMethod.DELETE)
    public void delete(@Valid @RequestBody Account account, Principal principal){
        int userId = jdbcUser.findIdByUsername(principal.getName());
        if(userId != account.getUserId()){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Not Authorized. Please sign in as the correct user.");
        }else {
            jdbcAccount.deleteAccount(account);
        }
    }

    @RequestMapping(path = "/account", method = RequestMethod.PUT)
    public Account update(@Valid @RequestBody Account account, Principal principal){
        int userId = jdbcUser.findIdByUsername(principal.getName());
        if(userId != account.getUserId()){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Not Authorized. Please sign in as the correct user.");
        }else {
            return jdbcAccount.updateAccount(account);
        }
    }

    @RequestMapping(path = "/account/{accountId}", method = RequestMethod.GET)
    public AccountDTO getAccountById(Principal principal, @PathVariable int accountId){
        int userId = jdbcUser.findIdByUsername(principal.getName());
        Account account = jdbcAccount.getAccountById(accountId);
        if(userId!=account.getUserId()){
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Invalid access.");
        }else {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setUsername(principal.getName());
            accountDTO.setAccountBalance(account.getBalance());
            return accountDTO;
        }
    }
}
