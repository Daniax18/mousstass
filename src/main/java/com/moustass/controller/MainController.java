package com.moustass.controller;

import com.moustass.model.User;
import com.moustass.service.LoginService;
import com.moustass.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MainController {
    @FXML
    private TextField userName;

    @FXML
    private PasswordField mdp;

    private final LoginService loginService = new LoginService();

    public void onClicked(ActionEvent actionEvent) {
        String username = userName.getText();
        String password = mdp.getText();

        try {
            User u = loginService.authenticate(username, password);
            if (u == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Échec de connexion");
                a.setHeaderText(null);
                a.setContentText("Nom d'utilisateur ou mot de passe invalide.");
                a.showAndWait();
                return;
            }else{
                SessionManager.login(u);
            }
            SessionManager.login(u);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            if (Boolean.TRUE.equals(u.getMustChangePwd())) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/moustass/first-login.fxml")
                );
                Parent root = loader.load();

                FirstLoginController ctrl = loader.getController();
                ctrl.setUserId(u.getId());

                stage.setScene(new Scene(root, 850, 575));
                stage.setTitle("First login - Change password");
                return;
            }

            boolean isAdmin = (u.getIsAdmin() != null && u.getIsAdmin());
            if (isAdmin) {
                // load create-account view with better diagnostics
                java.net.URL fxmlUrl = getClass().getResource("/com/moustass/create-account.fxml");
                if (fxmlUrl == null) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Erreur");
                    a.setHeaderText(null);
                    a.setContentText("Impossible d'ouvrir la création de compte: ressource FXML introuvable '/com/moustass/create-account.fxml'.");
                    a.showAndWait();
                    return;
                }

                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();
                CreateAccountController ctrl = loader.getController();
                ctrl.setPerformedBy(u.getId());
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 850, 575));
                stage.setTitle("Créer un compte");
            } else {
                java.net.URL fxmlUrl = getClass().getResource("/com/moustass/welcome-page.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 850, 575));
                stage.setTitle("Home page");
            }
        } catch (IllegalArgumentException | IOException ex){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Erreur");
            a.setHeaderText(null);
            a.setContentText("Erreur interne: " + ex.toString());
            a.showAndWait();
        }
    }
}
