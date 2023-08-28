package com.techelevator.services;

import com.techelevator.model.Account;
import com.techelevator.model.LoginDTO;
import com.techelevator.model.RegisterUserDTO;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputService {
    private Scanner scanner;

    public InputService() {
        this.scanner = new Scanner(System.in);
    }


    public LoginDTO promptForCredentials() {
        LoginDTO user = new LoginDTO();
        user.setUsername(promptForUsername());
        user.setPassword(promptForPassword());
        return user;
    }

    public RegisterUserDTO promptForRegistration() {
        RegisterUserDTO user = new RegisterUserDTO();
        user.setUsername(promptForUsername());
        user.setPassword(promptForPassword());
        return user;
    }


    public int promptForMenuSelection() {
        int selection = -1;
        System.out.println("Please enter a selection below: \n\n");
        try {
            selection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: Please enter a number.");
        }
        return selection;
    }

    public String promptForUsername() {
        System.out.println("Please enter a Username below:\n\n");
        return scanner.nextLine();
    }

    public String promptForPassword() {
        System.out.println("Please enter a password below: \n\n");
        return scanner.nextLine();
    }

    public int promptForAccountId() {
        int accountId = 0;
        System.out.println("Please enter your Account ID:");
        while (true) {
            try {
                accountId = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Please enter a number.");
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
                System.out.println("Invalid input: Please enter a valid deposit in digits.");
            }
        }
        return amount;
    }

    public BigDecimal promptForWithdrawAmount() {
        System.out.println("How much would you like to withdraw?\n");
        BigDecimal amount = BigDecimal.ZERO;
        while (true) {
            try {
                String depositAmount = scanner.nextLine();
                amount = new BigDecimal(depositAmount);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: please enter a valid deposit in digits.");
            }
        }
        return amount;
    }

    public BigDecimal promptForTransferAmount() {
        System.out.println("Please enter an amount to transfer/request");
        BigDecimal transferAmount;
        while (true) {
            try {
                transferAmount = new BigDecimal(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: please enter a valid deposit in digits.");
            }
        }
        return transferAmount;
    }

    public int promptForTransferId() {
        System.out.println("Please enter a Transfer ID: ");
        int transferId = 0;
        while (true) {
            try {
                transferId = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
            }
        }
        return transferId;
    }

    public boolean promptForTransferApproval() {
        System.out.println("Approve transfer? (Y) or (N)");
        String userChoice = "";
        while (true) {
            userChoice = scanner.nextLine();
            if (userChoice.equalsIgnoreCase("Y") || userChoice.equalsIgnoreCase("N")) {
                break;
            } else {
                System.out.println("Invalid input, please enter (Y) or (N)");
            }
        }
        return userChoice.equalsIgnoreCase("Y");
    }
}
