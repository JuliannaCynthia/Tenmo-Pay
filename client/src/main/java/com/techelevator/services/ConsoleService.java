package com.techelevator.services;

import com.techelevator.model.LoginDTO;

import java.util.Scanner;

public class ConsoleService {

    private Scanner scanner;

    public ConsoleService(){
        this.scanner = new Scanner(System.in);
    }

    public String printMainMenu(){
        return null;
    }

    public LoginDTO promptForLogin(){
        LoginDTO user = new LoginDTO();

        return user;
    }

    public LoginDTO promptForRegister(){
        LoginDTO user = new LoginDTO();

        return user;
    }

    public String promptForMenuSelection(){
        return null;
    }


}
