/*************************************************************************
 Michael Hefner
 C482 - Software 1
 *************************************************************************/


package com.michaelhefner.Controller;

import com.michaelhefner.Model.InHouse;
import com.michaelhefner.Model.Inventory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class InHousePart implements Initializable {

    final private String EMPTY_ERROR =
            "-fx-background-color: rgba(255, 0, 0, 0.1);" +
            " -fx-border-color: rgba(255,0,0,1);";

    final private String NO_ERROR =
            "-fx-background-color: rgba(225, 255, 255, 1);";
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
    private TextField txtMachineID;
    @FXML
    private RadioButton rbOutsource;
    @FXML
    private Button btnCancel;

    @FXML
    private void onSaveClicked(ActionEvent actionEvent){
        TextField[] allFields = {txtPrice,txtID,txtInv,txtMachineID,txtMax,txtMin,txtName,txtPrice};
        TextField[] integerFields = {txtID,txtInv,txtMachineID,txtMax,txtMin};
        TextField[] doubleFields = {txtPrice};

        if(validateFields(allFields, integerFields, doubleFields)){
            InHouse newInHousePart = new InHouse(Integer.parseInt(txtID.getText()),
                    txtName.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtInv.getText()),
                    Integer.parseInt(txtMin.getText()),
                    Integer.parseInt(txtMin.getText()),
                    Integer.parseInt(txtMachineID.getText()));
            if(partIDToModify >= 0){
                Inventory.updatePart(partIDToModify, newInHousePart);
                closeWindow();
            } else if (partIDToModify == -1){
                Inventory.addPart(newInHousePart);
                System.out.println("added inhouse");
                closeWindow();
            }
        }
    }

    @FXML
    private void closeWindow(){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private boolean validateFields(TextField[] allFields,
                                   TextField[] integerFields,
                                   TextField[] doubleFields){
        boolean isValid = true;
        isValid &= checkForEmptyField(allFields);
        isValid &= checkForIntegerField(integerFields);
        isValid &= checkForDoubleField(doubleFields);
        isValid &= checkMinMax(txtMin, txtMax);
        return isValid;
    }

    private boolean checkMinMax(TextField min, TextField max){
        boolean isValid = true;
        if(!txtMin.getText().isEmpty() && !txtMax.getText().isEmpty()){
            if (Integer.parseInt(txtMax.getText()) < Integer.parseInt(txtMin.getText())){
                txtMin.setStyle(EMPTY_ERROR);
                txtMax.setStyle(EMPTY_ERROR);
                isValid = false;
            } else {
                txtMin.setStyle(NO_ERROR);
                txtMax.setStyle(NO_ERROR);
            }
        } else {
            txtMin.setStyle(EMPTY_ERROR);
            txtMax.setStyle(EMPTY_ERROR);
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
            InHouse inHouse = (InHouse) Inventory.lookupPart(partToModify);
            txtID.setText(Integer.toString(inHouse.getId()));
            txtName.setText(inHouse.getName());
            txtInv.setText(Integer.toString(inHouse.getStock()));
            txtPrice.setText(Double.toString(inHouse.getPrice()));
            txtMax.setText(Integer.toString(inHouse.getMax()));
            txtMin.setText(Integer.toString(inHouse.getMin()));
            txtMachineID.setText(Integer.toString(inHouse.getMachineId()));
            txtHeading.setText("Modify Part");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtID.setText(Integer.toString(Inventory.getAIIParts().size()));
        txtID.setDisable(true);
        rbOutsource.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                System.out.println(t1);
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../View/OutsourcedPart.fxml"));
                    FlowPane root = loader.load();
                    OutsourcedPart outsourcedPart = loader.getController();
                    outsourcedPart.isModify(partIDToModify);
                    Stage outsourcePartStage = new Stage();
                    outsourcePartStage.setScene(new Scene(root));
                    outsourcePartStage.show();
                    closeWindow();
                } catch (Exception e){
                    System.out.println(e);
                }

            }
        });
    }
}