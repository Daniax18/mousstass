module com.moustass {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires javafx.graphics;

    opens com.moustass to javafx.fxml;
    exports com.moustass;
    exports com.moustass.config;
    opens com.moustass.config to javafx.fxml;
    opens com.moustass.controller to javafx.fxml;
    exports com.moustass.controller;
}