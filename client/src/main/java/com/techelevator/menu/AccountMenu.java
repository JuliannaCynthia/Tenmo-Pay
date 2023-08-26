package com.techelevator.menu;

import com.techelevator.model.Account;
import com.techelevator.model.UserToken;
import com.techelevator.services.AccountsService;

public class AccountMenu {
    private UserToken userToken;
    private AccountsService accountsService;
    private Account[] userAccounts;

    public AccountMenu(UserToken userToken) {
        this.userToken = userToken;
        accountsService = new AccountsService(userToken);

    }
    public void run(){

    }
}
