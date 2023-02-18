package com.example.management;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void manageResidents(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ManagementApplication.class.getResource("residents-view.fxml"));

            Parent root1 = fxmlLoader.load();

            // update parent's stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
            stage.setTitle("Residents Management");

        } catch (Exception ex) {

            ex.printStackTrace();
            showAlert(ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void manageEvents(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ManagementApplication.class.getResource("events-view.fxml"));

            Parent root1 = fxmlLoader.load();

            // update parent's stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
            stage.setTitle("Events Management");

        } catch (Exception ex) {
            showAlert(ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void approveEvents(ActionEvent actionEvent) {
//        go to approve events page

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ManagementApplication.class.getResource("events-approval.fxml"));

            Parent root1 = fxmlLoader.load();

            // update parent's stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
            stage.setTitle("Events Approval and Rejection Form");

        } catch (Exception ex) {
            showAlert(ex.getMessage(), Alert.AlertType.ERROR);
        }

    }


    public void logout(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ManagementApplication.class.getResource("login-view.fxml"));

            Parent root1 = fxmlLoader.load();

            // update parent's stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
            stage.setTitle("Login");

        } catch (Exception ex) {
            showAlert(ex.getMessage(), Alert.AlertType.ERROR);
        }

    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
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
