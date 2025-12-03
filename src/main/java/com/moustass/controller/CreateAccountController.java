package com.moustass.controller;

import com.moustass.model.User;
import com.moustass.service.CreateAccountService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateAccountController {
    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Button createBtn;

    private final CreateAccountService service = new CreateAccountService();
    private Integer performedByUserId = null;

    @FXML
    protected void onCreate() {
        try {
            User u = service.createAccount(firstname.getText(), lastname.getText(), username.getText(), password.getText(), confirmPassword.getText(), performedByUserId);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Compte créé");
            a.setHeaderText(null);
            a.setContentText("Utilisateur '" + u.getUsername() + "' créé (id=" + u.getId() + ").");
            a.showAndWait();
            // clear fields
            firstname.clear(); lastname.clear(); username.clear(); password.clear(); confirmPassword.clear();
        } catch (IllegalArgumentException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Erreur");
            a.setHeaderText(null);
            a.setContentText(e.getMessage());
            a.showAndWait();
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Erreur");
            a.setHeaderText(null);
            a.setContentText("Erreur interne: " + e.getMessage());
            a.showAndWait();
        }
    }

    public void setPerformedBy(Integer userId) {
        this.performedByUserId = userId;
    }
}
