package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class Transfer {
    @JsonProperty("transfer_id")
    private int transferId;
    @JsonProperty("transfer_amount")
    private BigDecimal transferAmount;
    @NotBlank
    @JsonProperty("user_transfer_from")
    private int transferFromUserId;
    @NotBlank
    @JsonProperty("user_transfer_to")
    private int transferToUserId;
    @JsonProperty("is_pending")
    private boolean isPending = true;
    @JsonProperty("is_approved")
    private boolean isApproved = false;

    public Transfer(){}

    public Transfer(int transferId, BigDecimal transferAmount, int transferFromUserId, int transferToUserId, boolean isPending, boolean isApproved) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.transferFromUserId = transferFromUserId;
        this.transferToUserId = transferToUserId;
        this.isPending = isPending;
        this.isApproved = isApproved;
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

    public int getTransferFromUserId() {
        return transferFromUserId;
    }

    public void setTransferFromUserId(int transferFromUserId) {
        this.transferFromUserId = transferFromUserId;
    }

    public int getTransferToUserId() {
        return transferToUserId;
    }

    public void setTransferToUserId(int transferToUserId) {
        this.transferToUserId = transferToUserId;
    }


    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
