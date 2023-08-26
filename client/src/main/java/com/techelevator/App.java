package com.techelevator;

import com.techelevator.menu.AccountMenu;
import com.techelevator.menu.FriendsMenu;
import com.techelevator.menu.TransferMenu;
import com.techelevator.model.LoginDTO;
import com.techelevator.model.RegisterUserDTO;
import com.techelevator.model.UserToken;
import com.techelevator.services.AccountsService;
import com.techelevator.services.InputService;
import com.techelevator.services.GraphicServices;
import com.techelevator.services.LoginRegisterService;

public class App {
    private InputService inputService;
    private GraphicServices graphicServices;
    private LoginRegisterService loginRegisterService;


    private UserToken userToken;


    public App() {
        this.inputService = new InputService();
        this.graphicServices = new GraphicServices();
        this.loginRegisterService = new LoginRegisterService();
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        boolean isRegistered;
        graphicServices.openingGraphic();

        int menuSelection = -1;
        while (menuSelection != 0) {
            menuSelection = loginRegisterMenu();
            if(menuSelection == 1){
                LoginDTO user = inputService.promptForCredentials();
                userToken = loginRegisterService.login(user);


            }
            if(menuSelection == 2){
                RegisterUserDTO user = inputService.promptForRegistration();
                isRegistered = loginRegisterService.register(user);
                graphicServices.isRegistered(isRegistered);
            }
        }
    }

    private int loginRegisterMenu() {
        int menuSelection = -1;
        graphicServices.loginRegisterMenu();
        menuSelection = inputService.promptForMenuSelection();
        if (menuSelection < 0 || menuSelection > 2) {
            System.out.println("Invalid input");
            return -1;
        }else {
            return menuSelection;
        }
    }

    private void userMenus() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            graphicServices.displayUserMenu();
            menuSelection = inputService.promptForMenuSelection();

            if(menuSelection == 1){
                AccountMenu accountMenu = new AccountMenu(userToken);
                accountMenu.run();
            }
            if(menuSelection == 2){
                FriendsMenu friendsMenu = new FriendsMenu(userToken);
                friendsMenu.run();
            }
            if(menuSelection == 3) {
                TransferMenu transferMenu = new TransferMenu(userToken);
                transferMenu.run();
            }


        }
    }

}
