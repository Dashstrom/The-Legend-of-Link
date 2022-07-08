package fr.dashstrom.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {

    private Controller ctrl;

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void quit() {
        System.out.println("bouh vous avez perdu");
        System.exit(0);
    }

    public void setParent(Controller ctrl) {
        this.ctrl = ctrl;
    }

    @FXML
    public void replay() {
        this.ctrl.init();
    }

}
