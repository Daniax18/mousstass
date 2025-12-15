package com.moustass.service;

import com.moustass.model.User;
import com.moustass.repository.UserRepository;
import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;

public class LoginService {
    private final UserRepository userRepository = new UserRepository();

    public User authenticate(String username, String password) {
        try {
            User u = userRepository.findByUsername(username);
            if (u == null) return null;

            String salt = u.getSalt() != null ? u.getSalt() : "";
            String pk = u.getPkPublic() != null ? u.getPkPublic() : "";
            String sk = u.getSkPrivate() != null ? u.getSkPrivate() : "";

            String computed = sha256Hex(salt + password + pk + sk);
            if (computed.equals(u.getPasswordHash())) return u;
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Authentication error: " + e.getMessage(), e);
        }
    }

    private String sha256Hex(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
