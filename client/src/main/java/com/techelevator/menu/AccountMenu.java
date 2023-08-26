package com.techelevator.menu;

import com.techelevator.model.Account;
import com.techelevator.model.AccountDTO;
import com.techelevator.model.UserToken;
import com.techelevator.services.AccountsService;
import com.techelevator.services.GraphicServices;
import com.techelevator.services.InputService;

import java.math.BigDecimal;
import java.util.List;

public class AccountMenu {

    private final AccountsService accountsService;
    private final List<AccountDTO> userAccounts;
    private final GraphicServices graphicServices;
    private final InputService inputService;

    public AccountMenu(UserToken userToken, GraphicServices graphicServices, InputService inputService) {
        this.graphicServices = graphicServices;
        this.inputService = inputService;
        accountsService = new AccountsService(userToken);
        userAccounts = accountsService.getUserAccounts();

    }
    public void run(){

        int menuSelection = -1;
        while (menuSelection != 0){
            graphicServices.displayAccountMenu();

             menuSelection = inputService.promptForMenuSelection();

             //display all accounts
            if(menuSelection == 1 ){
                graphicServices.displayUserAccounts(userAccounts);
            }

            //display account info
            if (menuSelection == 2) {
                Account account = inputService.promptForAccount();
                account = accountsService.getAccount(account);
                graphicServices.displayAccount(account);
            }

            //create new account
            if(menuSelection == 3){
                Account account = accountsService.createAccount();
                graphicServices.displayAccount(account);
            }

            //delete account
            if(menuSelection == 4){
                Account account = inputService.promptForAccount();

                boolean deleteSuccessful = accountsService.deleteAccount(account);
                graphicServices.deleteMessage(deleteSuccessful);
            }

            //deposit to account
            if(menuSelection == 5){
                Account account = inputService.promptForAccount();
                account = accountsService.getAccount(account);
                graphicServices.displayAccount(account);

                BigDecimal moneyToDeposit = inputService.promptForDepositAmount();
                account.setBalance(account.getBalance().add(moneyToDeposit));
                account = accountsService.updateAccount(account);

                graphicServices.displayAccount(account);

            }
            //withdraw from account
            if(menuSelection == 6){
                Account account = inputService.promptForAccount();
                account = accountsService.getAccount(account);
                graphicServices.displayAccount(account);

                BigDecimal moneyToWithdraw = inputService.promptForWithdrawAmount();
                if(moneyToWithdraw.compareTo(account.getBalance()) < 1){
                    account.setBalance(account.getBalance().subtract(moneyToWithdraw));
                    account = accountsService.updateAccount(account);
                    graphicServices.displayAccount(account);
                } else {
                    System.out.println("Cannot withdraw more than account balance");
                }

            }
        }
    }
}
