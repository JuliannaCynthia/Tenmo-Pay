package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.DaoException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
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
            Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class,
                    transfer.getTransferToUsername(),
                    transfer.getTransferFromUsername(),
                    transfer.getTransferAmount(),
                    transfer.isPending(),
                    transfer.isApproved());

            createdTransfer = getTransferById(newTransferId);

        } catch (DataAccessException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return createdTransfer;
    }

    @Override
    public Integer respondToTransferRequest(Transfer transfer) {
        int rowsAffected = 0;
        String sql = "UPDATE transfer SET is_approved = ?, is_pending = false " +
                "WHERE transfer_id = ? ;";

        try {
            rowsAffected = jdbcTemplate.update(sql, transfer.isApproved(), transfer.getTransferId());
        }  catch (DataAccessException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return rowsAffected;
    }


    @Override
    public List<TransferDTO> viewTransferFromHistory(String transferFromUsername, String transferToUserName) {
        List<TransferDTO> transferList = new ArrayList<>();
        String sql = SELECT_TRANSFER_BASE_SQL + "WHERE user_transfer_from = ?";

        //checks if friendUserName was input as a requestParameter and adjust sql as necessary.
        if(transferToUserName != null){
            sql = " AND user_transfer_to = ?;";
        }

        try {
            SqlRowSet results;
            if(transferToUserName != null) {
                results = jdbcTemplate.queryForRowSet(sql, transferFromUsername, transferToUserName);
            } else {
                results = jdbcTemplate.queryForRowSet(sql, transferFromUsername);
            }
                while (results.next()) {
                    transferList.add(mapRowSetToTransferDTO(results));
                }

        } catch (DataAccessException e){
            throw new DaoException(e.getMessage(), e);
        }
        return transferList;
    }

    @Override
    public List<TransferDTO> viewTransferToHistory(String transferToUsername, String transferFromUsername){
        List<TransferDTO> transferList = new ArrayList<>();
        String sql = SELECT_TRANSFER_BASE_SQL + "WHERE user_transfer_to = ?";

        if(transferFromUsername != null){
            sql = " AND user_transfer_from = ?;";
        }

        try {
            SqlRowSet results;
            if(transferFromUsername != null) {
                results = jdbcTemplate.queryForRowSet(sql, transferToUsername, transferFromUsername);
            } else {
                results = jdbcTemplate.queryForRowSet(sql, transferToUsername);
            }
            while(results.next()){
                transferList.add(mapRowSetToTransferDTO(results));
            }
        } catch (DataAccessException e){
            throw new DaoException(e.getMessage(), e);
        }
        return transferList;
    }

    @Override
    public List<TransferDTO> viewPendingTransfers(String username) {
        List<TransferDTO> transferList = new ArrayList<>();
        String sql = SELECT_TRANSFER_BASE_SQL + "WHERE (user_transfer_from = ? " +
                                                "OR user_transfer_to = ?) " +
                                                "AND is_pending = true ;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);
            while(results.next()){
                transferList.add(mapRowSetToTransferDTO(results));
            }
        }catch (DataAccessException e){
            throw new DaoException(e.getMessage(), e);
        }
        return transferList;
    }

    private Transfer mapRowSetToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferAmount(rowSet.getBigDecimal("transfer_amount"));
        transfer.setTransferToUsername(rowSet.getString("user_transfer_to"));
        transfer.setTransferFromUsername(rowSet.getString("user_transfer_from"));
        transfer.setPending(rowSet.getBoolean("is_pending"));
        transfer.setApproved(rowSet.getBoolean("is_approved"));

        return transfer;
    }

    private TransferDTO mapRowSetToTransferDTO(SqlRowSet rowSet){
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setTransferId(rowSet.getInt("transfer_id"));
        transferDTO.setTransferAmount(rowSet.getBigDecimal("transfer_amount"));
        transferDTO.setTransferFromUsername(rowSet.getString("user_transfer_from"));
        transferDTO.setTransferToUsername(rowSet.getString("user_transfer_to"));

        return transferDTO;
    }
}
