package com.techelevator.services;


import com.techelevator.model.Logger;
import com.techelevator.model.Transfer;
import com.techelevator.model.TransferDTO;
import com.techelevator.model.UserToken;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransferService {
    private static File file = new File("Logs","log.txt");
    private static Logger log = new Logger(file);
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
            log.write(e.getMessage());
        }
        return transferDTO;
    }

    public boolean respondToTransfer(Transfer transfer) {
        HttpEntity<Transfer> transferHttpEntity = makeTransferEntity(transfer);
        boolean isSuccessful = false;
        try {
            restTemplate.put(TRANSFER_BASE_URL, transferHttpEntity);
            isSuccessful = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            log.write(e.getMessage());
        }
        return isSuccessful;
    }

    public List<TransferDTO> viewTransferHistory(String friendUsername) {
        TransferDTO[] transferHistory = null;
        String transferHistoryURL;
        if (!friendUsername.isBlank()) {
            transferHistoryURL = TRANSFER_BASE_URL + "/history" + "?friendUsername=" + friendUsername;
        } else {
            transferHistoryURL = TRANSFER_BASE_URL + "/history";
        }
        try {
            ResponseEntity<TransferDTO[]> response = restTemplate.exchange(transferHistoryURL, HttpMethod.GET, makeAuthEntity(), TransferDTO[].class);
            transferHistory = response.getBody();
            if (transferHistory == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend '" + friendUsername + "' not found");
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            log.write(e.getMessage());
        } catch (ResponseStatusException e){
            System.out.println(e.getStatus().getReasonPhrase() + "\n" + e.getMessage() );
            transferHistory = new TransferDTO[1];
        }
        return Arrays.stream(transferHistory).sorted(Comparator.comparing(TransferDTO::getTransferId)).collect(Collectors.toList());

    }

    public Transfer getTransferById(Transfer transfer) {
        Transfer transferInfo = null;
        HttpEntity<Transfer> transferHttpEntity = makeTransferEntity(transfer);
        try {
            transferInfo = restTemplate.postForObject(TRANSFER_BASE_URL + "/by_id", transferHttpEntity, Transfer.class);

        } catch (RestClientResponseException | ResourceAccessException e) {
            log.write(e.getMessage());
        }
        return transferInfo;
    }

    public List<TransferDTO> getPendingTransfers() {
        TransferDTO[] pendingTransfers = null;
        try {
            ResponseEntity<TransferDTO[]> response = restTemplate.exchange(TRANSFER_BASE_URL + "/pending", HttpMethod.GET, makeAuthEntity(), TransferDTO[].class);
            pendingTransfers = response.getBody();
            if(pendingTransfers == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No pending transfers found");
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            log.write(e.getMessage());
        } catch (ResponseStatusException e ){
            System.out.println(e.getStatus().getReasonPhrase() + "\n" + e.getMessage());
            pendingTransfers = new TransferDTO[1];
        }
        return Arrays.stream(pendingTransfers).sorted(Comparator.comparing(TransferDTO::getTransferId)).collect(Collectors.toList());
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
