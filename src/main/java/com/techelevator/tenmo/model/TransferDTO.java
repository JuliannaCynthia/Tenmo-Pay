package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class TransferDTO {
    @NotBlank
    private int transferId;
    @Min(value = 0)
    private BigDecimal transferAmount;
    @NotBlank
    private String transferFromUsername;
    @NotBlank
    private String transferToUsername;

    public TransferDTO(int transferId, BigDecimal transferAmount, String transferFromUsername, String transferToUsername){
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.transferFromUsername = transferFromUsername;
        this.transferToUsername = transferToUsername;
    }

    public TransferDTO() {
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferFromUsername() {
        return transferFromUsername;
    }

    public void setTransferFromUsername(String transferFromUsername) {
        this.transferFromUsername = transferFromUsername;
    }

    public String getTransferToUsername() {
        return transferToUsername;
    }

    public void setTransferToUsername(String transferToUsername) {
        this.transferToUsername = transferToUsername;
    }

    @Override
    public String toString() {
        String string = String.format("{ \"transferId\" : %s,\n\"transferAmount\" : %s,\n\"from\" : %s,\n\"to\" : %s }",
                transferId, transferAmount, transferFromUsername, transferToUsername);
        return string;
    }
}
