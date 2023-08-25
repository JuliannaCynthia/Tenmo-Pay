package com.techelevator.tenmo.businesslogic;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.BaseDaoTests;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.security.Principal;

import static org.junit.Assert.*;

public class TransferBusinessLogicTest extends BaseDaoTests {

    AccountDao accountDao;
    JdbcTemplate jdbcTemplate;
    TransferBusinessLogic businessLogic;
    Principal principal;
    Transfer transfer;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        accountDao = new JdbcAccountDao(jdbcTemplate);
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
    public void senderHasEnoughMoney_returns_true_when_senders_balance_is_greater_than_transfer_amount() {
        transfer = new Transfer(3003, new BigDecimal("10.00"), "bob", 2001, "user", true, false);
        boolean hasEnough = businessLogic.senderHasEnoughMoney(transfer);

        assertTrue("senderHasEnoughMoney should return true when transfer is smaller than account balance", hasEnough);

    }

    @Test
    public void senderHasEnoughMoney_returns_true_when_senders_balance_is_equal_to_transfer_amount() {
        transfer = new Transfer(3003, new BigDecimal("1000.00"), "bob", 2001, "user", true, false);
        boolean hasEnough = businessLogic.senderHasEnoughMoney(transfer);

        assertTrue("senderHasEnoughMoney should return true when transfer is equal to the account balance", hasEnough);

    }

    @Test
    public void subtractFromSender_subtracts_appropriate_amount() {
        transfer = new Transfer(3003, new BigDecimal("100.00"), "bob", 2001, "user", true, false);
        Account account = new Account(2001, 1001, new BigDecimal("1000.00"));
        BigDecimal expected = new BigDecimal("900.00");

        account = businessLogic.subtractFromSenderAccount(transfer, account);

        assertEquals("Account balance should be $900", expected, account.getBalance() );
    }

    @Test
    public void addToReceivingAccount_adds_appropriate_amount() {
        transfer = new Transfer(3003, new BigDecimal("100.00"), "bob", 2001, "user", true, false);
        Account account = new Account(2001, 1001, new BigDecimal("1000.00"));
        BigDecimal expected = new BigDecimal("1100.00");

        account = businessLogic.addToReceivingAccount(transfer, account);

        assertEquals("Account balance should be $1,100", expected, account.getBalance() );
    }
}