package com.moustass;

import com.moustass.session.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import com.moustass.config.InitialData;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // create default admin if missing
        InitialData.initDefaultAdmin();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/moustass/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 575);
        Image icon = new Image(getClass().getResourceAsStream("/images/mus.png"));
        stage.getIcons().add(icon);

        stage.setTitle("Moustass");
        stage.setScene(scene);
        stage.show();
    }
}
