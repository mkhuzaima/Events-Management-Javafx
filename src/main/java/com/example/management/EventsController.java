package com.example.management;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class EventsController implements Initializable {

//    button
    @FXML private Button actionButton;

    int currentId = -1;




    @FXML private TextField hoursField;
    @FXML private TextField minutesField;
    @FXML private TextField venueField;
    @FXML private ComboBox<String> nameComboBox;
    @FXML private DatePicker datePicker;
    @FXML private TableColumn<MyEvent, String> nameColumn;

    @FXML private TableColumn<MyEvent, String> venueColumn;
    @FXML private TableColumn<MyEvent, String> dateColumn;
    @FXML private TableColumn<MyEvent, String> timeColumn;
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

        // Add some dummy data
        myEvents.add(new MyEvent("Name 1", LocalDateTime.now().toLocalDate(), 10, 30, "New york"));
        myEvents.add(new MyEvent("Name 2", LocalDateTime.now().toLocalDate(), 1, 40, "New york"));
        myEvents.add(new MyEvent("Name 3", LocalDateTime.now().toLocalDate(), 12, 50, "New york"));



//        occupy full width
        eventsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        eventsTable.setItems(myEvents);
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

        @Override
        void onEvent(MyEvent myEvent) {
            // Implement your edit event logic here
            System.out.println("event name is " + myEvent.getName());
            System.out.println("event venue is " + myEvent.getVenue());

            fillFieldsWith(myEvent);
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
                myEvents.remove(myEvent);
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

    @FXML private void onComboChanged(ActionEvent event) {
        System.out.println("Selected item: " + nameComboBox.getValue());
    }


    private void addListenerToField(TextField field, int x) {
        field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            System.out.println("observableValue: " + observableValue);
//                remove non-digit characters
            if (newValue == null || newValue.isEmpty()) {
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
        fillFieldsWith(0, 0, null, null, "");
        currentId = -1;
    }

    private void fillFieldsWith(MyEvent event){
        actionButton.setText("Edit");
        fillFieldsWith(event.getHours(), event.getMinutes(), event.getName(), event.getDate(), event.getVenue());
        currentId = event.getId();
    }

    private void fillFieldsWith(int hours, int minutes, String name, LocalDate date, String venue) {
        hoursField.setText(String.valueOf(hours));
        minutesField.setText(String.valueOf(minutes));
        nameComboBox.setValue(name);
        datePicker.setValue(date);
        venueField.setText(venue);

        if (date == null ) {
            datePicker.getEditor().clear();
        }
        else {
            datePicker.getEditor().setText(date.toString());
        }
    }

    @FXML private void handleAddButton(ActionEvent event) {
//        check operation


        String name = nameComboBox.getValue();
        LocalDate date = datePicker.getValue();
        int hours = Integer.parseInt(hoursField.getText());
        int minutes = Integer.parseInt(minutesField.getText());
        String venue = venueField.getText();
        if (actionButton.getText().equals("Edit")) {
            System.out.println("edit.id is " + currentId);
            System.out.println("Edit button clicked");
            MyEvent newEvent = new MyEvent(name, date, hours, minutes, venue, currentId);
            updateEvents(newEvent);
        }
        else {
            System.out.println("add.id is " + currentId);
            System.out.println("Add button clicked");
            MyEvent newEvent = new MyEvent(name, date, hours, minutes, venue);

//            add to events list
            myEvents.add(newEvent);
        }


//            update the table
        eventsTable.setItems(myEvents);

        System.out.println("Hours: " + hoursField.getText());
        System.out.println("Minutes: " + minutesField.getText());
        System.out.println("Name: " + nameComboBox.getValue());
        System.out.println("Date: " + datePicker.getValue());
        clearFields();
    }

    private void updateEvents(MyEvent event) {

        for(int i = 0; i < myEvents.size(); i++) {
            if (myEvents.get(i).getId() == currentId){
                myEvents.set(i, event);
                break;
            }
        }
    }

    @FXML private void handleCancelButton(ActionEvent event) {
        System.out.println("Cancel button clicked");
        clearFields();
    }


}
