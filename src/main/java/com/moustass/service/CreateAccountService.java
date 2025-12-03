package com.moustass.service;

import com.moustass.model.User;
import com.moustass.model.ActivityLog;
import com.moustass.repository.UserRepository;
import com.moustass.repository.ActivityLogRepository;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;

public class CreateAccountService {
    private final UserRepository userRepository = new UserRepository();
    private final ActivityLogRepository activityLogRepository = new ActivityLogRepository();

    public User createAccount(String firstname, String lastname, String username, String password, String confirmPassword, Integer performedByUserId) {
        if (firstname == null) firstname = "";
        if (lastname == null) lastname = "";
        if (username == null || username.trim().isEmpty()) throw new IllegalArgumentException("username required");
        if (password == null || !password.equals(confirmPassword)) throw new IllegalArgumentException("Passwords do not match");

        try {
            // ensure username not already used
            if (userRepository.findByUsername(username) != null) throw new IllegalArgumentException("username already exists");

            // generate salt
            String salt = generateSalt(16);

            // generate keypair
            KeyPair kp = generateKeyPair();
            String pkPublic = encodePublicKey(kp.getPublic());
            String skPrivate = encodePrivateKey(kp.getPrivate());

            // hash password using same scheme as InitialData
            String passwordHash = sha256Hex(salt + password + pkPublic + skPrivate);

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
            if (!ok) throw new RuntimeException("Failed to insert user");

            // log activity
            ActivityLog a = new ActivityLog();
            a.setUserId(performedByUserId);
            a.setAction("CREATE_USER");
            a.setDetails("Created user '" + username + "' (id=" + u.getId() + ")");
            a.setCreatedAt(LocalDateTime.now());
            activityLogRepository.insert(a);

            return u;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateSalt(int length) {
        byte[] b = new byte[length];
        new SecureRandom().nextBytes(b);
        return Base64.getEncoder().encodeToString(b);
    }

    private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }

    private String encodePublicKey(PublicKey pk) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pk.getEncoded());
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    private String encodePrivateKey(PrivateKey sk) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(sk.getEncoded());
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    private String sha256Hex(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(input.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
