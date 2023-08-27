package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class UserToken {

    @JsonAlias("token")
    private String token;

    @JsonAlias("user")
    private LoginDTO userInfo;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public LoginDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(LoginDTO userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
