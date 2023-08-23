package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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
        String sql = "select tenmo_user.username, account.balance from account join tenmo_user on account.user_id = tenmo_user.user_id where tenmo_user.user_id = ?;";
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

        try{
            return mapRowToAccount(jdbcTemplate.queryForRowSet(sql,id));
        }catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Account createAccount(String username) {
        String sqlUser = "select user_id from tenmo_user where username = ?;";
        String sqlInsert = "insert into account (user_id, balance) values (?,?) returning account_id;";
        int accountId;
        try{
            int userId = jdbcTemplate.queryForObject(sqlUser,int.class,username);
            accountId = jdbcTemplate.queryForObject(sqlInsert,int.class,userId,1000);

        }catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return getAccountById(accountId);
    }

    @Override
    public Account updateAccount(Account account) {
        String sql = "update account set user_id = ?, balance = ? where account_id = ?;";
        int rowsAffected = 0;
        try{
            rowsAffected = jdbcTemplate.update(sql,account.getUserId(),account.getBalance(),account.getAccountId());
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
        account.setAccountId(rowSet.getInt("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        account.setUserId(rowSet.getInt("user_id"));
        return account;
    }
}
