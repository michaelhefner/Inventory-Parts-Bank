/*************************************************************************
 Michael Hefner
 C482 - Software 1
 *************************************************************************/

package com.michaelhefner.Controller;

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
    private ObservableList<Part> allPartsObservable = FXCollections.observableArrayList();
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
    private void onSaveClicked(ActionEvent actionEvent) {
        TextField[] allFields = {txtPrice, txtID, txtInv, txtMax, txtMin, txtProductName, txtPrice};
        TextField[] integerFields = {txtID, txtInv, txtMax, txtMin};
        TextField[] doubleFields = {txtPrice};
        Stage stage = (Stage) txtMin.getScene().getWindow();

        if (validateFields(allFields, integerFields, doubleFields)) {
            Product tempProduct = new Product(Integer.parseInt(txtID.getText()),
                    txtProductName.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtInv.getText()),
                    Integer.parseInt(txtMin.getText()),
                    Integer.parseInt(txtMax.getText()));

            for (Part part : partsToAddToProduct) {
                tempProduct.addAssociatedPart(part);
            }
            if (productIDToModify >= 0) {
                Inventory.updateProduct(productIDToModify, tempProduct);
                stage.close();
            } else if (productIDToModify == -1) {
                Inventory.addProduct(tempProduct);
                stage.close();
            }
        }
    }

    @FXML
    private void closeWindow() {
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
    private void onAddPartClicked(ActionEvent actionEvent) {
        double totalCost = 0.0;
        for (int i = 0; i < partsToAddToProduct.size(); i++) {
            totalCost += partsToAddToProduct.get(i).getPrice();
        }
        if ((partSelectedToAdd >= 0)
                && (totalCost + Inventory.lookupPart(partSelectedToAdd).getPrice()) <= Double.parseDouble(txtPrice.getText())) {
            partsToAddToProduct.add(Inventory.lookupPart(partSelectedToAdd));
            txtPrice.setStyle(NO_ERROR);
            tblAddedParts.setStyle(NO_ERROR);
        } else {
            txtPrice.setStyle(EMPTY_ERROR);
            tblAddedParts.setStyle(EMPTY_ERROR);
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
                                   TextField[] doubleFields) {
        boolean isValid = true;
        isValid &= checkForEmptyField(allFields);
        isValid &= checkForIntegerField(integerFields);
        isValid &= checkForDoubleField(doubleFields);
        isValid &= checkForMinMax(txtMin, txtMax);
        isValid &= checkForMinInv(txtInv);
        isValid &= checkForInvMax(txtInv, txtMax);
        isValid &= checkForPartsAdded(tblAddedParts);
        return isValid;
    }

    private boolean checkForPartsAdded(TableView tableView) {
        boolean isValid = true;
        if (tableView.getItems().size() > 0) {
            tableView.setStyle(NO_ERROR);
        } else {
            tableView.setStyle(EMPTY_ERROR);
            isValid = false;
        }
        return isValid;
    }

    private boolean checkForInvMax(TextField inv, TextField max) {
        boolean isValid = true;
        if (!inv.getText().isEmpty() && !max.getText().isEmpty()) {
            if (Integer.parseInt(inv.getText()) > Integer.parseInt(max.getText())) {
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

    private boolean checkForMinInv(TextField inv) {
        boolean isValid = true;
        if (!inv.getText().isEmpty()) {
            if (Integer.parseInt(inv.getText()) < 1) {
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

    private boolean checkForMinMax(TextField min, TextField max) {
        boolean isValid = true;
        if (!min.getText().isEmpty() && !max.getText().isEmpty()) {
            if (Integer.parseInt(max.getText()) < Integer.parseInt(min.getText())) {
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

    private boolean checkForEmptyField(TextField[] textFields) {
        boolean isValid = true;
        for (TextField field : textFields) {
            if (field.getText().isEmpty()) {
                field.setStyle(EMPTY_ERROR);
                isValid = false;
            } else {
                field.setStyle(NO_ERROR);
            }
        }
        return isValid;
    }

    private boolean checkForIntegerField(TextField[] textFields) {
        boolean isValid = true;
        for (TextField field : textFields) {
            if (!field.getText().isEmpty()) {
                try {
                    Integer.parseInt(field.getText());
                    field.setStyle(NO_ERROR);
                } catch (Exception e) {
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

    private boolean checkForDoubleField(TextField[] textFields) {
        boolean isValid = true;
        for (TextField field : textFields) {
            if (!field.getText().isEmpty()) {
                try {
                    Double.parseDouble(field.getText());
                    field.setStyle(NO_ERROR);
                } catch (Exception e) {
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


    public void isModify(int productToModify) {
        if (productToModify != -1 && productToModify < Inventory.getAIIProducts().size()) {
            this.productIDToModify = productToModify;
            Product product = Inventory.lookupProduct(productToModify);
            txtID.setText(Integer.toString(product.getId()));
            txtProductName.setText(product.getName());
            txtInv.setText(Integer.toString(product.getStock()));
            txtPrice.setText(Double.toString(product.getPrice()));
            txtMax.setText(Integer.toString(product.getMax()));
            txtMin.setText(Integer.toString(product.getMin()));
            if (!product.getAllAssociatedParts().isEmpty()) {
                partsToAddToProduct.addAll(product.getAllAssociatedParts());
            }
            txtHeading.setText("Modify Product");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allPartsObservable.addAll(Inventory.getAIIParts());

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
                if (t1 != null) {
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
                if (t1 != null) {
                    partSelectedToDelete = t1.getId();
                }
            }
        });


    }

    private PropertyValueFactory<Part, String> partFactory(String val) {
        return new PropertyValueFactory<Part, String>(val);
    }


}
