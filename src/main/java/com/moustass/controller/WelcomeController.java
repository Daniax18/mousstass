package com.moustass.controller;

import com.moustass.exception.FileStorageException;
import com.moustass.service.SignatureLogService;
import com.moustass.session.SessionManager;
import com.moustass.view.SignatureView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

public class WelcomeController {
    @FXML
    public Label userConnected;

    @FXML
    public TextField nameFile;

    @FXML
    public Button btnChooseFile;

    @FXML
    public Button btnSign;

    private File selectedFile;
    private String currentFileName;

    // Table
    @FXML
    TableView<SignatureView> tableSignature;

    @FXML
    TableColumn<SignatureView, LocalDateTime> dateSign;

    @FXML
    TableColumn<SignatureView, String> fileNameSignature;

    @FXML
    TableColumn<SignatureView, String> userNameSignature;

    @FXML
    private TableColumn<SignatureView, Void> actionColumn;

    private final ObservableList<SignatureView> signatures = FXCollections.observableArrayList();

    // Service
    private final SignatureLogService signatureLogService = new SignatureLogService();

    public void initialize() {
        // Show the user connected
        userConnected.setText(SessionManager.getCurrentUsername());

        // Initialise column name
        dateSign.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDateSignature()));

        fileNameSignature.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getFileName()));

        userNameSignature.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getUserName()));

        addButtonToTable();

        loadSignatures();
        tableSignature.setItems(signatures);
    }

    private void verifyFile(int idSignature) {
        if(signatureLogService.isFileOk(idSignature)){
            showAlert("Fichier ok !", Alert.AlertType.INFORMATION);
        }else{
            showAlert("Fichier corrompu !", Alert.AlertType.ERROR);
        }
    }

    public void donwloadFile(int idSignature){
        File sourceFile = signatureLogService.fileToDownload(idSignature);

        if (!sourceFile.exists()) {
            showAlert("Fichier introuvable !!", Alert.AlertType.ERROR);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier");

        // nom proposé par défaut
        fileChooser.setInitialFileName(sourceFile.getName());

        Window window = tableSignature.getScene().getWindow();
        File destinationFile = fileChooser.showSaveDialog(window);

        if (destinationFile == null) {
            return; // canceled
        }

        try {
            Files.copy(
                    sourceFile.toPath(),
                    destinationFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            showAlert("Téléchargement terminé ✔", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Erreur lors du téléchargement : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onChooseFile() {
        Window window = btnChooseFile.getScene().getWindow();

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choisir un fichier à signer");

        selectedFile = chooser.showOpenDialog(window);

        if (selectedFile != null) {
            currentFileName = selectedFile.getName();
            nameFile.setText(currentFileName);
        }
    }

    @FXML
    public void onSaveFile() {
        if (currentFileName == null) {
            showAlert("No file selected", Alert.AlertType.ERROR);
            return;
        }

        try {
            this.signatureLogService.saveFile(selectedFile);
            refreshTable();
            showAlert("file saved", Alert.AlertType.INFORMATION);
            selectedFile = null;
            nameFile.setText("");
        }catch (FileStorageException ex){
            showAlert(ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String msg, Alert.AlertType typeAlert) {
        Alert alert = new Alert(typeAlert);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void loadSignatures(){
        signatures.addAll(
                signatureLogService.findAllSignatures()
        );
    }

    private void addButtonToTable() {
        Callback<TableColumn<SignatureView, Void>, TableCell<SignatureView, Void>> cellFactory =
                new Callback<TableColumn<SignatureView, Void>, TableCell<SignatureView, Void>>() {
                    @Override
                    public TableCell<SignatureView, Void> call(final TableColumn<SignatureView, Void> param) {
                        final TableCell<SignatureView, Void> cell;
                        cell = new TableCell<SignatureView, Void>() {

                            private final Button btnVerify = new Button("Vérifier");
                            private final Button btnDonwload = new Button("Telecharger");


                            private final HBox hbox = new HBox(30); // spacing

                            {
                                hbox.setAlignment(Pos.CENTER);
                                hbox.getChildren().addAll(btnVerify, btnDonwload);

                                btnDonwload.setOnAction(event -> {
                                    SignatureView signature = getTableView().getItems().get(getIndex());
                                    System.out.println("Télécharger ID: " + signature.getIdSignature());

                                    donwloadFile(signature.getIdSignature());
                                });

                                btnVerify.setOnAction(event -> {
                                    SignatureView signature = getTableView().getItems().get(getIndex());
                                    System.out.println("Vérifier ID: " + signature.getIdSignature());

                                   verifyFile(signature.getIdSignature());
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setAlignment(Pos.CENTER);
                                    setGraphic(hbox);
                                }
                            }
                        };
                        return cell;
                    }
                };
        actionColumn.setCellFactory(cellFactory);
    }


    private void refreshTable() {
        ObservableList<SignatureView> data = tableSignature.getItems();

        data.clear();
        loadSignatures();
        tableSignature.setItems(signatures);
    }
}
