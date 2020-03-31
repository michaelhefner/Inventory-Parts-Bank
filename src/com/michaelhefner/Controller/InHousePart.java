/*************************************************************************
 Michael Hefner
 C482 - Software 1
 I. User Interface


 Done I. User Interface

 Done B.  An add part screen, showing the following controls:

 Done •  radio buttons for “In-House” and “Outsourced” parts

 Done •  buttons for “Save” and “Cancel”

 Done •  text fields for ID, name, inventory level, price, max and min values, and company name or machine ID

 Done •  labels for ID, name, inventory level, price/cost, max and min values, the application title, and company name or machine ID

 Done C.  A modify part screen, with fields that populate with presaved data, showing the following controls:

 Done •  radio buttons for “In-House” and “Outsourced” parts

 Done  •  buttons for “Save” and “Cancel”

 Done •  text fields for ID, name, inventory level, price, max and min values, and company name or machine ID

 Done •  labels for ID, name, inventory level, price, max and min values, the application title, and company name or machine ID

 Done H.  Add the following functionalities to the part screens, using the methods provided in the attached “UML Class Diagram”:

 Done 1.  “Add Part” screen

 Done •  select “In-House” or “Outsourced”

 Done •  enter name, inventory level, price, max and min values, and company name or machine ID

 Done •  save the data and then redirect to the main screen

 Done •  cancel or exit out of this screen and go back to the main screen

 Done 2.  “Modify Part” screen

 Done •  select “In-House” or “Outsourced”

 Done •  modify or change data values

 Done •  save modifications to the data and then redirect to the main screen

 Done •  cancel or exit out of this screen and go back to the main screen

 Done J.  Write code to implement exception controls with custom error messages for one requirement out of each of the following sets (pick one from each):

 Done 1.  Set 1

 Done •  entering an inventory value that exceeds the minimum or maximum value for that part or product

 Done •  preventing the minimum field from having a value above the maximum field

 Done •  preventing the maximum field from having a value below the minimum field

 Done •  ensuring that a product must always have at least one part
 *************************************************************************/


package com.michaelhefner.Controller;

import com.michaelhefner.Model.InHouse;
import com.michaelhefner.Model.Inventory;
import com.michaelhefner.Model.Part;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class InHousePart implements Initializable {

    final private String EMPTY_ERROR =
            "-fx-background-color: rgba(255, 0, 0, 0.1);" +
                    " -fx-border-color: rgba(255,0,0,1);";

    final private String NO_ERROR =
            "-fx-background-color: rgba(225, 255, 255, 1);";
    private Part partIDToModify;
    @FXML
    private Text txtHeading;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtInv;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtMin;
    @FXML
    private TextField txtMax;
    @FXML
    private TextField txtMachineID;
    @FXML
    private RadioButton rbOutsource;
    @FXML
    private Button btnCancel;

    @FXML
    private void onSaveClicked(ActionEvent actionEvent) {
        TextField[] allFields = {txtPrice, txtID, txtInv, txtMachineID, txtMax, txtMin, txtName, txtPrice};
        TextField[] integerFields = {txtID, txtInv, txtMachineID, txtMax, txtMin};
        TextField[] doubleFields = {txtPrice};
        Stage stage = (Stage) txtMin.getScene().getWindow();

        if (validateFields(allFields, integerFields, doubleFields)) {
            InHouse newInHousePart = new InHouse(Integer.parseInt(txtID.getText()),
                    txtName.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtInv.getText()),
                    Integer.parseInt(txtMin.getText()),
                    Integer.parseInt(txtMax.getText()),
                    Integer.parseInt(txtMachineID.getText()));
            if (partIDToModify != null) {
                Inventory.updatePart(partIDToModify.getId(), newInHousePart);
                stage.close();
            } else {
                Inventory.addPart(newInHousePart);
                stage.close();
            }
        }
    }

    @FXML
    private void closeWindow() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("You are about to close this window");
        alert.setContentText("Select OK to proceed");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
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

    public void isModify(Part partToModify) {
        if (partToModify != null) {
            partIDToModify = partToModify;
            txtID.setText(Integer.toString(partToModify.getId()));
            txtName.setText(partToModify.getName());
            txtInv.setText(Integer.toString(partToModify.getStock()));
            txtPrice.setText(Double.toString(partToModify.getPrice()));
            txtMax.setText(Integer.toString(partToModify.getMax()));
            txtMin.setText(Integer.toString(partToModify.getMin()));
            if (Inventory.lookupPart(partToModify.getId()).getClass() == InHouse.class) {
                txtMachineID.setText(Integer.toString(((InHouse) partToModify).getMachineId()));
            }
            txtHeading.setText("Modify Part");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtID.setText(Integer.toString(Inventory.lookupPart(Inventory.getAIIParts().size() - 1).getId() + 1));
        txtID.setDisable(true);
        rbOutsource.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                System.out.println(t1);
                try {
                    FXMLLoader loader = new FXMLLoader();
                    System.out.println(loader);
                    loader.setLocation(getClass().getResource("../View/OutsourcedPart.fxml"));
                    FlowPane root = loader.load();
                    OutsourcedPart outsourcedPart = loader.getController();
                    System.out.println(partIDToModify);
                    outsourcedPart.isModify(partIDToModify);
                    Stage outsourcePartStage = new Stage();
                    outsourcePartStage.setScene(new Scene(root));
                    outsourcePartStage.show();
                    Stage stage = (Stage) btnCancel.getScene().getWindow();
                    stage.close();
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        });
    }
}