module com.example.management {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.mail;

    opens com.example.management to javafx.fxml;
    exports com.example.management;
}