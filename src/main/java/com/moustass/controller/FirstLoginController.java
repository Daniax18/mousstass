package com.moustass.controller;

import com.moustass.service.LoginService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX controller responsible for user first login.
 */
public class FirstLoginController {
    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField confirmPassword;
    private Integer userId;

    private final LoginService loginService = new LoginService();

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void onClicked(ActionEvent actionEvent) throws IOException {
        if (userId == null) {
            throw new IllegalArgumentException("User not found");
        }
        String newPass = this.newPassword.getText();
        String confirmPass = this.confirmPassword.getText();

        if (newPass == null || newPass.isBlank()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setContentText("Password required");
            a.showAndWait();
            return;
        }
        if (!newPass.equals(confirmPass)) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setContentText("Password not the same");
            a.showAndWait();
            return;
        }
        try {
            loginService.changePasswordFirstLogin(userId, newPass);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(null);
            a.setContentText("Password changed, login again");
            a.showAndWait();
            goToLogin(actionEvent);
        } catch (IllegalArgumentException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Invalid password");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }

    private void goToLogin(ActionEvent actionEvent) throws IOException {
        java.net.URL fxmlUrl = getClass().getResource("/com/moustass/login-view.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 850, 575));
        stage.setTitle("Moustass");
    }

}