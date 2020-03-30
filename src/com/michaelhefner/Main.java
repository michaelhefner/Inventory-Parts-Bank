/*************************************************************************
 Michael Hefner
 C482 - Software 1
 *************************************************************************/


package com.michaelhefner;

import com.michaelhefner.Model.InHouse;
import com.michaelhefner.Model.Inventory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("./View/MainPage.fxml"));
        primaryStage.setTitle("Inventory System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}