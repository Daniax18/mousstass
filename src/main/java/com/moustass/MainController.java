package com.moustass;

import com.moustass.model.User;
import com.moustass.service.LoginService;
import com.moustass.controller.CreateAccountController;
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

public class MainController {
    @FXML
    private TextField userName;

    @FXML
    private PasswordField mdp;

    private final LoginService loginService = new LoginService();

    public void onClicked(ActionEvent actionEvent) {
        String username = userName.getText();
        String password = mdp.getText();

        User u = loginService.authenticate(username, password);
        if (u == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Échec de connexion");
            a.setHeaderText(null);
            a.setContentText("Nom d'utilisateur ou mot de passe invalide.");
            a.showAndWait();
            return;
        }

        boolean isAdmin = (u.getIsAdmin() != null && u.getIsAdmin());
        if (isAdmin) {
            // load create-account view with better diagnostics
            try {
                java.net.URL fxmlUrl = getClass().getResource("/com/moustass/create-account.fxml");
                if (fxmlUrl == null) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Erreur");
                    a.setHeaderText(null);
                    a.setContentText("Impossible d'ouvrir la création de compte: ressource FXML introuvable '/com/moustass/create-account.fxml'.");
                    a.showAndWait();
                    System.err.println("FXML resource not found: /com/moustass/create-account.fxml");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();
                CreateAccountController ctrl = loader.getController();
                ctrl.setPerformedBy(u.getId());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 600, 400));
                stage.setTitle("Créer un compte");
            } catch (Exception e) {
                e.printStackTrace();
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Erreur");
                a.setHeaderText(null);
                a.setContentText("Impossible d'ouvrir la création de compte: " + e.toString());
                a.showAndWait();
            }
            return;
        }

        String role = "utilisateur";
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Connexion réussie");
        a.setHeaderText(null);
        a.setContentText("Bienvenue " + u.getFirstname() + " (" + role + ")");
        a.showAndWait();
    }
}
