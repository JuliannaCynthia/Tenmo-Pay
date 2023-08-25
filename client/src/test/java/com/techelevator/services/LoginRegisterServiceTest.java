package com.techelevator.services;

import com.techelevator.model.LoginDTO;
import com.techelevator.model.RegisterUserDTO;
import com.techelevator.model.UserToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.Random;

public class LoginRegisterServiceTest {
    LoginRegisterService loginRegisterService;
    LoginDTO loginDTO;

    @Before
    public void setUp() {
        loginRegisterService = new LoginRegisterService();
        loginDTO = new LoginDTO();
        loginDTO.setUsername("bob");
        loginDTO.setPassword("password");
    }

    @Test
    public void login_returns_not_null() {
        UserToken userToken = loginRegisterService.login(loginDTO);
        Assert.assertNotNull(userToken);
    }

    @Test
    public void register_returns_created_status(){
        Random random = new Random();
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("USER" + random.nextInt(100000));
        registerUserDTO.setPassword("password");
        boolean isCreated = false;
        isCreated = loginRegisterService.register(registerUserDTO);
        Assert.assertTrue("User should return created(small chance a identical user already exists, make sure to double check)",isCreated);
    }


}