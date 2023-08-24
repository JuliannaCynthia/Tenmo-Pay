package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.businesslogic.TransferBusinessLogic;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/transfer")
@RestController
public class TransferController {

    @Autowired
    private TransferDao transferDao;

    private final TransferBusinessLogic businessLogic = new TransferBusinessLogic();

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Transfer createTransfer(Principal principal, @Valid @RequestBody Transfer transfer) {

        if (!businessLogic.isToSameAccount(principal, transfer)) {
            return transferDao.createTransfer(transfer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid recipient: " +principal.getName());
        }
    }


    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Integer respondToTransferRequest(@Valid @RequestBody Transfer transfer) {
        return transferDao.respondToTransferRequest(transfer);
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
