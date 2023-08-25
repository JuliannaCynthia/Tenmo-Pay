package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{
    File file = new File("accountlogs.txt");
    Logger log = new Logger(file);
    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userDao = new JdbcUserDao(jdbcTemplate);
    }

    @Override
    public List<Account> getAccountsByUser(String username) {
        List<Account> accounts;
        String sql = "SELECT account_id,user_id,balance FROM account WHERE user_id = ?";
        try{
            int userId = userDao.findIdByUsername(username);
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,userId);
            accounts = new ArrayList<>();
            while(results.next()){
                accounts.add(mapRowToAccount(results));
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            log.write(username + " encountered an Error. Data Error or Connection Error.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return accounts;
    }

    @Override
    public Account getAccountById(int id) {
        String sql = "SELECT account_id,user_id,balance FROM account WHERE account_id = ?;";
        Account account = null;
        try{
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,id);
            if(rowSet.next()){
                account= mapRowToAccount(rowSet);
            }
        }catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            log.write("Encountered an Error. Data Error or Connection Error.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return account;
    }

    @Override
    public Account createAccount(String username) {
        String sqlInsert = "INSERT INTO account (user_id, balance) VALUES (?,?) RETURNING account_id;";
        int accountId;
        try{
            int userId = userDao.findIdByUsername(username);

            BigDecimal n = new BigDecimal("1000.00");
            accountId =jdbcTemplate.queryForObject(sqlInsert,int.class,userId,n);

        }catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            log.write(username + " encountered an Error. Data Error or Connection Error.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return getAccountById(accountId);
    }

    @Override
    public Account updateAccount(Account account) {
        String sql = "UPDATE account SET account_id =?, user_id = ?, balance = ? WHERE account_id = ?;";
        int rowsAffected;
        try{
            rowsAffected = jdbcTemplate.update(sql,account.getAccountId(),account.getUserId(),account.getBalance(),account.getAccountId());
            System.out.println(rowsAffected);
            if(rowsAffected!=1){
                log.write("Encountered an Error. Message -> (Not Found. No update performed)");
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
            }
        }catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            log.write("Encountered an Error. Data Error or Connection Error.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return getAccountById(account.getAccountId());
    }

    @Override
    public int deleteAccount(Account account) {
        String sql = "DELETE FROM account WHERE account_id = ? AND user_id = ?;";
        int rows;
        try{
           rows = jdbcTemplate.update(sql,account.getAccountId(),account.getUserId());
           if(rows!=1){
               log.write("Encountered an Error. Message -> (Not Found. No update performed)");
               throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED);
           }
           return rows;
        }catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            log.write("Encountered an Error. Data Error or Connection Error.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Account mapRowToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setUserId(rowSet.getInt("user_id"));
        account.setAccountId(rowSet.getInt("account_id"));
        BigDecimal balance = rowSet.getBigDecimal("balance");
        account.setBalance(balance);

        return account;
    }
}
