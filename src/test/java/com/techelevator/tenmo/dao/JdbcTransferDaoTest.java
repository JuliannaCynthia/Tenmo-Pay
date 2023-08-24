package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcTransferDaoTest extends BaseDaoTests{

    private final Transfer TEST_TRANSFER1 = new Transfer(3001, new BigDecimal(100), "user", "bob", false, true);
    private final Transfer TEST_TRANSFER2 = new Transfer(3002, new BigDecimal(100), "bob", "user", true, false);
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
        newTransfer.setTransferFromUsername("bob");
        newTransfer.setTransferToUsername("user");
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
        newTransfer.setTransferFromUsername("bob");
        newTransfer.setTransferToUsername("user");
        newTransfer.setTransferAmount(new BigDecimal("100.00"));

        sut.createTransfer(newTransfer);
        sut.createTransfer(newTransfer);


        List<TransferDTO> transferHistory = sut.viewTransferFromHistory("user", null);
        assertEquals("Expected one transfer returned", 1, transferHistory.size());
    }

    @Test
    public void viewTransferToHistory_returns_correct_number_of_transfers() {
        Transfer newTransfer = new Transfer();
        newTransfer.setTransferFromUsername("bob");
        newTransfer.setTransferToUsername("user");
        newTransfer.setTransferAmount(new BigDecimal("100.00"));

        sut.createTransfer(newTransfer);
        sut.createTransfer(newTransfer);

        List<TransferDTO> transferHistory = sut.viewTransferToHistory("user", null);
        assertEquals("Expected three transfers returned", 3, transferHistory.size());

    }

    @Test
    public void viewPendingTransfers() {
        List<TransferDTO> pendingTransfers = sut.viewPendingTransfers("bob");
        assertEquals("Expected one pending transfer",1,pendingTransfers.size());
    }

    private void assertTransfersMatch(String message, Transfer expected, Transfer actual){
        assertEquals(message, expected.getTransferFromUsername(), actual.getTransferFromUsername());
        assertEquals(message, expected.getTransferToUsername(), actual.getTransferToUsername());
        assertEquals(message, expected.getTransferAmount(), actual.getTransferAmount());
        assertEquals(message, expected.isPending(), actual.isPending());
        assertEquals(message, expected.isApproved(), actual.isApproved());
    }
}