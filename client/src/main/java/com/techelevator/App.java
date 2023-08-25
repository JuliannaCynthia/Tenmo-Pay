package com.techelevator;

import com.techelevator.services.inputService;
import com.techelevator.services.GraphicServices;
import com.techelevator.services.TenmoService;

public class App {
    private inputService inputService;
    private GraphicServices graphicServices;
    private TenmoService tenmoService;

    public App(){
        this.inputService = new inputService();
        this.graphicServices = new GraphicServices();
        this.tenmoService = new TenmoService();
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
            inputService.promptForMenuSelection();

        }
    }

}
