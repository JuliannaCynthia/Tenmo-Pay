package com.techelevator.services;

import com.techelevator.model.Account;
import com.techelevator.model.AccountDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GraphicsSoNoMergeIssues {


/*
* for account testing, youll need a constructor in the dto model. I'll finish the other ones soon!
* I'm going to do this part for most of the fields, whether we user them or not.
*
* */
    public static void main(String[] args) {
        BigDecimal dec = new BigDecimal("1000");
//        AccountDTO dto = new AccountDTO("bob",dec);
//        AccountDTO ditto = new AccountDTO("monica", dec);
        List<AccountDTO> dtoList = new ArrayList<>();
//        dtoList.add(ditto);
//        dtoList.add(dto);
        displayUserAccounts(dtoList);
    }

    public void loginRegisterMenu(){
        System.out.println("\n      ########++++++++++++++++++++++++++++########");
        System.out.println("      ####                                    ####");
        System.out.println("      ###           *0 {~Exit~}                ###");
        System.out.println("      ###           *1 {~Log In~}              ###");
        System.out.println("      ###           *2 {~Register~}            ###");
        System.out.println("      ####                                    ####");
        System.out.println("      ########++++++++++++++++++++++++++++########\n");
    };
    //TODO: add an option (0) exit

    public void isRegistered(boolean isRegistered){
        if(isRegistered){
            System.out.println("                 ####------------####");
            System.out.println("                 ##--Registration--##");
            System.out.println("                 ##----Complete----##");
            System.out.println("                 ####------------####");

        } else {
            System.out.println("                 X_X_X_X_X_X_X_X_X_X_X");
            System.out.println("                 X_X   Username    X_X");
            System.out.println("                 X_X Not Available X_X");
            System.out.println("                 X_X   Try Again   X_X");
            System.out.println("                 X_X_X_X_X_X_X_X_X_X_X");
        }
    }

    public void displayUserMenu(){
        System.out.println("\n      ########++++++++++++++++++++++++++++########");
        System.out.println("      ####                                    ####");
        System.out.println("      ###           *0 {~Exit~}                ###");
        System.out.println("      ###           *1 {~Friends~}             ###");
        System.out.println("      ###           *2 {~Accounts~}            ###");
        System.out.println("      ###           *3 {~Transfers~}           ###");
        System.out.println("      ####                                    ####");
        System.out.println("      ########++++++++++++++++++++++++++++########");
    }


    public void displayAccountMenu() {
        System.out.println("\n      ########++++++++++++++++++++++++++++########");
        System.out.println("      #####                                  #####");
        System.out.println("      ####       *0 {~Exit~}                  ####");
        System.out.println("      ###        *1 {~View All Accounts~}      ###");
        System.out.println("      ###        *2 {~View Specific Account~}  ###");
        System.out.println("      ###        *3 {~Create An Account~}      ###");
        System.out.println("      ####       *4 {~Delete An Account~}     ####");
        System.out.println("      #####                                  #####");
        System.out.println("      ########++++++++++++++++++++++++++++########");
    }


    public void displayFriendsMenu() {
        System.out.println("\n      ########++++++++++++++++++++++++++++########");
        System.out.println("      #####                                  #####");
        System.out.println("      ####       *0 {~Exit~}                  ####");
        System.out.println("      ###        *1 {~View Friends~}           ###");
        System.out.println("      ###        *2 {~View Pending Friends~}   ###");
        System.out.println("      ###        *3 {~Approve/Deny Friend~}    ###");
        System.out.println("      ####       *4 {~Remove Friend~}         ####");
        System.out.println("      #####                                  #####");
        System.out.println("      ########++++++++++++++++++++++++++++########");
    }




    public static void displayUserAccounts(List<AccountDTO> userAccounts) {
        int counter = 1;
        System.out.println("\n      ########++++++++++++++++++++++++++++########");
        System.out.println("      #####                                  #####");
        for(AccountDTO dto:userAccounts){
            System.out.println("      ####       *0 {~Account #" + counter +"~}            ####");
            System.out.println("      ####       *0 {~Balance $" + dto.getAccountBalance() + "~}         ####");
            if(userAccounts.size()>1) {
                System.out.println("      ####                                    ####");
            }
            counter++;
        }
        System.out.println("      #####                                  #####");
        System.out.println("      ########++++++++++++++++++++++++++++########");
    }

    public void displayAccount(Account account) {
    }

    public void deleteMessage(boolean deleteSuccessful) {
        if(deleteSuccessful){
            System.out.println("Account deleted");
        } else {
            System.out.println("Account deletion failed");
        }
    }



}
