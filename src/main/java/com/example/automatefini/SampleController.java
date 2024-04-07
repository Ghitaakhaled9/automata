package com.example.automatefini;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SampleController {

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btn1) {
            System.out.println("Button 1 clicked");
            // Add your logic for Button 1 here
        } else if (event.getSource() == btn2) {
            System.out.println("Button 2 clicked");
            // Add your logic for Button 2 here
        } else if (event.getSource() == btn3) {
            System.out.println("Button 3 clicked");
            // Add your logic for Button 3 here
        }
    }
}
