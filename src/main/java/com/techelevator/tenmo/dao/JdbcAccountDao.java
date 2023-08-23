package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> getAccountsByUser(String username) {
        List<Account> accounts = null;
        String sqlUser = "select user_id from tenmo_user where username = ?;";
        String sql = "select * from account where user_id = ?";
        try{
            int userId = jdbcTemplate.queryForObject(sqlUser,int.class,username);
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,userId);
            accounts = new ArrayList<>();
            while(results.next()){
                accounts.add(mapRowToAccount(results));
            }
        }catch(CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return accounts;
    }

    @Override
    public Account getAccountById(int id) {
        String sql = "select * from account where account_id = ?;";
        Account account = null;
        try{
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,id);
            if(rowSet.next()){
                account= mapRowToAccount(rowSet);
            }
        }catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return account;
    }

    @Override
    public Account createAccount(String username) {
        String sqlUser = "select user_id from tenmo_user where username = ?;";
        String sqlInsert = "insert into account (user_id, balance) values (?,?) returning account_id;";
        int accountId;
        try{
            int userId = jdbcTemplate.queryForObject(sqlUser,int.class,username);
            BigDecimal n = new BigDecimal("1000.00");
            accountId =jdbcTemplate.queryForObject(sqlInsert,int.class,userId,n);


        }catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return getAccountById(accountId);
    }

    @Override
    public Account updateAccount(Account account) {
        String sql = "update account set account_id =?, user_id = ?, balance = ? where account_id = ?;";
        int rowsAffected = 0;
        try{
            rowsAffected = jdbcTemplate.update(sql,account.getAccountId(),account.getUserId(),account.getBalance(),account.getAccountId());
            System.out.println(rowsAffected);
            if(rowsAffected!=1){
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
            }
        }catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return getAccountById(account.getAccountId());
    }

    @Override
    public void deleteAccount(Account account) {
        String sql = "delete from account where account_id = ? and user_id = ?;";
        int rowsAffected = 0;
        try{
            rowsAffected = jdbcTemplate.update(sql,account.getAccountId(),account.getUserId());
        }catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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
