package com.techelevator.tenmo.businesslogic;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.security.Principal;

import static org.junit.Assert.*;

public class TransferBusinessLogicTest {
    @Autowired
    AccountDao accountDao;

    TransferBusinessLogic businessLogic;
    Principal principal;
    Transfer transfer;
    @Before
    public void setUp() {
        businessLogic = new TransferBusinessLogic(accountDao);
        principal = new Principal() {

            @Override
            public String getName() {
                return "bob";
            }
        };



    }

    @Test
    public void isToSameAccount_returns_true_when_account_from_and_account_to_are_the_same() {
        transfer = new Transfer(3003, new BigDecimal("100.00"), "bob", 2001, "bob", true, false);

        boolean isSameAccount = businessLogic.isToSameAccount(principal, transfer);

        assertTrue("should return true if the transfer to account is the same as the principal sender", isSameAccount);
    }

    @Test
    public void senderHasEnoughMoney_returns_false_when_senders_balance_is_less_than_transfer_amount() {
        transfer = new Transfer(3003, new BigDecimal("10000.00"), "bob", 2001, "user", true, false);
        boolean hasEnough = businessLogic.senderHasEnoughMoney(transfer);

        assertFalse("senderHasEnoughMoney should return false when transfer is larger than account balance", hasEnough);


    }

    @Test
    public void subtractFromSenderAccount() {
    }

    @Test
    public void addToReceivingAccount() {
    }
}