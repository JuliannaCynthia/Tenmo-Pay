package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferById(Integer transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, user_transfer_to, user_transfer_from, transfer_amount, is_pending, is_approved " +
                "FROM transfer WHERE transfer_id = ?;";
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
    public Transfer approveTransfer(Transfer transfer) {
        return null;
    }

    @Override
    public List<Transfer> viewTransferHistory() {
        return null;
    }



    @Override
    public List<Transfer> viewTransfersByFriendUserName(String friendUsername) {
        return null;
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
