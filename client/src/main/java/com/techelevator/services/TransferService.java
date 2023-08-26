package com.techelevator.services;


import com.techelevator.model.Transfer;
import com.techelevator.model.TransferDTO;
import com.techelevator.model.UserToken;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransferService {

    private final static String TRANSFER_BASE_URL = "http://localhost:8080/transfer";
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserToken userToken;
    private final String username;

    public TransferService(UserToken userToken, String username) {
        this.userToken = userToken;
        this.username = username;
    }

    public TransferDTO createTransfer(boolean isSending, BigDecimal transferAmount, String transferFrom, int userAccountNumber, String transferTo) {
        Transfer transfer = new Transfer();
        transfer.setTransferAmount(transferAmount);
        transfer.setTransferFromUsername(transferFrom);
        transfer.setTransferToUsername(transferTo);
        if (isSending) {
            transfer.setAccountNumberFrom(userAccountNumber);
        } else {
            transfer.setAccountNumberTo(userAccountNumber);
        }
        HttpEntity<Transfer> transferHttpEntity = makeTransferEntity(transfer);
        TransferDTO transferDTO = null;
        try {
            transferDTO = restTemplate.postForObject(TRANSFER_BASE_URL, transferHttpEntity, TransferDTO.class);

        } catch (RestClientResponseException | ResourceAccessException e) {
            //TODO: add a logger here. log(e.getMessage)
        }
        return transferDTO;
    }

    public boolean respondToTransfer(Transfer transfer){
        HttpEntity<Transfer> transferHttpEntity = makeTransferEntity(transfer);
        boolean isSuccessful = false;
        try {
            restTemplate.put(TRANSFER_BASE_URL, transferHttpEntity);
            isSuccessful = true;
        }catch (RestClientResponseException | ResourceAccessException e) {
            //TODO: add a logger here. log(e.getMessage)
        }
        return isSuccessful;
    }

    public List<TransferDTO> viewTransferHistory(String friendUsername){
        TransferDTO[] transferHistory = null;
        String transferHistoryURL;
        if(!friendUsername.isBlank()) {
            transferHistoryURL = TRANSFER_BASE_URL + "?friendUsername=" + friendUsername;
        } else {
            transferHistoryURL = TRANSFER_BASE_URL;
        }
        try {
            ResponseEntity<TransferDTO[]> response = restTemplate.exchange(transferHistoryURL, HttpMethod.GET, makeAuthEntity(), TransferDTO[].class);
            transferHistory = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            //TODO: add a logger here. log(e.getMessage)
        }
        return Arrays.stream(transferHistory).collect(Collectors.toList());
    }
    public TransferDTO getTransferById(Transfer transfer){
        TransferDTO transferInfo = null;
        try {
            transferInfo = restTemplate.postForObject(TRANSFER_BASE_URL + "by_id",  makeTransferEntity(transfer), TransferDTO.class);

        }catch (RestClientResponseException | ResourceAccessException e) {
            //TODO: add a logger here. log(e.getMessage)
        }
        return transferInfo;
    }

    public List<TransferDTO> getPendingTransfers(){
        TransferDTO[] pendingTransfers = null;
        try{
            ResponseEntity<TransferDTO[]> response = restTemplate.exchange(TRANSFER_BASE_URL + "/pending", HttpMethod.GET, makeAuthEntity(), TransferDTO[].class);
            pendingTransfers = response.getBody();

        }catch (RestClientResponseException | ResourceAccessException e) {
            //TODO: add a logger here. log(e.getMessage)
        }
        return Arrays.stream(pendingTransfers).collect(Collectors.toList());
    }



    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken.getToken());
        return new HttpEntity<>(headers);
    }
}
