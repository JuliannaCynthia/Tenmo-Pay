package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcAccountDaoTest extends BaseDaoTests{

    private AccountDao sut;
    private UserDao userDao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
        userDao=new JdbcUserDao(jdbcTemplate);
        userDao.create("Test1", "password");
        userDao.create("Test2", "password");
        userDao.create("bub", "password");

    }
    @Test
    public void getAccountsByUser() {
        List<Account> test = sut.getAccountsByUser("Test1");
        assertEquals(1,test.size());
        sut.createAccount("Test1");
        test = sut.getAccountsByUser("Test1");
        assertEquals(2,test.size());

    }

    @Test
    public void getAccountById() {
        Account test = sut.getAccountById(2003);
        Account newTest = new Account();
        newTest.setUserId(1003);
        newTest.setAccountId(2003);
        BigDecimal tester = new BigDecimal("1000.00");
        newTest.setBalance(tester);
        assertAccountsMatch("Did not return correct Account for account_id",newTest,test);
    }

    @Test
    public void createAccount() {
        Account test = sut.createAccount("Test1");
        BigDecimal tester = new BigDecimal("1000.00");
        Account expected = new Account();
        expected.setUserId(1003);
        expected.setAccountId(2006);
        expected.setBalance(tester);
        assertAccountsMatch("Did not create the expected Account.",expected,test);
    }

    @Test
    public void updateAccount() {
        Account test = sut.getAccountById(2003);
        BigDecimal tester = new BigDecimal("1500.00");
        test.setBalance(tester);
        Account actual = sut.updateAccount(test);
        Account expected =new Account();
        expected.setUserId(1003);
        expected.setAccountId(2003);
        expected.setBalance(tester);
        assertAccountsMatch("Account did not update correctly.", expected, actual);
    }

    @Test
    public void deleteAccount() {
        Account test = sut.createAccount("Test1");
        int valTest = sut.deleteAccount(test);
        assertEquals(1,valTest);
    }


    private void assertAccountsMatch(String message, Account expected, Account actual){
        assertEquals(message, expected.getAccountId(), actual.getAccountId());
        assertEquals(message, expected.getUserId(), actual.getUserId());
        assertEquals(message, expected.getBalance(), actual.getBalance());
    }

}