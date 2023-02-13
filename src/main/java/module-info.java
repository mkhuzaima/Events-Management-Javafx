module com.example.management {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.management to javafx.fxml;
    exports com.example.management;
}