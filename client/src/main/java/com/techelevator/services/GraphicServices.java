package com.techelevator.services;

import com.techelevator.model.Account;
import com.techelevator.model.AccountDTO;
import com.techelevator.model.FriendsDTO;
import com.techelevator.model.TransferDTO;

import java.util.List;

public class GraphicServices {


    public void openingGraphic(){
        System.out.println("\n      ########++++++++++++++++++++++++++++########");
        System.out.println("      ####                                    ####");
        System.out.println("      ###             WELCOME  TO              ###");
        System.out.println("      ###             TENMO - PAY              ###");
        System.out.println("      ####                                    ####");
        System.out.println("      ########++++++++++++++++++++++++++++########");
        System.out.println("      ~Where you are 'ten'-tamount to our success!~\n\n");
    }


    public void loginRegisterMenu(){
        System.out.println("\n      ########++++++++++++++++++++++++++++########");
        System.out.println("      ####                                    ####");
        System.out.println("      ###           #1  {~Log In~}             ###");
        System.out.println("      ###           #2 {~Register~}            ###");
        System.out.println("      ###           #0   {~Exit~}              ###");
        System.out.println("      ####                                    ####");
        System.out.println("      ########++++++++++++++++++++++++++++########\n\n");
    }


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
        System.out.println("      ###           *1 {~Accounts~}            ###");
        System.out.println("      ###           *2 {~Friends~}             ###");
        System.out.println("      ###           *3 {~Transfers~}           ###");
        System.out.println("      ####                                    ####");
        System.out.println("      ########++++++++++++++++++++++++++++########");
    }

    public void displayAccountMenu() {
        System.out.println("\n      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
        System.out.println("      #####                                   #####");
        System.out.println("      ####        *1 {~View All Accounts~}     ####"); //todo: make only user accounts able to be viewed
        System.out.println("      ###         *2 {~View An Account~}        ###");
        System.out.println("      ###         *3 {~Create A New Account~}   ###");
        System.out.println("      ###         *4 {~Delete An Account~}      ###");
        System.out.println("      ###         *5 {~New Deposit~}            ###");
        System.out.println("      ###         *6 {~New Withdrawal~}         ###");
        System.out.println("      ####        *0 {~Exit~}                  ####");
        System.out.println("      #####                                   #####");
        System.out.println("      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
    }


    public void displayUserAccounts(List<AccountDTO> userAccounts) {
        int counter = 1;
        System.out.println("\n      ########++++++++++++++++++++++++++++########");
        System.out.println("      #####                                  #####");
        for(AccountDTO dto:userAccounts){
            System.out.println("      ####          {~Account #" + counter +"~}            ####");
            System.out.println("      ####          {~Balance $" + dto.getAccountBalance() + "~}         ####");
            if(userAccounts.size()>1) {
                System.out.println("      ####                                    ####");
            }
            counter++;
        }
        System.out.println("      #####                                  #####");
        System.out.println("      ########++++++++++++++++++++++++++++########");
    }

    public void displayAccount(Account account) {
        System.out.println("\n      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
        System.out.println("      #####                                   #####");
        System.out.println("      ####           {~Account #" + account.getAccountId() +"~}            ####");
        System.out.println("      ####           {~Balance $" + account.getBalance() +"~}            ####");
        System.out.println("      #####                                   #####");
        System.out.println("      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
    }

    public void displayAccount(AccountDTO account) {
        System.out.println("\n      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
        System.out.println("      #####                                   #####");
        System.out.println("      ####           {~Username: " + account.getUsername() +"~}            ####");
        System.out.println("      ####           {~Balance $" + account.getAccountBalance() +"~}            ####");
        System.out.println("      #####                                   #####");
        System.out.println("      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
    }

    public void deleteMessage(boolean deleteSuccessful) {
        if(deleteSuccessful){
            System.out.println("                 ####-------------####");
            System.out.println("                 #X-----Account-----X#");
            System.out.println("                 #X-----Deleted-----X#");
            System.out.println("                 ####-------------####");
        } else {
            System.out.println("                 X_X_X_X_X_X_X_X_X_X_X");
            System.out.println("                 X_X    Account    X_X");
            System.out.println("                 X_X    Deletion   X_X");
            System.out.println("                 X_X    Failure    X_X");
            System.out.println("                 X_X_X_X_X_X_X_X_X_X_X");
        }
    }

    public void displayFriendsMenu() {
        System.out.println("\n      ####### |><|><|><|><|><|><|><|><|><| ########");
        System.out.println("      #####                                   #####");
        System.out.println("      ####        *1 {~View Friends~}          ####");
        System.out.println("      ###         *2 {~Add A Friend~}           ###");
        System.out.println("      ###         *3 {~View Pending Friends~}   ###");
        System.out.println("      ###         *4 {~Approve Friend~}         ###");
        System.out.println("      ###         *5 {~Deny Friend~}            ###");
        System.out.println("      ###         *6 {~Remove Friend~}          ###");
        System.out.println("      ####        *0 {~Exit~}                  ####");
        System.out.println("      #####                                   #####");
        System.out.println("      ####### |><|><|><|><|><|><|><|><|><| ########");
    }

    public void displayFriends(List<FriendsDTO> friendsList) {
        int counter = 1;
        System.out.println("\n      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~#######");
        System.out.println("      #####                                  #####");
        for(FriendsDTO dto:friendsList){
            System.out.println("                    {~Friend   #" + counter +"~}");
            System.out.println("                    {~UserName >" + dto.getUsername()+ "~}");
            if(friendsList.size()>1) {
                System.out.println("      ####                                    ####");
            }
            counter++;
        }
        System.out.println("      #####                                   #####");
        System.out.println("      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
    }

    public void displayTransferMenu() {
        System.out.println("\n      ########=+--==--++--==--++--==--++-=########");
        System.out.println("      #####                                  #####");
        System.out.println("      ####       *1 {~Send a transfer~}          ####");
        System.out.println("      ###        *2 {~Request a transfer~}           ###");
        System.out.println("      ###        *3 {~Respond to pending transfer~}   ###");
        System.out.println("      ###        *4 {~View transfer history~}         ###");
        System.out.println("      ###        *5 {~Search transfers by friend~}            ###");
        System.out.println("      ###        *6 {~View pending transfers~}          ###");
        System.out.println("      ####       *0 {~Exit~}                  ####");
        System.out.println("      #####                                  #####");
        System.out.println("      ########=+--==--++--==--++--==--++-=########");
    }

    public void displayTransfer(TransferDTO createdTransfer) {
        System.out.println("\n      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
        System.out.println("      #####                                   #####");
        System.out.println("                    {~Transfer #" + createdTransfer.getTransferId() +"~}");
        System.out.println("                    {~Amount $" + createdTransfer.getTransferAmount() +"~}");
        System.out.println("                    {~From #" + createdTransfer.getTransferFromUsername() +"~}");
        System.out.println("                    {~To $" + createdTransfer.getTransferToUsername() +"~}");
        System.out.println("      #####                                   #####");
        System.out.println("      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
    }

    public void requestSuccessful(boolean isSuccessful) {
        if(isSuccessful){
            System.out.println("                 ####--------------####");
            System.out.println("                 #><---Successful---><#");
            System.out.println("                 #><----Transfer----><#");
            System.out.println("                 ####--------------####");
        } else {
            System.out.println("                 X_X_X_X_X_X_X_X_X_X_X");
            System.out.println("                 X_X    Transfer   X_X");
            System.out.println("                 X_X    Failure    X_X");
            System.out.println("                 X_X_X_X_X_X_X_X_X_X_X");
        }
    }

    public void displayTransfers(List<TransferDTO> transferHistory) {
        int counter = 0;
        System.out.println("\n      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
        System.out.println("      #####                                   #####");
        for(TransferDTO dto:transferHistory){
            System.out.println("                    {~Archive #" + counter +"~}");
            System.out.println("                    {~Transfer #" + dto.getTransferId() +"~}");
            System.out.println("                    {~Amount $" + dto.getTransferAmount() +"~}");
            System.out.println("                    {~From #" + dto.getTransferFromUsername() +"~}");
            System.out.println("                    {~To $" + dto.getTransferToUsername() +"~}");
            if(transferHistory.size()>1) {
                System.out.println("      ####                                    ####");
            }
            counter++;
        }
        System.out.println("      #####                                   #####");
        System.out.println("      ########~*~*~*~*~*~*~*~*~*~*~*~*~*~*~########");
    }
}
