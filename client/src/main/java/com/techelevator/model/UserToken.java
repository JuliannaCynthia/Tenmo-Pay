package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class UserToken {

    @JsonAlias("token")
    private String token;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    @Override
    public String toString() {
        return "UserToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
