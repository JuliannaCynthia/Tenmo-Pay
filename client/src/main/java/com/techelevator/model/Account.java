package com.techelevator.model;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class Account {
    @Min(value = 2001, message = "Account cannot be registered with an invalid id.")
    private int accountId;
    @Min(value = 1001, message = "Account cannot be registered with an invalid id.")
    private int userId;

    private BigDecimal balance = new BigDecimal("1000");

    public Account(){}

    public Account(int accountId, int userId, BigDecimal balance){
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }


    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
