package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;

public class Friends {
    @NotBlank
    private String userNameRequest;
    @NotBlank
    private String userNameReceived;

    private boolean approved;

    public Friends() {
    }

    public Friends(String userNameRequest, String userNameReceived, boolean approved) {
        this.userNameRequest = userNameRequest;
        this.userNameReceived = userNameReceived;
        this.approved = approved;
    }

    public String getUserNameRequest() {
        return userNameRequest;
    }

    public void setUserNameRequest(String userNameRequest) {
        this.userNameRequest = userNameRequest;
    }

    public String getUserNameReceived() {
        return userNameReceived;
    }

    public void setUserNameReceived(String userNameReceived) {
        this.userNameReceived = userNameReceived;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Friends{" + userNameRequest + " & "+ userNameReceived +"}";
    }
}
