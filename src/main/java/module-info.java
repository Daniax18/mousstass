module com.moustass {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens com.moustass to javafx.fxml;
    exports com.moustass;
    exports com.moustass.config;
    opens com.moustass.config to javafx.fxml;
}