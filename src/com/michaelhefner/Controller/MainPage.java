/*************************************************************************
 Michael Hefner
 C482 - Software 1
 *************************************************************************/

package com.michaelhefner.Controller;

import com.michaelhefner.Model.InHouse;
import com.michaelhefner.Model.Inventory;
import com.michaelhefner.Model.Outsourced;
import com.michaelhefner.Model.Part;
import javafx.application.Platform;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainPage implements Initializable {
    private boolean partSelectedIsInhouse = true;
    private int partSelected = -1;
    @FXML
    private TableView<Part> tblParts;

    @FXML
    private TableColumn<Part, String> clmPartID;

    @FXML
    private TableColumn<Part, String> clmPartName;

    @FXML
    private TableColumn<Part, String> clmInvLevel;

    @FXML
    private TableColumn<Part, String> clmPrice;

    @FXML
    private TextField txtSearchPart;

    @FXML
    public void handleSearchPart(ActionEvent actionEvent) {
        txtSearchPart.setText("hello!");
    }

    @FXML
    public void openAddProduct(ActionEvent actionEvent) throws IOException {
        Parent addProduct = FXMLLoader.load(getClass().getResource("../View/AddProduct.fxml"));
        Stage addProductStage = new Stage();
        addProductStage.setScene(new Scene(addProduct));
        addProductStage.show();
    }

    @FXML
    public void openAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/InHousePart.fxml"));
        Stage inHousePartStage = new Stage();
        inHousePartStage.setScene(new Scene(root));
        inHousePartStage.show();
    }

    @FXML
    private void onModifyClicked(ActionEvent actionEvent) throws IOException {

        if (partSelectedIsInhouse) {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("../View/InHousePart.fxml"));
            FlowPane root = loader.load();
            InHousePart inHousePart = loader.getController();
            inHousePart.isModify(partSelected);
            Stage inHousePartStage = new Stage();
            inHousePartStage.setScene(new Scene(root));
            inHousePartStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("../View/OutsourcedPart.fxml"));
            FlowPane root = loader.load();
            OutsourcedPart outsourcedPart = loader.getController();
            outsourcedPart.isModify(partSelected);
            Stage outsourcePartStage = new Stage();
            outsourcePartStage.setScene(new Scene(root));
            outsourcePartStage.show();
        }
    }

    @FXML
    public void onExit(ActionEvent actionEvent) {
        try {
            System.out.println("end");
            Platform.exit();
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void onDeletePart(ActionEvent actionEvent) {
        if (partSelected != -1) {
            Part part = Inventory.lookupPart(partSelected);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("You are about to delete " + part.getName());
            alert.setContentText("Are you sure you would like to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Inventory.deletePart(Inventory.lookupPart(partSelected));
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /***************
         * Start Demo Data
         */
        InHouse part = new InHouse(0, "testing", 1.0, 1, 0, 10, 12);
        InHouse part2 = new InHouse(1, "one", 1.0, 1, 0, 10, 1234);
        Outsourced part3 = new Outsourced(2, "outsourced", 1.0, 1, 0, 10, "comp");

        Inventory.addPart(part);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        /*******
         * End Demo Data
         *
         */

        clmPartID.setCellValueFactory(new PropertyValueFactory<Part, String>("id"));
        clmPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        clmPrice.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        clmInvLevel.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));

        tblParts.setItems(Inventory.getAIIParts());


        tblParts.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                try {
                    partSelectedIsInhouse = (Inventory.lookupPart(t1.intValue()).getClass() == InHouse.class);
                    partSelected = t1.intValue();
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
            }
        });

    }
}
