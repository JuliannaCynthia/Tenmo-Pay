package com.techelevator.tenmo.businesslogic;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;

public class TransferBusinessLogic {

    AccountDao accountDao;
    public TransferBusinessLogic(AccountDao accountDao){
        this.accountDao = accountDao;
    }

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

    public Account subtractFromSenderAccount(Transfer transfer, Account account){
        BigDecimal transferAmount = transfer.getTransferAmount();
        BigDecimal accountBalance = account.getBalance();

        account.setBalance(accountBalance.subtract(transferAmount));
        return account;
    }

    public Account addToReceivingAccount(Transfer transfer, Account account){
        BigDecimal transferAmount = transfer.getTransferAmount();
        BigDecimal accountBalance = account.getBalance();

        account.setBalance(accountBalance.add(transferAmount));
        return account;
    }

    public boolean isResponderSender(Principal principal, Transfer transfer){
        return principal.getName().equals(transfer.getTransferFromUsername());
    }

    public boolean isCreaterSender(Principal principal, Transfer transfer){
        return principal.getName().equals(transfer.getTransferFromUsername());
    }




}
