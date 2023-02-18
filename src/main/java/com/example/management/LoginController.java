package com.example.management;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private DBModel model = new DBModel();

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
    protected  void onLoginClicked(ActionEvent event ){
        System.out.println("Username: " + txtEmail.getText());
        System.out.println("Password: " + pass_hidden.getText());
        System.out.println("Login button clicked");


        try {

//            check if email is empty
            if (txtEmail.getText().isBlank()) {
                showAlert("Email is required.", Alert.AlertType.ERROR);
            }
            else if (pass_hidden.getText().isBlank()) {
                showAlert("Password is required.", Alert.AlertType.ERROR);
            }
            else {
                if (model.UserLogin(txtEmail.getText(), pass_hidden.getText())) {
                    FXMLLoader fxmlLoader = new FXMLLoader(ManagementApplication.class.getResource("main-view.fxml"));

                    Parent root1 = fxmlLoader.load();

                    // update parent's stage
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root1));
                    stage.show();
                    stage.setTitle("Events Management");
                }
                else {
                    showAlert("Username or password is incorrect.", Alert.AlertType.INFORMATION);
                }
            }
        } catch(SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        switch (type) {
            case ERROR -> alert.setTitle("ERROR");
            case INFORMATION -> alert.setTitle("SUCCESS");
            case WARNING -> alert.setTitle("WARNING");
        }
        alert.setHeaderText(message);
        alert.show();
    }

}
