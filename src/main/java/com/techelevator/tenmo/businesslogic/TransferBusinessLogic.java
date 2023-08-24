package com.techelevator.tenmo.businesslogic;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

public class TransferBusinessLogic {
    @Autowired
    AccountDao accountDao;

    public boolean isToSameAccount(Principal principal, Transfer transfer){
            String username = principal.getName();
            String transferToUsername =  transfer.getTransferToUsername();
        return username.equals(transferToUsername);
    }

}
