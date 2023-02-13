package com.example.management;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField pass_text;

    @FXML
    private TextField pass_hidden;
    @FXML
    private CheckBox checkBox;


    //Run
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindPasswordShowHideWithCheckBox();


    }

    private void bindPasswordShowHideWithCheckBox() {
        // Set initial state
        pass_text.setManaged(false);
        pass_text.setVisible(false);

        // Bind properties. Toggle textField and passwordField
        // visibility and managability properties mutually when checkbox's state is changed.
        // Because we want to display only one component (textField or passwordField)
        // on the scene at a time.
        pass_text.managedProperty().bind(checkBox.selectedProperty());
        pass_text.visibleProperty().bind(checkBox.selectedProperty());

        pass_hidden.managedProperty().bind(checkBox.selectedProperty().not());
        pass_hidden.visibleProperty().bind(checkBox.selectedProperty().not());

        // Bind the textField and passwordField text values bidirectionally.
        pass_hidden.textProperty().bindBidirectional(pass_text.textProperty());
    }


    @FXML
    protected void onExitButtonClicked(){
        System.exit(0);
    }


    @FXML
    protected  void onLoginClicked(){
        System.out.println("Username: " + txtEmail.getText());
        System.out.println("Password: " + pass_hidden.getText());
        System.out.println("Login button clicked");
    }

}
