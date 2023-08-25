package com.techelevator;

import com.techelevator.model.UserToken;
import com.techelevator.services.AccountsService;
import com.techelevator.services.InputService;
import com.techelevator.services.GraphicServices;
import com.techelevator.services.LoginRegisterService;

public class App {
    private InputService inputService;
    private GraphicServices graphicServices;
    private LoginRegisterService loginRegisterService;
    private AccountsService accountsService;

    private UserToken userToken;

    public App(){
        this.inputService = new InputService();
        this.graphicServices = new GraphicServices();
        this.loginRegisterService = new LoginRegisterService();
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        graphicServices.openingGraphic();

        int menuSelection = -1;
        while (menuSelection != 0){
            graphicServices.loginRegisterMenu();
            menuSelection = inputService.promptForMenuSelection();

        }
    }

}
