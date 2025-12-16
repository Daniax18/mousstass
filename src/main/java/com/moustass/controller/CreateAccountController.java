package com.moustass.controller;

import com.moustass.model.User;
import com.moustass.repository.UserRepository;
import com.moustass.service.CreateAccountService;
import com.moustass.session.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
            boolean adminVerified = false;
            if (performedByUserId != null) {
                User performer = new UserRepository().findById(performedByUserId);
                if (performer != null && Boolean.TRUE.equals(performer.getIsAdmin())) {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Double vérification requise");
                    confirm.setHeaderText(null);
                    confirm.setContentText("Vous créez un compte en tant qu'Admin. Confirmer la création ?");
                    java.util.Optional<javafx.scene.control.ButtonType> res = confirm.showAndWait();
                    if (res.isPresent() && res.get() == javafx.scene.control.ButtonType.OK) {
                        adminVerified = true;
                    } else {
                        return; // abort creation
                    }
                }
            }

            User u = service.createAccount(firstname.getText(), lastname.getText(), username.getText(), password.getText(), confirmPassword.getText(), performedByUserId, adminVerified);
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

    public void rollBack(MouseEvent mouseEvent) throws IOException {
        SessionManager.logout();

        java.net.URL fxmlUrl = getClass().getResource("/com/moustass/login-view.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 850, 575));
        stage.setTitle("Moustass");
    }
}
