package com.techelevator.menu;

import com.techelevator.model.Transfer;
import com.techelevator.model.TransferDTO;
import com.techelevator.model.UserToken;
import com.techelevator.services.GraphicServices;
import com.techelevator.services.InputService;
import com.techelevator.services.TransferService;

import java.math.BigDecimal;
import java.util.List;

public class TransferMenu {
    private final TransferService transferService;
    private List<TransferDTO> transferHistory;
    private final GraphicServices graphicServices;
    private final InputService inputService;
    private final String username;

    public TransferMenu(String username, UserToken userToken, GraphicServices graphicServices, InputService inputService) {
        this.transferService = new TransferService(userToken, username);
        this.graphicServices = graphicServices;
        this.inputService = inputService;
        this.username = username;
    }


    public void run() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            graphicServices.displayTransferMenu();
            menuSelection = inputService.promptForMenuSelection();

            //create new transfer: sending
            if (menuSelection == 1) {
                System.out.println("Who would you like send money to?");
                String friendUsername = inputService.promptForUsername();
                BigDecimal transferAmount = inputService.promptForTransferAmount();
                int transferAccount = inputService.promptForAccountId();

                TransferDTO createdTransfer =
                        transferService.createTransfer(true, transferAmount, username, transferAccount, friendUsername);
                graphicServices.displayTransfer(createdTransfer);
            }

            //create transfer request
            if (menuSelection == 2) {
                System.out.println("Who would you like to request money from");
                String friendUsername = inputService.promptForUsername();

                BigDecimal transferAmount = inputService.promptForTransferAmount();
                System.out.println("What account would you like to receive transfer?");
                int transferToAccount = inputService.promptForAccountId();

                TransferDTO createdTransfer =
                        transferService.createTransfer(false, transferAmount, friendUsername, transferToAccount, username);

                graphicServices.displayTransfer(createdTransfer);
            }

            //respond to transfer request
            if (menuSelection == 3) {
                int transferId = inputService.promptForTransferId();
                Transfer transfer = new Transfer();
                transfer.setTransferId(transferId);

                transfer = transferService.getTransferById(transfer);
                TransferDTO transferDTO = new TransferDTO(transfer.getTransferId(), transfer.getTransferAmount(), transfer.getTransferFromUsername(), transfer.getTransferToUsername());
                graphicServices.displayTransfer(transferDTO);

                boolean approved = inputService.promptForTransferApproval();
                if(approved){
                    System.out.println("What account would you like to transfer from?");
                    transfer.setAccountNumberFrom(inputService.promptForAccountId());
                }
                transfer.setApproved(approved);
                boolean isSuccessful = transferService.respondToTransfer(transfer);

                graphicServices.requestSuccessful(isSuccessful);

            }

            //view all previous transfers
            if (menuSelection == 4 ) {
                transferHistory = transferService.viewTransferHistory("");
                graphicServices.displayTransfers(transferHistory);

            }

            //view transfers by friend
            if (menuSelection == 5) {
                System.out.println("Please enter a friend's username");
                String friendUsername = inputService.promptForUsername();
                transferHistory = transferService.viewTransferHistory(friendUsername);
                graphicServices.displayTransfers(transferHistory);
            }

            //view pending transfers
            if(menuSelection == 6) {
                List<TransferDTO> pendingTransfers = transferService.getPendingTransfers();
                graphicServices.displayTransfers(pendingTransfers);
            }
        }

    }
}
