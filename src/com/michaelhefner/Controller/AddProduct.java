/*************************************************************************
Michael Hefner
 C482 - Software 1
 *************************************************************************/

package com.michaelhefner.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProduct implements Initializable {

    @FXML
    private Label label1;
    @FXML
    public void handleSearchPart(ActionEvent actionEvent) {
        label1.setText("hello!");
    }

    @FXML
    public void onCancelPressed(ActionEvent actionEvent) {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
