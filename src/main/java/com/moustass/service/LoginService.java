package com.moustass.service;

import com.moustass.model.ActivityLog;
import com.moustass.model.User;
import com.moustass.repository.ActivityLogRepository;
import com.moustass.repository.UserRepository;

import java.security.MessageDigest;

public class LoginService {
    private final UserRepository userRepository = new UserRepository();
    private final ActivityLogRepository activityLogRepository = new ActivityLogRepository();

    public User authenticate(String username, String password) {
        try {
            User u = userRepository.findByUsername(username);
            if (u == null) {
                // log failure - user not found
                try {
                    ActivityLog a = new ActivityLog();
                    a.setUserId(null);
                    a.setAction("LOGIN_FAILURE");
                    a.setDetails("Failed login attempt for username='" + username + "' (user not found)");
                    activityLogRepository.insert(a);
                } catch (Exception logEx) {
                    System.err.println("Failed to log login failure: " + logEx.getMessage());
                }
                return null;
            }

            String salt = u.getSalt() != null ? u.getSalt() : "";
            String pk = u.getPkPublic() != null ? u.getPkPublic() : "";
            String sk = u.getSkPrivate() != null ? u.getSkPrivate() : "";

            String computed = sha256Hex(salt + password + pk + sk);
            if (computed.equals(u.getPasswordHash())) {
                // log success
                try {
                    ActivityLog a = new ActivityLog();
                    a.setUserId(u.getId());
                    a.setAction("LOGIN_SUCCESS");
                    a.setDetails("User '" + username + "' logged in successfully");
                    activityLogRepository.insert(a);
                } catch (Exception logEx) {
                    System.err.println("Failed to log login success: " + logEx.getMessage());
                }
                return u;
            } else {
                // log failure - wrong password
                try {
                    ActivityLog a = new ActivityLog();
                    a.setUserId(u.getId());
                    a.setAction("LOGIN_FAILURE");
                    a.setDetails("Failed login attempt for username='" + username + "' (incorrect password)");
                    activityLogRepository.insert(a);
                } catch (Exception logEx) {
                    System.err.println("Failed to log login failure: " + logEx.getMessage());
                }
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Authentication error: " + e.getMessage(), e);
        }
    }

    private String sha256Hex(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(input.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
