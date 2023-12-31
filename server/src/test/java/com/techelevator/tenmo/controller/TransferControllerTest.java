package com.techelevator.tenmo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.techelevator.tenmo.model.RegisterUserDTO;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.techelevator.tenmo.controller.LoginUtils.getTokenForLogin;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransferControllerTest {
//    @Autowired
//    TransferController transferController;
//    @Autowired
//    AuthenticationController authenticationController;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    private String userToken;
    private String bobToken;

    @Before
    public void setUp() throws Exception {
        System.out.println("setup()...");
//        SecurityContextHolder.setContext();
//        mockMvc = MockMvcBuilders.standaloneSetup(transferController, authenticationController).build();

    }
    @Before
    public void createUsers() throws Exception {
        userToken = getTokenForLogin("user", "password", mockMvc);
        bobToken = getTokenForLogin("bob", "password", mockMvc);
    }

    @Test
    public void createTransfer_should_return_with_valid_transfer() throws Exception{
        Transfer newTransfer = new Transfer();
        newTransfer.setTransferFromUsername("bob");
        newTransfer.setTransferToUsername("user");
        newTransfer.setTransferAmount(new BigDecimal("100.00"));


        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bobToken)
                .content(transferToJson(newTransfer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transferId").value(3003))
                .andExpect(jsonPath("$.isPending").value(true))
                .andExpect(jsonPath("$.isApproved").value(false));


    }

    @Test
    public void respondToTransferRequest_should_return_transfer_pending_equals_false_and_approved_true() throws Exception {
        Transfer transfer = new Transfer(3002, new BigDecimal("100.00"), "bob", 1001, "user", false, true );
        transfer.setAccountNumberTo(2002);

        mockMvc.perform(put("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bobToken)
                .content(transferToJson(transfer)))
                .andExpect(status().isOk());

    }

    @Test
    public void transferHistory() throws Exception {
        mockMvc.perform(get("/transfer/history")
                .header("Authorization", "Bearer " + bobToken))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getTransferById() throws Exception {
        mockMvc.perform(get("/transfer/history/3001")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bobToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transferId").value(3001));


    }

    @Test
    public void viewPendingTransfers() {
    }

    private String userToJson(RegisterUserDTO userDTO) throws JsonProcessingException{
        return mapper.writeValueAsString(userDTO);
    }
    private String transferToJson(Transfer transfer) throws JsonProcessingException {
        return mapper.writeValueAsString(transfer);
    }
}