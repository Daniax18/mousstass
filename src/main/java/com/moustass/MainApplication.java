package com.moustass;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import com.moustass.config.InitialData;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // create default admin if missing
        InitialData.initDefaultAdmin();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Moustass");
        stage.setScene(scene);
        stage.show();
    }
}
