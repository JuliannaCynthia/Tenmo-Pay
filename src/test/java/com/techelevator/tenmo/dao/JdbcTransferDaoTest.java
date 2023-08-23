package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcTransferDaoTest extends BaseDaoTests{

    private final Transfer TEST_TRANSFER1 = new Transfer(3001, new BigDecimal(100), 1002, 1001, false, true);
    private final Transfer TEST_TRANSFER2 = new Transfer(3002, new BigDecimal(100), 1001, 1002, true, false);
    private TransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getTransferById_returns_correct_transfer_id() {
        int transferId = TEST_TRANSFER1.getTransferId();

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
        newTransfer.setTransferId(3003);

        assertNotNull("createTransfer unexpectedly returned null", createdTransfer);
        assertTransfersMatch("CreatedTransfer did not return correct transfer information", newTransfer, createdTransfer);
    }

    @Test
    public void respondToTransferRequest_returns_correct_number_of_rows_updated() {
        Transfer respondingTransfer = TEST_TRANSFER2;
        respondingTransfer.setApproved(true);

        int rowsAffected = sut.respondToTransferRequest(respondingTransfer);
        assertEquals("Expected one row affected", 1, rowsAffected);
    }

    @Test
    public void respondToTransferRequest_returns_with_correct_pending_and_approved_values() {
        Transfer respondingTransfer = TEST_TRANSFER2;
        respondingTransfer.setApproved(true);

        sut.respondToTransferRequest(respondingTransfer);
        Transfer updatedTransfer = sut.getTransferById(3002);
        assertTrue("Expected is_approved to be true", updatedTransfer.isApproved());
        assertFalse("Expected is_pending to be false", updatedTransfer.isPending());
    }

    @Test
    public void viewTransferFromHistory_returns_correct_number_of_transfers() {
        Transfer newTransfer = new Transfer();
        newTransfer.setTransferFromUserId(1001);
        newTransfer.setTransferToUserId(1002);
        newTransfer.setTransferAmount(new BigDecimal("100.00"));

        sut.createTransfer(newTransfer);
        sut.createTransfer(newTransfer);


        List<Transfer> transferHistory = sut.viewTransferFromHistory(1002, null);
        assertEquals("Expected one transfer returned", 1, transferHistory.size());
    }

    @Test
    public void viewTransferToHistory_returns_correct_number_of_transfers() {
        Transfer newTransfer = new Transfer();
        newTransfer.setTransferFromUserId(1001);
        newTransfer.setTransferToUserId(1002);
        newTransfer.setTransferAmount(new BigDecimal("100.00"));

        sut.createTransfer(newTransfer);
        sut.createTransfer(newTransfer);

        List<Transfer> transferHistory = sut.viewTransferToHistory(1002, null);
        assertEquals("Expected three transfers returned", 3, transferHistory.size());

    }

    @Test
    public void viewPendingTransfers() {
        List<Transfer> pendingTransfers = sut.viewPendingTransfers(1001);
        assertEquals("Expected one pending transfer",1,pendingTransfers.size());
    }

    private void assertTransfersMatch(String message, Transfer expected, Transfer actual){
        assertEquals(message, expected.getTransferFromUserId(), actual.getTransferFromUserId());
        assertEquals(message, expected.getTransferToUserId(), actual.getTransferToUserId());
        assertEquals(message, expected.getTransferAmount(), actual.getTransferAmount());
        assertEquals(message, expected.isPending(), actual.isPending());
        assertEquals(message, expected.isApproved(), actual.isApproved());
    }
}