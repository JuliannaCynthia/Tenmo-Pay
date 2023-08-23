package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;
    private final String SELECT_TRANSFER_BASE_SQL = "SELECT transfer_id, user_transfer_to, user_transfer_from, transfer_amount, is_pending, is_approved " +
            "FROM transfer ";

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferById(Integer transferId) {
        Transfer transfer = null;
        String sql = SELECT_TRANSFER_BASE_SQL + " WHERE transfer_id = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
            if(result.next()){
                transfer = mapRowSetToTransfer(result);
            }
        } catch (DataAccessException e){
            throw new DaoException(e.getMessage(), e);
        }
        return transfer;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        Transfer createdTransfer;
        String sql = "INSERT INTO transfer (user_transfer_to, user_transfer_from, transfer_amount, is_pending, is_approved) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id; ";

        try {
            Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferToUserId(), transfer.getTransferFromUserId(), transfer.getTransferAmount(), transfer.isPending(), transfer.isApproved());
            createdTransfer = getTransferById(newTransferId);

        } catch (DataAccessException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return createdTransfer;
    }

    @Override
    public Integer approveTransfer(Transfer transfer) {
        Integer rowsAffected = 0;
        String sql = "UPDATE transfer SET is_approved = true, is_pending = false " +
                "WHERE transfer_id = ?;";

        try {
            rowsAffected = jdbcTemplate.update(sql, Integer.class, transfer.getTransferId());
        }  catch (DataAccessException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return rowsAffected;
    }

    //TODO: make optional requestParam in controller
    @Override
    public List<Transfer> viewTransferFromHistory(int userId, Integer friendUserId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = SELECT_TRANSFER_BASE_SQL + "WHERE user_transfer_from = ?";

        //checks if friendUserName was input as a requestParameter and adjust sql as necessary.
        if(friendUserId != null){
            sql = " AND user_transfer_to = ?;";
        }

        try {
            SqlRowSet results;
            if(friendUserId != null) {
                results = jdbcTemplate.queryForRowSet(sql, userId, friendUserId);
            } else {
                results = jdbcTemplate.queryForRowSet(sql, userId);
            }
                while (results.next()) {
                    transferList.add(mapRowSetToTransfer(results));
                }

        } catch (DataAccessException e){
            throw new DaoException(e.getMessage(), e);
        }
        return transferList;
    }

    @Override
    public List<Transfer> viewTransferToHistory(int userId, Integer friendUserId){
        List<Transfer> transferList = new ArrayList<>();
        String sql = SELECT_TRANSFER_BASE_SQL + "WHERE user_transfer_to = ?";

        if(friendUserId != null){
            sql = " AND user_transfer_from = ?;";
        }

        try {
            SqlRowSet results;
            if(friendUserId != null) {
                results = jdbcTemplate.queryForRowSet(sql, userId, friendUserId);
            } else {
                results = jdbcTemplate.queryForRowSet(sql, userId);
            }
            while(results.next()){
                transferList.add(mapRowSetToTransfer(results));
            }
        } catch (DataAccessException e){
            throw new DaoException(e.getMessage(), e);
        }
        return transferList;
    }

    @Override
    public List<Transfer> viewPendingTransfers() {
        return null;
    }

    private Transfer mapRowSetToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferAmount(rowSet.getBigDecimal("transfer_amount"));
        transfer.setTransferToUserId(rowSet.getInt("user_transfer_to"));
        transfer.setTransferFromUserId(rowSet.getInt("user_transfer_from"));
        transfer.setPending(rowSet.getBoolean("is_pending"));
        transfer.setApproved(rowSet.getBoolean("is_approved"));

        return transfer;
    }
}
