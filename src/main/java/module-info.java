module com.moustass {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.moustass to javafx.fxml;
    exports com.moustass;
}