package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;

public class FriendsDTO {
    @NotBlank
    private String userNameReceive;

    public FriendsDTO(String userNameReceive) {
        this.userNameReceive = userNameReceive;
    }

    public FriendsDTO() {
    }

    public String getUserNameReceive() {
        return userNameReceive;
    }

    public void setUserNameReceive(String userNameReceive) {
        this.userNameReceive = userNameReceive;
    }

    @Override
    public String toString() {
        return "{" + "'userName' : " + userNameReceive + '}';
    }
}
