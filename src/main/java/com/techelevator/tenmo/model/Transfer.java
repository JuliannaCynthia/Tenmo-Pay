package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Transfer {
    private int transferId;
    private BigDecimal transferAmount;
    private int transferFromUserId;
    private int transferToUserId;
    private Date dateCreated;
    private boolean isPending;
    private boolean isApproved;

    public Transfer(int transferId, BigDecimal transferAmount, int transferFromUserId, int transferToUserId, Date date_created, boolean isPending, boolean isApproved) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.transferFromUserId = transferFromUserId;
        this.transferToUserId = transferToUserId;
        this.dateCreated = date_created;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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
