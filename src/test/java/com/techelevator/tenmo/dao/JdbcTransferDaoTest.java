package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class JdbcTransferDaoTest extends BaseDaoTests{
    //VALUES ('1001', '1002', 100, false, true);
    private final Transfer TEST_TRANSFER = new Transfer(3001, new BigDecimal(100), 1002, 1001, false, true);
    private TransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getTransferById_returns_correct_transfer_id() {
        int transferId = TEST_TRANSFER.getTransferId();

        Transfer testGetTransfer = sut.getTransferById(transferId);

        assertNotNull("getTransferById unexpectedly returned null", testGetTransfer);
        assertEquals("getTransferById did not return correct id", transferId, testGetTransfer.getTransferId());
    }

    @Test
    public void createTransfer_successfully_returns_created_transfer() {
        Transfer newTransfer = new Transfer();
        newTransfer.setTransferFromUserId(1001);
        newTransfer.setTransferToUserId(1002);
        newTransfer.setTransferAmount(new BigDecimal("100.00"));

        Transfer createdTransfer = sut.createTransfer(newTransfer);
        newTransfer.setTransferId(3002);

        assertNotNull("createTransfer unexpectedly returned null", createdTransfer);
        assertTransfersMatch("CreatedTransfer did not return correct transfer information", newTransfer, createdTransfer);
    }

    @Test
    public void respondToTransferRequest() {
    }

    @Test
    public void viewTransferFromHistory() {
    }

    @Test
    public void viewTransferToHistory() {
    }

    @Test
    public void viewPendingTransfers() {
    }

    private void assertTransfersMatch(String message, Transfer expected, Transfer actual){
        assertEquals(message, expected.getTransferFromUserId(), actual.getTransferFromUserId());
        assertEquals(message, expected.getTransferToUserId(), actual.getTransferToUserId());
        assertEquals(message, expected.getTransferAmount(), actual.getTransferAmount());
        assertEquals(message, expected.isPending(), actual.isPending());
        assertEquals(message, expected.isApproved(), actual.isApproved());
    }
}