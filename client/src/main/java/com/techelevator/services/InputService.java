package com.techelevator.services;

import com.techelevator.model.Account;
import com.techelevator.model.LoginDTO;
import com.techelevator.model.RegisterUserDTO;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputService {
    private Scanner scanner;

    public InputService(){
        this.scanner = new Scanner(System.in);
    }


    public LoginDTO promptForCredentials(){
        LoginDTO user = new LoginDTO();
        user.setUsername(promptForUsername());
        user.setPassword(promptForPassword());
        return user;
    }

    public RegisterUserDTO promptForRegistration(){
        RegisterUserDTO user = new RegisterUserDTO();
        user.setUsername(promptForUsername());
        user.setPassword(promptForPassword());
        return user;
    }


    public int promptForMenuSelection(){
        int selection = -1;
        System.out.println("Please enter a selection below: \n\n");
        try {
         selection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e){
            System.out.println("Invalid input: Please enter a number");
        }
        return selection;
    }

    public String promptForUsername(){
        System.out.println("Please enter a Username below:\n\n");
        return scanner.nextLine();
    }

    public String promptForPassword(){
        System.out.println("Please enter a password below: \n\n");
        return scanner.nextLine();
    }

    public int promptForAccountId(){
        int accountId = 0;
        System.out.println("Please enter your accountId");
        while(true) {
            try {
                accountId = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Please enter a number");
            }
        }
        return accountId;
    }

    public Account promptForAccount() {
        Account account = new Account();
        account.setAccountId(promptForAccountId());
        return account;
    }

    public BigDecimal promptForDepositAmount() {
        System.out.println("How much money would you like to deposit?\n");
        BigDecimal amount = BigDecimal.ZERO;
        while (true) {
            try {
                String depositAmount = scanner.nextLine();
                amount = new BigDecimal(depositAmount);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: please enter a valid deposit in digits");
            }
        }
        return amount;
    }

    public BigDecimal promptForWithdrawAmount() {
        System.out.println("How much would you like to withdraw?\n");
        BigDecimal amount = BigDecimal.ZERO;
        while(true){
            try {
                String depositAmount = scanner.nextLine();
                amount = new BigDecimal(depositAmount);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: please enter a valid deposit in digits");
            }
        }
        return amount;
    }

}
