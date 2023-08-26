package com.techelevator.services;

import com.techelevator.model.Account;
import com.techelevator.model.AccountDTO;

import java.util.List;

public class GraphicServices {


    public void openingGraphic(){
        System.out.println("########++++++++++++++++++++++++++++########");
        System.out.println("####                                    ####");
        System.out.println("###             WELCOME  TO              ###");
        System.out.println("###             TENMO - PAY              ###");
        System.out.println("####                                    ####");
        System.out.println("########++++++++++++++++++++++++++++########");
        System.out.println("~Where you are 'ten'-tamount to our success!~\n\n");
    }


    public void loginRegisterMenu(){
        System.out.println("########++++++++++++++++++++++++++++########");
        System.out.println("####                                    ####");
        System.out.println("###           #1  {~Log In~}             ###");
        System.out.println("###           #2 {~Register~}            ###");
        System.out.println("####                                    ####");
        System.out.println("########++++++++++++++++++++++++++++########\n\n");
    }
        //TODO: add an option (0) exit

    public void isRegistered(boolean isRegistered){
        if(isRegistered){
            System.out.println("Registration completed!");
        } else {
            System.out.println("Username already taken, please try again");
        }
    }

    public void displayUserMenu(){}

    public void displayAccountMenu() {

    }

    public void displayUserAccounts(List<AccountDTO> userAccounts) {
        System.out.println(userAccounts);
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
