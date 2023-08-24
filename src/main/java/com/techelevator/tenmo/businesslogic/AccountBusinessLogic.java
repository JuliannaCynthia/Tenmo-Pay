package com.techelevator.tenmo.businesslogic;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;

public class AccountBusinessLogic {

    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;

    public AccountBusinessLogic(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userDao = new JdbcUserDao(jdbcTemplate);
    }

    public boolean

}
