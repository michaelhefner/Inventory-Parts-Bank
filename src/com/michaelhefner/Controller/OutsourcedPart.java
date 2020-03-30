/*************************************************************************
 Michael Hefner
 C482 - Software 1

 *************************************************************************/

package com.michaelhefner.Controller;

import com.michaelhefner.Model.InHouse;
import com.michaelhefner.Model.Inventory;
import com.michaelhefner.Model.Outsourced;
import com.michaelhefner.Model.Part;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
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

public class OutsourcedPart  implements Initializable {

    private int partIDToModify = -1;
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

    final private String EMPTY_ERROR =
            "-fx-background-color: rgba(255, 0, 0, 0.1);" +
                    " -fx-border-color: rgba(255,0,0,1);";

    final private String NO_ERROR =
            "-fx-background-color: rgba(225, 255, 255, 1);";

    @FXML
    private void onSaveClicked(ActionEvent actionEvent){
        TextField[] allFields = {txtPrice,txtID,txtInv,txtCompanyName,txtMax,txtMin,txtName,txtPrice};
        TextField[] integerFields = {txtID,txtInv,txtCompanyName,txtMax,txtMin};
        TextField[] doubleFields = {txtPrice};
        Stage stage = (Stage) txtMin.getScene().getWindow();

        if(validateFields(allFields, integerFields, doubleFields)){
            InHouse newInHousePart = new InHouse(Integer.parseInt(txtID.getText()),
                    txtName.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtInv.getText()),
                    Integer.parseInt(txtMin.getText()),
                    Integer.parseInt(txtMin.getText()),
                    Integer.parseInt(txtCompanyName.getText()));
            if(partIDToModify >= 0){
                Inventory.updatePart(partIDToModify, newInHousePart);
                stage.close();
            } else if (partIDToModify == -1){
                Inventory.addPart(newInHousePart);
                stage.close();
            }
        }
    }

    @FXML
    private void closeWindow(){
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtID.setText(Integer.toString(Inventory.lookupPart(Inventory.getAIIParts().size() - 1).getId() + 1));
        txtID.setDisable(true);
        rbInHouse.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                System.out.println(t1);
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../View/InHousePart.fxml"));
                    FlowPane root = loader.load();
                    InHousePart inHousePart = loader.getController();
                    inHousePart.isModify(partIDToModify);
                    Stage inHousePartStage = new Stage();
                    inHousePartStage.setScene(new Scene(root));
                    inHousePartStage.show();
                    Stage stage = (Stage) btnCancel.getScene().getWindow();
                    stage.close();
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });
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

    public void isModify(int partToModify){
        if(partToModify != -1){
            this.partIDToModify = partToModify;
            Outsourced outsourced = (Outsourced) Inventory.lookupPart(partToModify);
            txtID.setText(Integer.toString(outsourced.getId()));
            txtName.setText(outsourced.getName());
            txtInv.setText(Integer.toString(outsourced.getStock()));
            txtPrice.setText(Double.toString(outsourced.getPrice()));
            txtMax.setText(Integer.toString(outsourced.getMax()));
            txtMin.setText(Integer.toString(outsourced.getMin()));
            txtCompanyName.setText(outsourced.getCompanyName());
            txtHeading.setText("Modify Part");
        }
    }

}