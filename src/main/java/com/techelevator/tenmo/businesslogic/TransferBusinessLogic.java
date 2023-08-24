package com.techelevator.tenmo.businesslogic;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.security.Principal;

public class TransferBusinessLogic {
    @Autowired
    AccountDao accountDao;

    public boolean isToSameAccount(Principal principal, Transfer transfer){
            String username = principal.getName();
            String transferToUsername =  transfer.getTransferToUsername();
        return username.equals(transferToUsername);
    }

    public boolean senderHasEnoughMoney(Transfer transfer){
        Account account = accountDao.getAccountById(transfer.getAccountNumberFrom());
        BigDecimal accountBalance = account.getBalance();
        BigDecimal transferAmount = transfer.getTransferAmount();

        return accountBalance.compareTo(transferAmount) >= 0;
    }





}
