package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/transfer")
@RestController
public class TransferController {
    private TransferDao transferDao;


    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Transfer createTransfer(@Valid @RequestBody Transfer transfer){
        return transferDao.createTransfer(transfer);
    }


    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Integer respondToTransferRequest(@Valid @RequestBody Transfer transfer) {
        return transferDao.respondToTransferRequest(transfer);
    }


    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public List<TransferDTO> transferHistory(Principal principal,
                                          @RequestParam (required = false) String friendUsername){

        String username = principal.getName();

        List<TransferDTO> transferList = transferDao.viewTransferFromHistory(username, friendUsername);
        transferList.addAll(transferDao.viewTransferToHistory(username, friendUsername));

        return transferList;
    }

    @RequestMapping(value = "/history/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable Integer transferId){
        return transferDao.getTransferById(transferId);
    }

    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public List<TransferDTO> viewPendingTransfers(Principal principal){
        return transferDao.viewPendingTransfers(principal.getName());
    }
}
