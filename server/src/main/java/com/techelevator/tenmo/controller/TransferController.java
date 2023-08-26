package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.businesslogic.TransferBusinessLogic;
import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Logger;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/transfer")
@RestController
public class TransferController {
    File file = new File("transferlogs.txt");
    Logger log = new Logger(file);
    private final TransferBusinessLogic businessLogic;
    private final AccountDao accountDao;
    private final TransferDao transferDao;

    public TransferController(JdbcTemplate jdbcTemplate) {
        this.accountDao = new JdbcAccountDao(jdbcTemplate);
        this.transferDao = new JdbcTransferDao(jdbcTemplate);
        this.businessLogic = new TransferBusinessLogic(accountDao);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public TransferDTO createTransfer(Principal principal, @Valid @RequestBody Transfer transfer) {
        if (!transferDao.transferCredentialsAreNotFriends(transfer)) {
            log.write(principal.getName() + " encountered an Error. Message -> (You must be friends to send a transfer.)");
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "You must be friends to send a transfer.");
        }
        if (businessLogic.isCreatorSender(principal, transfer) && businessLogic.senderHasEnoughMoney(transfer) && !businessLogic.isToSameAccount(principal, transfer)) {
            transfer.setApproved(true);
            transfer.setPending(false);
            Account sendingAccount = accountDao.getAccountById(transfer.getAccountNumberFrom());
            Account receivingAccount = accountDao.getAccountsByUser(transfer.getTransferToUsername()).get(0); //Default account for receiver

            transfer = transferDao.createTransfer(transfer);
            transferMoney(transfer, sendingAccount, receivingAccount);
            log.write(principal.getName() + " sent a transfer request to -> "+ transfer.getTransferToUsername());
            return mapTransferToTransferDTO(transfer);

        } else if (!businessLogic.isToSameAccount(principal, transfer)) {
            log.write(principal.getName() + " sent a transfer request to -> "+ transfer.getTransferFromUsername());
            return mapTransferToTransferDTO(transferDao.createTransfer(transfer));
        } else {
            log.write(principal.getName() + " encountered an Error. Message -> (Cannot send to the same account)");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public boolean respondToTransferRequest(Principal principal, @Valid @RequestBody Transfer transfer) {
        if (businessLogic.isResponderSender(principal, transfer)) {
            boolean isApproved = transfer.isApproved();
            Account sendingAccount = accountDao.getAccountById(transfer.getAccountNumberFrom());
            Account receivingAccount = accountDao.getAccountById(transfer.getAccountNumberTo());

            if (isApproved && businessLogic.senderHasEnoughMoney(transfer)) {
                if (transferDao.respondToTransferRequest(transfer) != 1) {
                    log.write(principal.getName() + " encountered an Error. Message -> (Something went wrong, transfer was not updated.)");
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong, transfer was not updated");
                }
                log.write(principal.getName() + " accepted transfer.");
                return transferMoney(transfer, sendingAccount, receivingAccount);
            }
        } else {
            log.write(principal.getName() + " encountered an Error. Message -> (Only the sender may approve a transfer request.)");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only sending may approve a transfer request");
        }
        return false;
    }


    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public List<TransferDTO> transferHistory(Principal principal,
                                             @RequestParam(required = false) String friendUsername) {

        List<TransferDTO> transferList = transferDao.viewTransferFromHistory(principal.getName(), friendUsername);
        transferList.addAll(transferDao.viewTransferToHistory(principal.getName(), friendUsername));
        for(TransferDTO transferDTO:transferList){
            double ID = encryptId(transferDTO.getTransferId());
            log.write(principal.getName() + " viewed transfer history, current Transfer #" + ID);
        }
        return transferList;
    }

    @RequestMapping(value = "/history/by_id", method = RequestMethod.POST)
    public Transfer getTransferById(@RequestBody Transfer transfer) {
        int transferId = transfer.getTransferId();
        double ID = encryptId(transferId);
        log.write("User viewed pending Transfer #" + ID);
        return transferDao.getTransferById(transferId);
    }

    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public List<TransferDTO> viewPendingTransfers(Principal principal) {
        List<TransferDTO> view = transferDao.viewPendingTransfers(principal.getName());
        for(TransferDTO transferDTO: view){
           double ID = encryptId(transferDTO.getTransferId());
           log.write(principal.getName() + " viewed pending Transfer #" + ID);
        }
        return view;
    }

    private TransferDTO mapTransferToTransferDTO(Transfer transfer) {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setTransferId(transfer.getTransferId());
        transferDTO.setTransferAmount(transfer.getTransferAmount());
        transferDTO.setTransferFromUsername(transfer.getTransferFromUsername());
        transferDTO.setTransferToUsername(transfer.getTransferToUsername());
        return transferDTO;
    }

    private boolean transferMoney(Transfer transfer, Account sendingAccount, Account receivingAccount){
        sendingAccount = businessLogic.subtractFromSenderAccount(transfer, sendingAccount);
        receivingAccount = businessLogic.addToReceivingAccount(transfer, receivingAccount);
        accountDao.updateAccount(sendingAccount);
        accountDao.updateAccount(receivingAccount);
        return true;
    }

    public static double encryptId(int id){
        double encrypt = id;
        encrypt = encrypt/24;
        encrypt += 42;
        return encrypt;
    }

}
