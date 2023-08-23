package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    //POST /transfer/request/
    Transfer createTransfer(Transfer transfer);

    //PUT /transfer/request/
    Transfer approveTransfer(Transfer transfer);

    //GET /transfer/history
    List<Transfer> viewTransferHistory();

    //GET /transfer/history/id
    Transfer getTransferById(Integer transferId);

    //GET /transfer/history/friendUserName
    List<Transfer> viewTransfersByFriendUserName(String friendUsername);

    //GET transfer/pending
    List<Transfer> viewPendingTransfers();





}
