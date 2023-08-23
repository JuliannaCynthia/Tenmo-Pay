package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface AccountDao {

    List<Account> getAccountsByUser(String username);
    Account getAccountById(int id);
    Account createAccount(String username);
    Account updateAccount(Account account);
    void deleteAccount(Account account);

}
