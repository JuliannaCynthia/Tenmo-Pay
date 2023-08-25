package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AccountDTO;
import com.techelevator.tenmo.model.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
    File file = new File("accountlogs.txt");
    Logger log = new Logger(file);
    @Autowired
    AccountDao jdbcAccount;
    @Autowired
    UserDao jdbcUser;


    @RequestMapping(path = "/account/all", method = RequestMethod.GET)
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
            double logId = encryptAccountId(account.getAccountId());
            log.write(principal.getName() + " accessed and viewed Account #" +logId+principal.getName().charAt(0)+principal.getName().charAt(principal.getName().length()-1));
        }
        return finalList;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/account/create", method = RequestMethod.POST)
    public Account newAccount(Principal principal){
        Account account = jdbcAccount.createAccount(principal.getName());
        double logId = encryptAccountId(account.getAccountId());
        log.write(principal.getName() + " created Account #" +logId+principal.getName().charAt(0)+principal.getName().charAt(principal.getName().length()-1));
        return account;
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/account", method = RequestMethod.DELETE)
    public void delete(@Valid @RequestBody Account account, Principal principal){
        int userId = jdbcUser.findIdByUsername(principal.getName());
        if(userId != account.getUserId()){
            log.write(principal.getName() + " encountered an error. Deletion failed. Error message (Not Authorized. Please sign in as the correct user.)");
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Not Authorized. Please sign in as the correct user.");
        }else {
            jdbcAccount.deleteAccount(account);
            log.write(principal.getName() + " successfully deleted Account #" + account.getAccountId());
        }
    }

    @RequestMapping(path = "/account", method = RequestMethod.PUT)
    public Account update(@Valid @RequestBody Account account, Principal principal){
        int userId = jdbcUser.findIdByUsername(principal.getName());
        if(userId != account.getUserId()){
            log.write(principal.getName() + " encountered an error. Update failed. Error message (Not Authorized. Please sign in as the correct user.)");
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Not Authorized. Please sign in as the correct user.");
        }else {
            Account account1 = jdbcAccount.updateAccount(account);
            double logId = encryptAccountId(account1.getAccountId());
            log.write(principal.getName() + "  successfully updated Account #" +logId+principal.getName().charAt(0)+principal.getName().charAt(principal.getName().length()-1));
            return account1;
        }
    }

    @RequestMapping(path = "/account", method = RequestMethod.POST)
    public AccountDTO getAccountById(Principal principal, @RequestBody int accountId){
        int userId = jdbcUser.findIdByUsername(principal.getName());
        Account account = jdbcAccount.getAccountById(accountId);
        if(userId!=account.getUserId()){
            log.write(principal.getName() + " encountered an error. Error message (Invalid access.)");
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Invalid access.");
        }else {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setUsername(principal.getName());
            accountDTO.setAccountBalance(account.getBalance());
            double logId = encryptAccountId(account.getAccountId());
            log.write(principal.getName() + " accessed and viewed Account #" +logId+principal.getName().charAt(0)+principal.getName().charAt(principal.getName().length()-1));
            return accountDTO;
        }
    }

    public static double encryptAccountId(int id){
        double encrypt = id;
        encrypt = encrypt/24;
        encrypt += 42;
        return encrypt;
    }
}
