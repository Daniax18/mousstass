package com.moustass.config;

import com.moustass.exception.InitializeDataException;
import com.moustass.model.User;
import com.moustass.repository.UserRepository;
import com.moustass.utils.CryptoUtils;

import java.security.*;
import java.time.LocalDateTime;

/**
 * Application initial data loader.
 * <p>
 * This class is responsible for initializing mandatory system data
 * at application startup, such as the default administrator account (if not exist).
 * </p>
 */
public class InitialData {
    private static final UserRepository userRepository = new UserRepository();

    /**
     * Private constructor to prevent instantiation.
     */
    private InitialData(){}

    /**
     * Initializes the default administrator account.
     * <p>
     * If an administrator with the predefined username already exists,
     * the method exits without performing any operation.
     * </p>
     * @throws InitializeDataException if a cryptographic error occurs
     *                                 during initialization
     */
    public static void initDefaultAdmin() {
        try {
            String adminUsername = "admin";
            // Do not recreate if exists
            User existing = userRepository.findByUsername(adminUsername);
            if (existing != null) {
                return;
            }

            String plainPassword = AppConfig.getInstance().getProperty("admin.mdp");

            // generate salt
            String salt = CryptoUtils.generateSalt(16);

            // generate key pair (RSA)
            KeyPair kp = CryptoUtils.generateKeyPair();
            String pkPublic = CryptoUtils.encodePublicKey(kp.getPublic());
            String skPrivate = CryptoUtils.encodePrivateKey(kp.getPrivate());

            // hash password with SHA-256 over (salt + password + pk + sk)
            String passwordHash = CryptoUtils.sha256Hex(salt + plainPassword + pkPublic + skPrivate);

            User admin = new User();
            admin.setFirstname("System");
            admin.setLastname("Administrator");
            admin.setUsername(adminUsername);
            admin.setPasswordHash(passwordHash);
            admin.setSalt(salt);
            admin.setPkPublic(pkPublic);
            admin.setSkPrivate(skPrivate);
            admin.setMustChangePwd(Boolean.FALSE);
            admin.setIsAdmin(Boolean.TRUE);
            admin.setCreatedAt(LocalDateTime.now());

            userRepository.insert(admin);
        } catch (NoSuchAlgorithmException e) {
            throw new InitializeDataException("Error : " +e.getMessage());
        }
    }
}
