/*************************************************************************
Michael Hefner
 C482 - Software 1
 I. User Interface


 Create a JavaFX application with a graphical user interface (GUI) based on the attached “GUI Mock-Up”. Write code to display each  of the following screens in the GUI:



 A.  A main screen, showing the following controls:

 •  buttons for “Add”, “Modify”, “Delete”, “Search” for parts and products, and “Exit”

 •  lists for parts and products

 •  text boxes for searching for parts and products

 •  title labels for parts, products, and the application title



 B.  An add part screen, showing the following controls:

 •  radio buttons for “In-House” and “Outsourced” parts

 •  buttons for “Save” and “Cancel”

 •  text fields for ID, name, inventory level, price, max and min values, and company name or machine ID

 •  labels for ID, name, inventory level, price/cost, max and min values, the application title, and company name or machine ID



 C.  A modify part screen, with fields that populate with presaved data, showing the following controls:

 •  radio buttons for “In-House” and “Outsourced” parts

 •  buttons for “Save” and “Cancel”

 •  text fields for ID, name, inventory level, price, max and min values, and company name or machine ID

 •  labels for ID, name, inventory level, price, max and min values, the application title, and company name or machine ID


 D. An add product screen, showing the following controls:

 •  buttons for “Save”, “Cancel”, “Add” part, and “Delete” part

 •  text fields for ID, name, inventory level, price, and max and min values

 •  labels for ID, name, inventory level, price, max and min values, and the application

 •  a list for associated parts for this product

 •  a “Search” button and a text field with an associated list for displaying the results of the search


 E.  A modify product screen, with fields that populate with presaved data, showing the following controls:

 •  buttons for “Save”, “Cancel”, “Add” part, and “Delete” part

 •  text fields for ID, name, inventory level, price, and max and min values

 •  labels for ID, name, inventory level, price, max and min values, and the application

 •  a list for associated parts for this product

 •  a “Search” button and a text field with associated list for displaying the results of the search


 II. Application



 Now that you’ve created the GUI, write code to create the class structure provided in the attached “UML (unified modeling language) Class Diagram”. Enable each  of the following capabilities in the application:



 F.  Using the attached “UML Class Diagram”, create appropriate classes and instance variables with the following criteria:

 •  five classes with the 16 associated instance variables

 •  variables are only accessible through getter methods

 •  variables are only modifiable through setter methods


 G.  Add the following functionalities to the main screen, using the methods provided in the attached “UML Class Diagram”:

 •  redirect the user to the “Add Part”, “Modify Part”, “Add Product”, or “Modify Product” screens

 •  delete a selected part or product from the list

 •  search for a part or product and display matching results

 •  exit the main screen



 H.  Add the following functionalities to the part screens, using the methods provided in the attached “UML Class Diagram”:

 1.  “Add Part” screen

 •  select “In-House” or “Outsourced”

 •  enter name, inventory level, price, max and min values, and company name or machine ID

 •  save the data and then redirect to the main screen

 •  cancel or exit out of this screen and go back to the main screen

 2.  “Modify Part” screen

 •  select “In-House” or “Outsourced”

 •  modify or change data values

 •  save modifications to the data and then redirect to the main screen

 •  cancel or exit out of this screen and go back to the main screen


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
import com.michaelhefner.Model.Part;
import com.michaelhefner.Model.Product;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AddProduct implements Initializable {

    final private String EMPTY_ERROR =
            "-fx-background-color: rgba(255, 0, 0, 0.1);" +
                    " -fx-border-color: rgba(255,0,0,1);";
    final private String NO_ERROR =
            "-fx-background-color: rgba(225, 255, 255, 1);";
    private int partSelectedToAdd = -1;
    private int partSelectedToDelete = -1;
    private Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    private FilteredList<Part> filteredList;
    private ObservableList<Part> allPartsObservable = Inventory.getAIIParts();
    private ObservableList<Part> partsToAddToProduct = FXCollections.observableArrayList();
    private Product newProduct;

    private int productIDToModify = -1;
    @FXML
    private Text txtHeading;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtInv;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtMin;
    @FXML
    private TextField txtMax;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtSearchPart;
    @FXML
    private TableView<Part> tblToAddParts;
    @FXML
    private TableColumn<Part, String> clmToAddPartID;
    @FXML
    private TableColumn<Part, String> clmToAddPartName;
    @FXML
    private TableColumn<Part, String> clmToAddInvLevel;
    @FXML
    private TableColumn<Part, String> clmToAddPrice;
    @FXML
    private TableView<Part> tblAddedParts;
    @FXML
    private TableColumn<Part, String> clmAddedPartID;
    @FXML
    private TableColumn<Part, String> clmAddedPartName;
    @FXML
    private TableColumn<Part, String> clmAddedPartInvLevel;
    @FXML
    private TableColumn<Part, String> clmAddedPartPrice;
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
    private void onSaveClicked(ActionEvent actionEvent){
        TextField[] allFields = {txtPrice,txtID,txtInv,txtMax,txtMin,txtProductName,txtPrice};
        TextField[] integerFields = {txtID,txtInv,txtMax,txtMin};
        TextField[] doubleFields = {txtPrice};
        Stage stage = (Stage) txtMin.getScene().getWindow();

        if(validateFields(allFields, integerFields, doubleFields)){
            Product tempProduct = new Product(Integer.parseInt(txtID.getText()),
                    txtProductName.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtInv.getText()),
                    Integer.parseInt(txtMin.getText()),
                    Integer.parseInt(txtMax.getText()));
            for (Part part : partsToAddToProduct){
                System.out.println(part.getClass() + part.getName());
//                tempProduct.addAssociatedPart(part);
            }
            if(productIDToModify >= 0){
                Inventory.updateProduct(productIDToModify, tempProduct);
                stage.close();
            } else if (productIDToModify == -1){
                Inventory.addProduct(tempProduct);
                stage.close();
            }
        }
    }

    @FXML
    private void closeWindow(){
        alert.setTitle("Cancel");
        alert.setHeaderText("You are about to close this window");
        alert.setContentText("Select OK to proceed");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void onAddPartClicked(ActionEvent actionEvent){
        if (partSelectedToAdd >= 0){
            partsToAddToProduct.add(Inventory.lookupPart(partSelectedToAdd));
            allPartsObservable.remove(Inventory.lookupPart(partSelectedToAdd));
        }
    }


    @FXML
    public void onDeletePart(ActionEvent actionEvent) {
        if (partSelectedToDelete != -1) {
            Part part = Inventory.lookupPart(partSelectedToDelete);
            alert.setTitle("Delete");
            alert.setHeaderText("You are about to delete " + part.getName());
            alert.setContentText("Are you sure you would like to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                partsToAddToProduct.remove(part);
            }
        }
    }

    private boolean validateFields(TextField[] allFields,
                                   TextField[] integerFields,
                                   TextField[] doubleFields){
        boolean isValid = true;
        isValid &= checkForEmptyField(allFields);
        isValid &= checkForIntegerField(integerFields);
        isValid &= checkForDoubleField(doubleFields);
        isValid &= checkForMinMax(txtMin, txtMax);
        isValid &= checkForMinInv(txtInv);
        isValid &= checkForInvMax(txtInv, txtMax);
        return isValid;
    }

    private boolean checkForInvMax(TextField inv, TextField max){
        boolean isValid = true;
        if (!inv.getText().isEmpty() && !max.getText().isEmpty()){
            if (Integer.parseInt(inv.getText()) > Integer.parseInt(max.getText())){
                inv.setStyle(EMPTY_ERROR);
                max.setStyle(EMPTY_ERROR);
                isValid = false;
            } else {
                inv.setStyle(NO_ERROR);
                max.setStyle(NO_ERROR);
            }
        } else {
            inv.setStyle(EMPTY_ERROR);
            max.setStyle(EMPTY_ERROR);
            isValid = false;
        }
        return isValid;
    }
    private boolean checkForMinInv(TextField inv){
        boolean isValid = true;
        if (!inv.getText().isEmpty()){
            if(Integer.parseInt(inv.getText()) < 1){
                inv.setStyle(EMPTY_ERROR);
                isValid = false;
            } else {
                inv.setStyle(NO_ERROR);
            }
        } else {
            inv.setStyle(EMPTY_ERROR);
            isValid = false;
        }
        return isValid;
    }
    private boolean checkForMinMax(TextField min, TextField max){
        boolean isValid = true;
        if(!min.getText().isEmpty() && !max.getText().isEmpty()){
            if (Integer.parseInt(max.getText()) < Integer.parseInt(min.getText())){
                min.setStyle(EMPTY_ERROR);
                max.setStyle(EMPTY_ERROR);
                isValid = false;
            } else {
                min.setStyle(NO_ERROR);
                min.setStyle(NO_ERROR);
            }
        } else {
            min.setStyle(EMPTY_ERROR);
            max.setStyle(EMPTY_ERROR);
            isValid = false;
        }
        return isValid;
    }
    private boolean checkForEmptyField(TextField[] textFields){
        boolean isValid = true;
        for(TextField field : textFields){
            if(field.getText().isEmpty()){
                field.setStyle(EMPTY_ERROR);
                isValid = false;
            } else {
                field.setStyle(NO_ERROR);
            }
        }
        return isValid;
    }

    private boolean checkForIntegerField(TextField[] textFields){
        boolean isValid = true;
        for(TextField field : textFields){
            if(!field.getText().isEmpty()){
                try{
                    Integer.parseInt(field.getText());
                    field.setStyle(NO_ERROR);
                } catch (Exception e){
                    field.setStyle(EMPTY_ERROR);
                    field.setText("Invalid Value");
                    isValid = false;
                }
            } else {
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean checkForDoubleField(TextField[] textFields){
        boolean isValid = true;
        for(TextField field : textFields){
            if(!field.getText().isEmpty()){
                try{
                    Double.parseDouble(field.getText());
                    field.setStyle(NO_ERROR);
                } catch (Exception e){
                    field.setStyle(EMPTY_ERROR);
                    field.setText("Invalid Value");
                    isValid = false;
                }
            } else {
                isValid = false;
            }
        }
        return isValid;
    }


    public void isModify(int productToModify){
        if(productToModify != -1 && productToModify < Inventory.getAIIProducts().size()){
            this.productIDToModify = productToModify;
            Product product = Inventory.lookupProduct(productToModify);
            txtID.setText(Integer.toString(product.getId()));
            txtProductName.setText(product.getName());
            txtInv.setText(Integer.toString(product.getStock()));
            txtPrice.setText(Double.toString(product.getPrice()));
            txtMax.setText(Integer.toString(product.getMax()));
            txtMin.setText(Integer.toString(product.getMin()));
            if (!product.getAllAssociatedParts().isEmpty()){
                partsToAddToProduct.addAll(product.getAllAssociatedParts());
            }
            txtHeading.setText("Modify Product");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        clmToAddPartID.setCellValueFactory(partFactory("id"));
        clmToAddPartName.setCellValueFactory(partFactory("name"));
        clmToAddPrice.setCellValueFactory(partFactory("price"));
        clmToAddInvLevel.setCellValueFactory(partFactory("stock"));

        filteredList = new FilteredList<Part>(allPartsObservable, new Predicate<Part>() {
            @Override
            public boolean test(Part part) {
                return true;
            }
        });

        tblToAddParts.setItems(filteredList);
        tblToAddParts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Part>() {
            @Override
            public void changed(ObservableValue<? extends Part> observableValue, Part part, Part t1) throws NullPointerException {
                if(t1 != null) {
                    partSelectedToAdd = t1.getId();
                }
            }
        });


        clmAddedPartID.setCellValueFactory(partFactory("id"));
        clmAddedPartName.setCellValueFactory(partFactory("name"));
        clmAddedPartPrice.setCellValueFactory(partFactory("price"));
        clmAddedPartInvLevel.setCellValueFactory(partFactory("stock"));

        tblAddedParts.setItems(partsToAddToProduct);
        tblAddedParts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Part>() {
            @Override
            public void changed(ObservableValue<? extends Part> observableValue, Part part, Part t1) throws NullPointerException {
                if(t1 != null) {
                    partSelectedToDelete = t1.getId();
                }
            }
        });


    }

    private PropertyValueFactory<Part, String> partFactory(String val){
        return new PropertyValueFactory<Part, String>(val);
    }


}
