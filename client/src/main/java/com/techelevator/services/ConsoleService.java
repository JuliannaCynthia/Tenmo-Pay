package com.techelevator.services;

import com.techelevator.model.LoginDTO;

import java.util.Scanner;

public class ConsoleService {
    GraphicServices graphicServices;
    private Scanner scanner;

    public ConsoleService(){
        this.scanner = new Scanner(System.in);
        this.graphicServices = new GraphicServices();
    }

    public String printMainMenu(){
       graphicServices.logRegMenu();
       return promptForMenuSelection();
    }



    public LoginDTO promptForCredentials(){
        LoginDTO user = new LoginDTO();
        user.setUsername(promptForUsername());
        user.setPassword(promptForPassword());
        return user;
    }


    public String promptForMenuSelection(){
        System.out.println("Please enter a selection below: \n\n");
        return scanner.nextLine();
    }

    public String promptForUsername(){
        System.out.println("Please enter a Username below:\n\n");
        return scanner.nextLine();
    }

    public String promptForPassword(){
        System.out.println("Please enter a password below: \n\n");
        return scanner.nextLine();
    }
}