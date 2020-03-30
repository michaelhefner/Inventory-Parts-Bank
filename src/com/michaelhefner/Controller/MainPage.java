/*************************************************************************
 Michael Hefner
 C482 - Software 1

 II. Application

 G.  Add the following functionalities to the main screen, using the methods provided in the attached “UML Class Diagram”:

 •  delete a selected part or product from the list

 •  search for a part or product and display matching results

 I.  Add the following functionalities to the product screens, using the methods provided in the attached “UML Class Diagram”:

 1.  “Add Product” screen

 •  enter name, inventory level, price, and max and min values

 •  save the data and then redirect to the main screen

 •  associate one or more parts with a product

 •  remove or disassociate a part from a product

 •  cancel or exit out of this screen and go back to the main screen

 2.  “Modify Product” screen

 •  modify or change data values

 •  save modifications to the data and then redirect to the main screen

 •  associate one or more parts with a product

 •  remove or disassociate a part from a product

 •  cancel or exit out of this screen and go back to the main screen


 J.  Write code to implement exception controls with custom error messages for one requirement out of each of the following sets (pick one from each):

 1.  Set 1

 •  entering an inventory value that exceeds the minimum or maximum value for that part or product

 •  preventing the minimum field from having a value above the maximum field

 •  preventing the maximum field from having a value below the minimum field

 •  ensuring that a product must always have at least one part

 2.  Set 2

 •  including a confirm dialogue for all “Delete” and “Cancel” buttons

 •  ensuring that the price of a product cannot be less than the cost of the parts

 •  ensuring that a product must have a name, price, and inventory level (default 0)
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
import javafx.collections.transformation.FilteredList;
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
import java.util.function.Predicate;

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

    private FilteredList<Part> filteredList;

    private Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

    @FXML
    public void handleSearchPart(ActionEvent actionEvent) {
        filteredList.setPredicate(new Predicate<Part>() {
            @Override
            public boolean test(Part part) {
                if (txtSearchPart.getText().isEmpty())
                    return true;
                return (part.getName().equals(txtSearchPart.getText()));
            }
        });
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

        alert.setTitle("Exit");
        alert.setHeaderText("You are exiting the application. ");
        alert.setContentText("Would you like to proceed? (select OK to proceed exit)");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {

                Platform.exit();
                System.exit(0);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @FXML
    public void onDeletePart(ActionEvent actionEvent) {
        if (partSelected != -1) {
            Part part = Inventory.lookupPart(partSelected);
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


        filteredList = new FilteredList<Part>(Inventory.getAIIParts(), new Predicate<Part>() {
            @Override
            public boolean test(Part part) {
                return true;
            }
        });

//        tblParts.setItems(Inventory.getAIIParts());
        tblParts.setItems(filteredList);

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
