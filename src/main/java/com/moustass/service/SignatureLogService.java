package com.moustass.service;

import com.moustass.config.AppConfig;
import com.moustass.exception.DatabaseConnectionException;
import com.moustass.exception.FileStorageException;
import com.moustass.exception.SignatureRSAException;
import com.moustass.model.ActivityLog;
import com.moustass.model.SignatureLog;
import com.moustass.model.User;
import com.moustass.repository.ActivityLogRepository;
import com.moustass.repository.SignatureLogRepository;
import com.moustass.repository.UserRepository;
import com.moustass.session.SessionManager;
import com.moustass.utils.CryptoUtils;
import com.moustass.view.SignatureView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;

public class SignatureLogService {

    private final SignatureLogRepository signatureLogRepository = new SignatureLogRepository();
    private final ActivityLogRepository activityLogRepository = new ActivityLogRepository();
    private final UserRepository userRepository = new UserRepository();

    private static final String UPLOAD_DIR = "upload.dir";

    public boolean isFileOk(int idSignature)
            throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        try {
            SignatureLog signature = signatureLogRepository.findById(idSignature);
            User author = userRepository.findById(signature.getUserId());
            PublicKey pk = CryptoUtils.publicKeyFromBase64(author.getPkPublic());

            AppConfig config = AppConfig.getInstance();
            File fileToVerify = new File(config.getProperty(UPLOAD_DIR) + signature.getFileName());

            return CryptoUtils.verifySha256WithRsa(
                    Files.readAllBytes(fileToVerify.toPath()),
                    CryptoUtils.fromB64(signature.getSignatureValue()),
                    pk
            );
        } catch (IOException | NoSuchAlgorithmException | SignatureException | InvalidKeyException | InvalidKeySpecException e) {
            throw new SignatureRSAException("Error : " + e.getMessage());
        }
    }

    public void saveFile(File fileToSave)
            throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException{
        try {
            savePhysicalFile(fileToSave);
            User currentUser = SessionManager.getCurrentUser();

            // Get the private key
            PrivateKey sk = CryptoUtils.privateKeyFromBase64(currentUser.getSkPrivate());

            // Hash file
            byte[] hash = CryptoUtils.sha256(fileToSave);

            // sign the file
            byte[] signature = CryptoUtils.signSha256WithRsa(
                    Files.readAllBytes(fileToSave.toPath()),
                    sk
            );

            SignatureLog signatureLog = new SignatureLog(
                    currentUser.getId(),
                    fileToSave.getName(),
                    CryptoUtils.b64(hash),
                    CryptoUtils.b64(signature),
                    null);
            signatureLogRepository.insert(signatureLog);

            ActivityLog log = new ActivityLog(currentUser.getId(), ActivityLog.TypeAction.FILE_DOWNLOAD, fileToSave.getName());
            activityLogRepository.insert(log);

            // NON TRANSACTIONEL FUNCTION !!!!!
            // Use Connection => auto commit false
        } catch (IOException | NoSuchAlgorithmException | SignatureException | InvalidKeyException | InvalidKeySpecException e) {
            throw new SignatureRSAException("Error : " +e.getMessage());
        }catch (SQLException ex){
            throw new DatabaseConnectionException("Error Db: " +ex.getMessage());
        }
    }

    public void savePhysicalFile(File fileToSave){
        try {
            AppConfig config = AppConfig.getInstance();
            String savePath = config.getProperty(UPLOAD_DIR);
            Files.createDirectories(Path.of(savePath));

            Path destination = Paths.get(savePath + fileToSave.getName());

            Files.copy(fileToSave.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException ex){
            throw new FileStorageException("Error: " +ex.getMessage());
        }
    }

    public File fileToDownload(int idSignature){
        SignatureLog signature = signatureLogRepository.findById(idSignature);
        AppConfig config = AppConfig.getInstance();
        return new File(config.getProperty(UPLOAD_DIR) + signature.getFileName());
    }

    public List<SignatureView> findAllSignatures(){
        return signatureLogRepository.findAllSignatures();
    }
}
