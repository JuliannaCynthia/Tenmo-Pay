package com.techelevator.tenmo.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.*;

public class JdbcAccountDaoTest extends BaseDaoTests{

    private AccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);

    }
    @Test
    public void getAccountsByUser() {
    }

    @Test
    public void getAccountById() {
    }

    @Test
    public void createAccount() {
    }

    @Test
    public void updateAccount() {
    }

    @Test
    public void deleteAccount() {
    }

    @Test
    public void mapRowToAccount() {
    }
}