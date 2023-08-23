package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/transfer")
@RestController
public class TransferController {
    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public Transfer createTransfer(@Valid @RequestBody Transfer transfer){
        return transferDao.createTransfer(transfer);
    }


    @RequestMapping(value = "/request", method = RequestMethod.PUT)
    public Integer respondToTransferRequest(@Valid @RequestBody Transfer transfer) {
        return transferDao.respondToTransferRequest(transfer);
    }


    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public List<Transfer> transferHistory(@Valid @RequestBody User user,
                                          @RequestParam (required = false) String friendUsername){

        int userId = user.getId();
        Integer friendUserId = userDao.findIdByUsername(friendUsername);

        List<Transfer> transferList = transferDao.viewTransferFromHistory(userId, friendUserId);
        transferList.addAll(transferDao.viewTransferToHistory(userId, friendUserId));

        return transferList;
    }

    @RequestMapping(value = "/history/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable Integer transferId){
        return transferDao.getTransferById(transferId);
    }

    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public List<Transfer> viewPendingTransfers(@Valid @RequestBody User user){
        return transferDao.viewPendingTransfers(user.getId());
    }
}
