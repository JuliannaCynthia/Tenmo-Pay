package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;

import java.util.List;

public interface TransferDao {

    //POST /transfer/request/
    Transfer createTransfer(Transfer transfer);

    //PUT /transfer/request/
    Integer respondToTransferRequest(Transfer transfer);

    //GET /transfer/history
    List<TransferDTO> viewTransferFromHistory(String transferFromUsername, String transferToUsername);
    List<TransferDTO> viewTransferToHistory(String transferToUsername, String transferFromUsername);

    //GET /transfer/history/id
    Transfer getTransferById(Integer transferId);

    //GET transfer/pending
    List<TransferDTO> viewPendingTransfers(String username);

    boolean transferCredentialsAreNotFriends(Transfer transfer);





}
