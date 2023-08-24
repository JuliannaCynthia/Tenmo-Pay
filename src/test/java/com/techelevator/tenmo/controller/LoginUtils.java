package com.techelevator.tenmo.controller;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.model.User;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class LoginUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private LoginUtils() {
    }

    public static String getTokenForLogin(String username, String password, MockMvc mockMvc) throws Exception {
        String content = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
         AuthenticationResponse authenticationResponse = OBJECT_MAPPER.readValue(getToken(content), AuthenticationResponse.class);
        return authenticationResponse.getIdToken();
    }

    private static String getToken(String content){
        int index = content.indexOf(",\"user\"");
        String tokenString = content.substring(0,index) + "}";
        return tokenString;
    }
    private static class AuthenticationResponse {

        @JsonAlias("token")
        private String idToken;


        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        public String getIdToken() {
            return idToken;
        }
    }
}