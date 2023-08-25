package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.businesslogic.FriendlyBusinessLogic;
import com.techelevator.tenmo.businesslogic.TransferBusinessLogic;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/transfer")
@RestController
public class TransferController {

    @Autowired
    private TransferDao transferDao;
    @Autowired
    private AccountDao accountDao;

    private final TransferBusinessLogic businessLogic = new TransferBusinessLogic();

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Transfer createTransfer(Principal principal, @Valid @RequestBody Transfer transfer) {
        if(!transferDao.transferCredentialsAreNotFriends(transfer)){
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "You must be friends to send a transfer.");
        }

        if (!businessLogic.isToSameAccount(principal, transfer) && businessLogic.senderHasEnoughMoney(transfer)) {
            return transferDao.createTransfer(transfer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid recipient: " + principal.getName());
        }
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Integer respondToTransferRequest(Principal principal, @Valid @RequestBody Transfer transfer) {
        boolean isApproved = transfer.isApproved();
        Account sendingAccount = accountDao.getAccountById(transfer.getAccountNumberFrom());
        Account receivingAccount = accountDao.getAccountById(transfer.getAccountNumberTo());

        if (isApproved && businessLogic.senderHasEnoughMoney(transfer)) {
            sendingAccount = businessLogic.subtractFromSenderAccount(transfer, sendingAccount);
            receivingAccount = businessLogic.addToReceivingAccount(transfer, receivingAccount);
            accountDao.updateAccount(sendingAccount);
            accountDao.updateAccount(receivingAccount);
            return transferDao.respondToTransferRequest(transfer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid recipient: " + principal.getName());
        }
    }


    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public List<TransferDTO> transferHistory(Principal principal,
                                             @RequestParam(required = false) String friendUsername) {

        List<TransferDTO> transferList = transferDao.viewTransferFromHistory(principal.getName(), friendUsername);
        transferList.addAll(transferDao.viewTransferToHistory(principal.getName(), friendUsername));

        return transferList;
    }

    @RequestMapping(value = "/history/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable Integer transferId) {
        return transferDao.getTransferById(transferId);
    }

    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public List<TransferDTO> viewPendingTransfers(Principal principal) {
        return transferDao.viewPendingTransfers(principal.getName());
    }
}
