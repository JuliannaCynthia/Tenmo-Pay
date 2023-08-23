package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    //POST /transfer/request/
    Transfer createTransfer(Transfer transfer);

    //PUT /transfer/request/
    Integer respondToTransferRequest(Transfer transfer);

    //GET /transfer/history
    List<Transfer> viewTransferFromHistory(int userId, Integer friendUserId);
    List<Transfer> viewTransferToHistory(int userId, Integer friendUserId);

    //GET /transfer/history/id
    Transfer getTransferById(Integer transferId);

    //GET transfer/pending
    List<Transfer> viewPendingTransfers(Integer userId);





}
