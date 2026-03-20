package com.drivecheaper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DriveController {

    @FXML
    private Label welcomeText;

    @FXML
    private Label clickMeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onClickMeButtonClick(){ clickMeText.setText("Hallo World "); }
}



