package com.example.management;

import jakarta.mail.MessagingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
public class ResidentController implements Initializable {

    private DBModel model = new DBModel();
    int currentId = -1;

    @FXML private TextField nameField;
    @FXML private TextField houseNumberField;
    @FXML private TextField clusterField;
    @FXML private TextField emailField;
    @FXML private Button actionButton;

    @FXML private TextArea announcementField;

    @FXML private Button sendAnnouncementButton;

    
    @FXML private TableColumn<Resident, String> nameColumn;

    @FXML private TableColumn<Resident, String> houseNumberColumn;
    @FXML private TableColumn<Resident, String> clusterColumn;
    @FXML private TableColumn<Resident, String> emailColumn;
    @FXML private TableColumn<Resident, Resident> editColumn;
    @FXML private TableColumn<Resident, Resident> deleteColumn;


    @FXML private ObservableList<Resident> events = FXCollections.observableArrayList();


    @FXML
    private TableView<Resident> residentsTable;


    // ...

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // add listener to avoid invalid input
        addListenerToField(houseNumberField);

//        SET up the column in the table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        houseNumberColumn.setCellValueFactory(new PropertyValueFactory<>("houseNumber"));
        clusterColumn.setCellValueFactory(new PropertyValueFactory<>("cluster"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add an edit button to each row
        Callback<TableColumn<Resident, Resident>, TableCell<Resident, Resident>> cellFactory =
                new Callback<TableColumn<Resident, Resident>, TableCell<Resident, Resident>>() {
                    @Override
                    public TableCell<Resident, Resident> call(TableColumn<Resident, Resident> column) {
                        return new EditButtonCell();
                    }
                };
        editColumn.setCellFactory(cellFactory);

        // Add a delete button to each row
        Callback<TableColumn<Resident, Resident>, TableCell<Resident, Resident>> cellFactory2 =
                new Callback<TableColumn<Resident, Resident>, TableCell<Resident, Resident>>() {
                    @Override
                    public TableCell<Resident, Resident> call(TableColumn<Resident, Resident> column) {
                        return new DeleteButtonCell();
                    }
                };
        deleteColumn.setCellFactory(cellFactory2);

        try {
            refreshData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


//        occupy full width
        residentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    public void openMainMenu(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ManagementApplication.class.getResource("main-view.fxml"));

        Parent root1 = fxmlLoader.load();

        // update parent's stage
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();
        stage.setTitle("Events Management");

    }

    public void onSendAnnouncementButtonClicked(ActionEvent actionEvent) {
//        get the announcement
        String announcement = announcementField.getText();
        if (announcement == null ||  announcement.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Announcement is empty");
            alert.setContentText("Please enter an announcement");
            alert.showAndWait();
        }
        else {
            try {
                model.sendAnnouncement(announcement);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Announcement sent");
                alert.setContentText("Announcement sent successfully");
                alert.showAndWait();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private abstract class MyTableCellButton<T> extends TableCell<T, T> {
        private Button button;
        public MyTableCellButton(String text) {
            button = new Button(text);

            // occupy full width
            button.setMaxWidth(Double.MAX_VALUE);

            button.setOnAction(event -> {
                T myEventItem = getTableRow().getItem();
                onEvent(myEventItem);
            });
        }

        abstract void onEvent(T myEvent);

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(button);
            }
        }
    }

    //    Define the edit button cell
    private class EditButtonCell extends MyTableCellButton<Resident> {

        public EditButtonCell() {
            super("Edit");
        }

        //        are all fields empty
        private boolean allFieldsEmpty() {
            return (nameField.getText().isEmpty() || nameField.getText().equals("0") ) &&
                    houseNumberField.getText().isEmpty() &&
                    clusterField.getText().isEmpty() &&
                    emailField.getText().isEmpty();
        }

        @Override
        void onEvent(Resident myEvent) {
            // Implement your edit event logic here
            System.out.println("event name is " + myEvent.getName());

            if (!allFieldsEmpty()) {
//                ask whether to discard previous
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Discard previous");
                alert.setHeaderText("Are you sure you want to discard previous changes?");
                alert.setContentText("Event name: " + myEvent.getName() );

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    // discard previous changes
                    fillFieldsWith(myEvent);
                } else {
                    // ... user chose CANCEL or closed the dialog
                    System.out.println("print nothing");
                }
            }
//            not editing any event
            else {
                fillFieldsWith(myEvent);
            }

        }
    }

    // Define the delete button cell
    private class DeleteButtonCell extends  MyTableCellButton<Resident> {

        public DeleteButtonCell() { super("Delete"); }

        @Override
        void onEvent(Resident myEvent) {

//            ask for confirmation using alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            ask for user, whether he wants to delete event or not
            alert.setTitle("Delete event");
            alert.setHeaderText("Are you sure you want to delete this event?");
            alert.setContentText("Event name: " + myEvent.getName() );

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK
                // delete event from database

                try {
                    model.deleteResident(myEvent.getId());
                    refreshData();
                } catch (SQLException e) {
                    System.out.println("exception while deleting");
                    throw new RuntimeException(e);
                }

            } else {
                // ... user chose CANCEL or closed the dialog
                System.out.println("print nothing");
            }
//             clear all field if the same event is being edited
            if (currentId == myEvent.getId()) {
                clearFields();
            }

        }
    }


    private void addListenerToField(TextField field) {
        field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            System.out.println("observableValue: " + observableValue);
//                remove non-digit characters
            if (newValue == null || newValue.isBlank() ) {
                return;
            }

            if (!newValue.matches("\\d+")) {
                field.setText(newValue.replaceAll("\\D", ""));
            }
//            remove leading zeros
            else if (newValue.matches("0+")) {
                field.setText(newValue.replaceAll("0+", ""));
//            should be in range 0-x

            }
        });
    }


    //    clear all fields
    private void clearFields() {
        actionButton.setText("Add");
        fillFieldsWith("", "", "", "");
        currentId = -1;
    }

    private void fillFieldsWith(Resident event){

        actionButton.setText("Edit");
        fillFieldsWith(event.getName(), event.getHouseNumber(), event.getCluster(), event.getEmail());
        currentId = event.getId();
    }

//    name, housenumber, cluster, email
    private void fillFieldsWith(String name, String houseNumber, String cluster, String email) {
        nameField.setText(name);
        houseNumberField.setText(houseNumber);
        clusterField.setText(cluster);
        emailField.setText(email);
    }

    public void handleActionButton(ActionEvent actionEvent) throws SQLException {
//        check operation

        String name = nameField.getText();
        String houseNumber = houseNumberField.getText();
        String cluster = clusterField.getText();
        String email = emailField.getText();

        if (actionButton.getText().equals("Edit")) {
            System.out.println("edit.id is " + currentId);
            System.out.println("Edit button clicked");

            model. updateResident(name, houseNumber, cluster, email, currentId);
            refreshData();
        }
        else {
            System.out.println("add.id is " + currentId);
            System.out.println("Add button clicked");

            model.addResident(name, houseNumber, cluster, email);
            try {
                refreshData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        clearFields();
    }


    //    synchronize the events list with the database
    private void refreshData() throws SQLException {
        events = model.getAllResidents();
        residentsTable.setItems(events);
    }


    @FXML private void handleCancelButton(ActionEvent event) {
        System.out.println("Cancel button clicked");
        clearFields();
    }


}
