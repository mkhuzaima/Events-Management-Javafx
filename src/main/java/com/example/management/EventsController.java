package com.example.management;

import javafx.beans.property.ReadOnlyStringWrapper;
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
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class EventsController implements Initializable {


    private DBModel model = new DBModel();



    //    button
    @FXML private Button actionButton;

    int currentId = -1;




    @FXML private TextField hoursField;
    @FXML private TextField minutesField;
    @FXML private TextField venueField;
//    @FXML private ComboBox<String> nameComboBox;
    @FXML private TextField nameField;
    @FXML private TextField organizerField;

    @FXML private DatePicker datePicker;
    @FXML private TableColumn<MyEvent, String> nameColumn;

    @FXML private TableColumn<MyEvent, String> venueColumn;
    @FXML private TableColumn<MyEvent, String> dateColumn;
    @FXML private TableColumn<MyEvent, String> timeColumn;
    @FXML private TableColumn<MyEvent, String> organizerColumn;
    @FXML private TableColumn<MyEvent, MyEvent> editColumn;
    @FXML private TableColumn<MyEvent, MyEvent> deleteColumn;


    @FXML private ObservableList<MyEvent> myEvents = FXCollections.observableArrayList();


    @FXML
    private TableView<MyEvent> eventsTable;


    // ...

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // add listener to avoid invalid input
        addListenerToField(hoursField, 23);
        addListenerToField(minutesField, 59);
        // ...

        // Set up the columns
        timeColumn.setCellValueFactory(cellData -> {
            MyEvent myEvent = cellData.getValue();
            int hours = myEvent.getHours();
            int minutes = myEvent.getMinutes();
            String time = String.format("%02d:%02d", hours, minutes);
            return new ReadOnlyStringWrapper(time);
        });

//        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        venueColumn.setCellValueFactory(new PropertyValueFactory<>("venue"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        organizerColumn.setCellValueFactory(new PropertyValueFactory<>("organizer"));

        // Add an edit button to each row
        Callback<TableColumn<MyEvent, MyEvent>, TableCell<MyEvent, MyEvent>> cellFactory =
                new Callback<TableColumn<MyEvent, MyEvent>, TableCell<MyEvent, MyEvent>>() {
                    @Override
                    public TableCell<MyEvent, MyEvent> call(TableColumn<MyEvent, MyEvent> column) {
                        return new EditButtonCell();
                    }
                };
        editColumn.setCellFactory(cellFactory);

        // Add a delete button to each row
        Callback<TableColumn<MyEvent, MyEvent>, TableCell<MyEvent, MyEvent>> cellFactory2 =
                new Callback<TableColumn<MyEvent, MyEvent>, TableCell<MyEvent, MyEvent>>() {
                    @Override
                    public TableCell<MyEvent, MyEvent> call(TableColumn<MyEvent, MyEvent> column) {
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
        eventsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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

// ...

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
    private class EditButtonCell extends MyTableCellButton<MyEvent> {

        public EditButtonCell() {
            super("Edit");
        }

//        are all fields empty
        private boolean allFieldsEmpty() {
            return (hoursField.getText()== null || hoursField.getText().isBlank() || hoursField.getText().equals("0") ) &&
                    ( minutesField.getText() == null || hoursField.getText().isBlank() || minutesField.getText().equals("0") ) &&
                    (venueField.getText() == null ||venueField.getText().isEmpty()) &&
                    (nameField.getText() == null ||nameField.getText().isEmpty()) &&
                    (organizerField.getText() == null ||organizerField.getText().isEmpty()) &&
                    datePicker.getValue() == null;
        }

        @Override
        void onEvent(MyEvent myEvent) {


            // Implement your edit event logic here
            System.out.println("event name is " + myEvent.getName());
            System.out.println("event venue is " + myEvent.getVenue());


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
    private class DeleteButtonCell extends  MyTableCellButton<MyEvent> {

        public DeleteButtonCell() { super("Delete"); }

        @Override
        void onEvent(MyEvent myEvent) {

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
                    model.deleteEvent(myEvent.getId(), false);
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


    private void addListenerToField(TextField field, int x) {
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

            } else if (Integer.parseInt(newValue) > x) {
                field.setText(String.valueOf(x));
            }
        });
    }


//    clear all fields
    private void clearFields() {
        actionButton.setText("Add");
        fillFieldsWith(0, 0, null, null, "", "");
        currentId = -1;
    }

    private void fillFieldsWith(MyEvent event){
        actionButton.setText("Edit");
        fillFieldsWith(event.getHours(), event.getMinutes(), event.getName(), event.getDate(), event.getVenue(), event.getOrganizer());
        currentId = event.getId();
    }

    private void fillFieldsWith(int hours, int minutes, String name, LocalDate date, String venue, String organizer) {
        hoursField.setText(String.valueOf(hours));
        minutesField.setText(String.valueOf(minutes));
        nameField.setText(name);
        datePicker.setValue(date);
        venueField.setText(venue);
        organizerField.setText(organizer);

        if (date == null ) {
            datePicker.getEditor().clear();
        }
        else {
            datePicker.getEditor().setText(date.toString());
        }
    }

    @FXML private void handleAddButton(ActionEvent event) {
//        check operation


        String name = nameField.getText();
        LocalDate date = datePicker.getValue();
        int hours = Integer.parseInt(hoursField.getText());
        int minutes = Integer.parseInt(minutesField.getText());
        String venue = venueField.getText();

        String organizer = organizerField.getText();


        if (actionButton.getText().equals("Edit")) {
            System.out.println("edit.id is " + currentId);
            System.out.println("Edit button clicked");

//            update the event from database
            try {
                model.updateEvent(name, date, hours, minutes, venue, organizer, currentId);

//                refresh the table
                refreshData();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("add.id is " + currentId);
            System.out.println("Add button clicked");

//            add the event to database
            try {
                model.addEvent(name, date, hours, minutes, venue, organizer);

//                refresh the table
                refreshData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

//            MyEvent newEvent = new MyEvent(name, date, hours, minutes, venue);
//
////            add to events list
//            myEvents.add(newEvent);
        }

        System.out.println("Hours: " + hoursField.getText());
        System.out.println("Minutes: " + minutesField.getText());
        System.out.println("Name: " + nameField.getText());
        System.out.println("Date: " + datePicker.getValue());
        clearFields();
    }


//    synchronize the events list with the database
    private void refreshData() throws SQLException {
            myEvents = model.getAllEvents();
            eventsTable.setItems(myEvents);
    }
//
//    private void updateEvents(MyEvent event) {
//
//        for(int i = 0; i < myEvents.size(); i++) {
//            if (myEvents.get(i).getId() == currentId){
//                myEvents.set(i, event);
//                break;
//            }
//        }
//    }

    @FXML private void handleCancelButton(ActionEvent event) {
        System.out.println("Cancel button clicked");
        clearFields();
    }


}
