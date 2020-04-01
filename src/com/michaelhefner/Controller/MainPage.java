/*
 * Michael Hefner
 * C482 - Software 1
 */

package com.michaelhefner.Controller;

import com.michaelhefner.Model.*;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainPage implements Initializable {
    private boolean partSelectedIsInHouse = true;
    private Part partSelected;
    private Product productSelected;
    private FilteredList<Part> filteredList;
    private FilteredList<Product> productFilteredList;
    private Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

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
    private TableView<Product> tblProduct;

    @FXML
    private TableColumn<Product, String> clmProductID;

    @FXML
    private TableColumn<Product, String> clmProductName;

    @FXML
    private TableColumn<Product, String> clmProductInvLevel;

    @FXML
    private TableColumn<Product, String> clmProductPrice;

    @FXML
    private TextField txtSearchPart;

    @FXML
    private TextField txtSearchProduct;

    @FXML
    public void handleSearchPart() {
        filteredList.setPredicate(part -> {
            if (txtSearchPart.getText().isEmpty())
                return true;
            return (part.getName().equals(txtSearchPart.getText()));
        });
    }

    @FXML
    public void handleSearchProduct() {
        productFilteredList.setPredicate(product -> {
            if (txtSearchProduct.getText().isEmpty())
                return true;
            return (product.getName().equals(txtSearchProduct.getText()));
        });
    }

    @FXML
    public void openAddProduct() throws IOException {
        Parent addProduct = FXMLLoader.load(getClass().getResource("../View/AddProduct.fxml"));
        Stage addProductStage = new Stage();
        addProductStage.setScene(new Scene(addProduct));
        addProductStage.show();
    }

    @FXML
    public void openAddPart() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/InHousePart.fxml"));
        Stage inHousePartStage = new Stage();
        inHousePartStage.setScene(new Scene(root));
        inHousePartStage.show();
    }

    @FXML
    private void onModifyPartClicked() throws IOException {
        if (partSelected != null) {
            FXMLLoader loader = new FXMLLoader();

            if (partSelectedIsInHouse) {
                loader.setLocation(getClass().getResource("../View/InHousePart.fxml"));
                FlowPane root = loader.load();
                InHousePart inHousePart = loader.getController();
                inHousePart.isModify(partSelected);
                Stage inHousePartStage = new Stage();
                inHousePartStage.setScene(new Scene(root));
                inHousePartStage.show();
            } else {
                loader.setLocation(getClass().getResource("../View/OutsourcedPart.fxml"));
                FlowPane root = loader.load();
                OutsourcedPart outsourcedPart = loader.getController();
                outsourcedPart.isModify(partSelected);
                Stage outsourcePartStage = new Stage();
                outsourcePartStage.setScene(new Scene(root));
                outsourcePartStage.show();
            }
        }
    }

    @FXML
    private void onModifyProductClicked() throws IOException {
        if (productSelected != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/AddProduct.fxml"));
            FlowPane root = loader.load();
            AddProduct addProduct = loader.getController();
            addProduct.isModify(productSelected);
            Stage addProductStage = new Stage();
            addProductStage.setScene(new Scene(root));
            addProductStage.show();
        }
    }

    @FXML
    public void onExit() {

        alert.setTitle("Exit");
        alert.setHeaderText("You are exiting the application. ");
        alert.setContentText("Would you like to proceed? (select OK to proceed exit)");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Platform.exit();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onDeletePart() {
        if (partSelected != null) {
            alert.setTitle("Delete");
            alert.setHeaderText("You are about to delete " + partSelected.getName());
            alert.setContentText("Are you sure you would like to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(partSelected);
            }
        }
    }


    @FXML
    public void onDeleteProduct() {
        if (productSelected != null) {
            alert.setTitle("Delete");
            alert.setHeaderText("You are about to delete " + productSelected.getName());
            alert.setContentText("Are you sure you would like to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(productSelected);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clmPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        clmInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));

        filteredList = new FilteredList<>(Inventory.getAIIParts(), part -> true);

        tblParts.setItems(filteredList);
        tblParts.getSelectionModel().selectedItemProperty().addListener((observableValue, part, t1) -> {
            if (t1 != null) {
                partSelectedIsInHouse = t1.getClass() == InHouse.class;
            }
            partSelected = observableValue.getValue();

        });

        clmProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        clmProductInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productFilteredList = new FilteredList<>(Inventory.getAIIProducts(), product -> true);
        tblProduct.setItems(productFilteredList);
        tblProduct.getSelectionModel().selectedItemProperty().addListener((observableValue, product, t1) -> {
            productSelected = observableValue.getValue();
        });
    }
}

