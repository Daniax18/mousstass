package com.moustass.config;

import com.moustass.model.User;
import com.moustass.repository.UserRepository;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.time.LocalDateTime;

public class InitialData {
    private static final UserRepository userRepository = new UserRepository();

    public static void initDefaultAdmin() {
        try {
            String adminUsername = "admin";
            // Do not recreate if exists
            User existing = userRepository.findByUsername(adminUsername);
            if (existing != null) {
                System.out.println("Default admin already exists, skipping creation.");
                return;
            }

            String plainPassword = "Admin@123";

            // generate salt
            String salt = generateSalt(16);

            // generate key pair (RSA)
            KeyPair kp = generateKeyPair();
            String pkPublic = encodePublicKey(kp.getPublic());
            String skPrivate = encodePrivateKey(kp.getPrivate());

            // hash password with SHA-256 over (salt + password + pk + sk)
            String passwordHash = sha256Hex(salt + plainPassword + pkPublic + skPrivate);

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

            boolean ok = userRepository.insert(admin);
            if (ok) System.out.println("Default admin created with username='" + adminUsername + "' and temporary password='" + plainPassword + "'.");
            else System.out.println("Failed to create default admin.");

        } catch (Exception e) {
            System.err.println("Error while creating default admin: " + e.getMessage());
        }
    }

    private static String generateSalt(int length) {
        byte[] b = new byte[length];
        new SecureRandom().nextBytes(b);
        return Base64.getEncoder().encodeToString(b);
    }

    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }

    private static String encodePublicKey(PublicKey pk) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pk.getEncoded());
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    private static String encodePrivateKey(PrivateKey sk) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(sk.getEncoded());
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    private static String sha256Hex(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(input.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
