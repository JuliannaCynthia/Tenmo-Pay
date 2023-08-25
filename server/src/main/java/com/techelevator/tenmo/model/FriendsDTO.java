package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;

public class FriendsDTO {
    @NotBlank
    private String username;

    public FriendsDTO(String username) {
        this.username = username;
    }

    public FriendsDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "{" + "'userName' : " + username + '}';
    }
}
