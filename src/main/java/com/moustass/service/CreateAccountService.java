package com.moustass.service;

import com.moustass.exception.DatabaseConnectionException;
import com.moustass.exception.SignatureRSAException;
import com.moustass.model.User;
import com.moustass.model.ActivityLog;
import com.moustass.repository.UserRepository;
import com.moustass.repository.ActivityLogRepository;
import com.moustass.utils.CryptoUtils;
import java.security.*;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CreateAccountService {
    private final UserRepository userRepository = new UserRepository();
    private final ActivityLogRepository activityLogRepository = new ActivityLogRepository();

    public User createAccount(String firstname, String lastname, String username, String password, String confirmPassword, Integer performedByUserId, boolean adminVerified)
            throws IllegalArgumentException, NoSuchAlgorithmException{
        if (firstname == null) firstname = "";
        if (lastname == null) lastname = "";
        if (username == null || username.trim().isEmpty()) throw new IllegalArgumentException("username required");
        if (password == null || !password.equals(confirmPassword)) throw new IllegalArgumentException("Passwords do not match");

        try {
            // ensure username not already used
            if (userRepository.findByUsername(username) != null) throw new IllegalArgumentException("username already exists");

            // if the creator is an admin, require adminVerified == true
            if (performedByUserId != null) {
                com.moustass.model.User performer = userRepository.findById(performedByUserId);
                if (performer != null && Boolean.TRUE.equals(performer.getIsAdmin()) && !adminVerified) {
                    throw new IllegalArgumentException("DOUBLE_VERIFICATION_REQUIRED: Admin must confirm creation");
                }
            }

            // validate password rules
            validatePasswordRules(password);

            // generate salt
            String salt = CryptoUtils.generateSalt(16);

            // generate keypair
            KeyPair kp = CryptoUtils.generateKeyPair();
            String pkPublic = CryptoUtils.encodePublicKey(kp.getPublic());
            String skPrivate = CryptoUtils.encodePrivateKey(kp.getPrivate());

            // hash password using same scheme as InitialData
            String passwordHash = CryptoUtils.sha256Hex(salt + password + pkPublic + skPrivate);

            User u = new User();
            u.setFirstname(firstname);
            u.setLastname(lastname);
            u.setUsername(username);
            u.setPasswordHash(passwordHash);
            u.setSalt(salt);
            u.setPkPublic(pkPublic);
            u.setSkPrivate(skPrivate);
            u.setMustChangePwd(Boolean.TRUE);
            u.setIsAdmin(Boolean.FALSE);
            u.setCreatedAt(LocalDateTime.now());

            boolean ok = userRepository.insert(u);
            if (!ok) throw new DatabaseConnectionException("Failed to insert user");

            // log activity
            ActivityLog a = new ActivityLog();
            a.setUserId(performedByUserId);
            a.setAction(ActivityLog.TypeAction.USER_CREATED);
            a.setDetails("Created user '" + username + "' (id=" + u.getId() + ")");
            a.setCreatedAt(LocalDateTime.now());
            activityLogRepository.insert(a);

            return u;
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Error : " + ex.getMessage());
        } catch (NoSuchAlgorithmException ex){
            throw new SignatureRSAException("Error RSA : " + ex.getMessage());
        }catch (SQLException ex){
            throw new DatabaseConnectionException("Error DB : " + ex.getMessage());
        }
    }

    public void validatePasswordRules(String password) throws IllegalArgumentException{
        StringBuilder errors = new StringBuilder();
        if (password == null) {
            errors.append("Password required. ");
        } else {
            boolean hasUpper = false;
            boolean hasLower = false;
            boolean hasDigit = false;
            boolean hasSpecial = false;

            for (char c : password.toCharArray()) {
                if (Character.isUpperCase(c)) hasUpper = true;
                else if (Character.isLowerCase(c)) hasLower = true;
                else if (Character.isDigit(c)) hasDigit = true;
                else hasSpecial = true;
            }

            if (!hasUpper) errors.append("Au moins une majuscule requise. ");
            if (!hasLower) errors.append("Au moins une minuscule requise. ");
            if (!hasDigit) errors.append("Au moins un chiffre requis. ");
            if (!hasSpecial) errors.append("Au moins un caractère spécial requis. ");
        }
        if (!errors.isEmpty()) throw new IllegalArgumentException(errors.toString().trim());
    }
}
