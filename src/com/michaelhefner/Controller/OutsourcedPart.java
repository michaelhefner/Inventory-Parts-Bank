/*
 * Michael Hefner
 * C482 - Software 1
 */

package com.michaelhefner.Controller;

import com.michaelhefner.Model.Inventory;
import com.michaelhefner.Model.Outsourced;
import com.michaelhefner.Model.Part;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class OutsourcedPart implements Initializable {

    private Part partToModify;
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
    private TextField txtCompanyName;
    @FXML
    private RadioButton rbInHouse;
    @FXML
    private Button btnCancel;

    final private String ERROR =
            "-fx-background-color: rgba(255, 0, 0, 0.1);" +
                    " -fx-border-color: rgba(255,0,0,1);";

    final private String NO_ERROR =
            "-fx-background-color: rgba(225, 255, 255, 1);";

    @FXML
    private void onSaveClicked() {
        TextField[] allFields = {txtPrice, txtID, txtInv, txtCompanyName, txtMax, txtMin, txtName, txtPrice};
        TextField[] integerFields = {txtID, txtInv, txtMax, txtMin};
        TextField[] doubleFields = {txtPrice};
        Stage stage = (Stage) txtMin.getScene().getWindow();

        if (validateFields(allFields, integerFields, doubleFields)) {
            Outsourced outsourcedPart = new Outsourced(Integer.parseInt(txtID.getText()),
                    txtName.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtInv.getText()),
                    Integer.parseInt(txtMin.getText()),
                    Integer.parseInt(txtMax.getText()),
                    txtCompanyName.getText());
            if (partToModify != null) {
                Inventory.updatePart(partToModify.getId(), outsourcedPart);
                stage.close();
            } else {
                Inventory.addPart(outsourcedPart);
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
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int currentIndex = 0;
        if (Inventory.getAIIParts().size() > 0)
            currentIndex = (Objects.requireNonNull(Inventory.lookupPart(Inventory.getAIIParts().size() - 1))).getId() + 1;
        txtID.setText(Integer.toString(currentIndex));
        txtID.setDisable(true);
        rbInHouse.selectedProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../View/InHousePart.fxml"));
                    FlowPane root = loader.load();
                    InHousePart inHousePart = loader.getController();
                    if (partToModify != null)
                        inHousePart.isModify(partToModify);
                    Stage inHousePartStage = new Stage();
                    inHousePartStage.setScene(new Scene(root));
                    inHousePartStage.show();
                    Stage stage = (Stage) btnCancel.getScene().getWindow();
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTextFieldError(TextField textField) {
        textField.setStyle(ERROR);
        textField.setText("Invalid Value");
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


        if (!isValid) {
            Alert invalidTypeAlert = new Alert(Alert.AlertType.CONFIRMATION);
            invalidTypeAlert.setTitle("Invalid Type");
            invalidTypeAlert.setHeaderText("Please check all input fields for correct value.");
            invalidTypeAlert.setContentText("Select OK to continue");
            invalidTypeAlert.showAndWait();
        }

        return isValid;
    }

    private boolean checkForInvMax(TextField inv, TextField max) {
        boolean isValid = true;
        try {
            Integer.parseInt(inv.getText());
        } catch (NumberFormatException e) {
            setTextFieldError(inv);
            isValid = false;
        }
        try {
            if (!inv.getText().isEmpty() && !max.getText().isEmpty()) {
                if (Integer.parseInt(inv.getText()) > Integer.parseInt(max.getText())) {
                    setTextFieldError(inv);
                    setTextFieldError(max);
                    isValid = false;
                } else {
                    inv.setStyle(NO_ERROR);
                    max.setStyle(NO_ERROR);
                }
            } else {
                setTextFieldError(inv);
                setTextFieldError(max);
                isValid = false;
            }
        } catch (NumberFormatException e) {
            setTextFieldError(max);
            isValid = false;
        }
        return isValid;
    }

    private boolean checkForMinInv(TextField inv) {
        boolean isValid = true;
        try {
            if (!inv.getText().isEmpty()) {
                if (Integer.parseInt(inv.getText()) < 1) {
                    setTextFieldError(inv);
                    isValid = false;
                } else {
                    inv.setStyle(NO_ERROR);
                }
            } else {
                setTextFieldError(inv);
                isValid = false;
            }
        } catch (NumberFormatException e) {
            inv.setStyle(ERROR);
            isValid = false;
        }
        return isValid;
    }

    private boolean checkForMinMax(TextField min, TextField max) {
        boolean isValid = true;
        try {
            Integer.parseInt(min.getText());
        } catch (NumberFormatException e) {
            setTextFieldError(min);
            isValid = false;
        }
        try {
            if (!min.getText().isEmpty() && !max.getText().isEmpty()) {
                if (Integer.parseInt(max.getText()) < Integer.parseInt(min.getText())) {
                    setTextFieldError(min);
                    setTextFieldError(max);
                    isValid = false;
                } else {
                    min.setStyle(NO_ERROR);
                    min.setStyle(NO_ERROR);
                }
            } else {
                setTextFieldError(min);
                setTextFieldError(max);
                isValid = false;
            }
        } catch (NumberFormatException e) {
            setTextFieldError(max);
            isValid = false;
        }
        return isValid;
    }

    private boolean checkForEmptyField(TextField[] textFields) {
        boolean isValid = true;
        for (TextField field : textFields) {
            if (field.getText().isEmpty()) {
                setTextFieldError(field);
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
                    setTextFieldError(field);
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
                    setTextFieldError(field);
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
            this.partToModify = partToModify;
            txtID.setText(Integer.toString(partToModify.getId()));
            txtName.setText(partToModify.getName());
            txtInv.setText(Integer.toString(partToModify.getStock()));
            txtPrice.setText(Double.toString(partToModify.getPrice()));
            txtMax.setText(Integer.toString(partToModify.getMax()));
            txtMin.setText(Integer.toString(partToModify.getMin()));
            if (Objects.requireNonNull(Inventory.lookupPart(partToModify.getId())).getClass() == Outsourced.class) {
                txtCompanyName.setText(((Outsourced) partToModify).getCompanyName());
            }
            txtHeading.setText("Modify Part");
        }
    }
}

