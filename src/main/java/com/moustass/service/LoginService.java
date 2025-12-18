package com.moustass.service;

import com.moustass.exception.DatabaseConnectionException;
import com.moustass.exception.SignatureRSAException;
import com.moustass.model.ActivityLog;
import com.moustass.model.AuthEvent;
import com.moustass.model.AuthLog;
import com.moustass.repository.AuthLogRepository;
import com.moustass.utils.CryptoUtils;
import com.moustass.model.User;
import com.moustass.repository.ActivityLogRepository;
import com.moustass.repository.UserRepository;
import com.moustass.utils.ValidatorUtils;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Service responsible for user authentication and password management.
 * <p>
 * This class handles login operations, authentication validation,
 * password updates on first login, and records related security
 * and activity logs.
 * </p>
 */
public class LoginService {
    private final UserRepository userRepository = new UserRepository();
    private final ActivityLogRepository activityLogRepository = new ActivityLogRepository();
    private final AuthLogRepository authLogRepository = new AuthLogRepository();

    /**
     * Authenticates a user using their credentials.
     * <p>
     * This method validates the provided username and password,
     * records authentication success or failure, and logs the
     * corresponding user activity.
     * </p>
     *
     * @param username the username provided for authentication
     * @param password the password provided for authentication
     * @return the authenticated {@link User} if credentials are valid
     * @throws IllegalArgumentException if authentication fails
     */
    public User authenticate(String username, String password) {
        try {
            User u = userRepository.findByUsername(username);
            if (u == null) {
                ActivityLog a = new ActivityLog();
                a.setUserId(null);
                a.setAction(ActivityLog.TypeAction.LOGIN_FAILURE);
                a.setDetails("Failed login attempt for username='" + username + "' (user not found)");
                activityLogRepository.insert(a);

                return null;
            }

            String salt = u.getSalt() != null ? u.getSalt() : "";
            String pk = u.getPkPublic() != null ? u.getPkPublic() : "";
            String sk = u.getSkPrivate() != null ? u.getSkPrivate() : "";

            String computed = CryptoUtils.sha256Hex(salt + password + pk + sk);
            boolean isLogged = computed.equals(u.getPasswordHash());
            ActivityLog a = new ActivityLog();
            a.setUserId(u.getId());
            a.setAction(isLogged ? ActivityLog.TypeAction.LOGIN_SUCCESS : ActivityLog.TypeAction.LOGIN_FAILURE);
            String message = isLogged ?
                    "User '" + username + "' logged in successfully" :
                    "Failed login attempt for username='" + username + "' (incorrect password)";
            a.setDetails(message);

            activityLogRepository.insert(a);
            return isLogged ? u : null;
        } catch (SQLException logEx) {
            throw new DatabaseConnectionException("Login failure: " + logEx.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Authentication error: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e){
            throw new SignatureRSAException("Erreur RSA: " + e.getMessage());
        }
    }

    /**
     * Updates the user's password during the first login process.
     * <p>
     * This method enforces password change when the user is required
     * to update credentials upon initial authentication.
     * </p>
     *
     * @param userId      the identifier of the user
     * @param newPassword the new password to be set
     */
    public void changePasswordFirstLogin(Integer userId, String newPassword) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }

        if (newPassword == null || newPassword.length() < 12) {
            throw new IllegalArgumentException("Password is weak");
        }
        User u = userRepository.findById(userId);
        if (u == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (!Boolean.TRUE.equals(u.getMustChangePwd())) {
            throw new IllegalArgumentException("Password change not required");
        }
        ValidatorUtils.validatePasswordRules(newPassword);

        try {
            String salt = CryptoUtils.generateSalt(16);
            String hashPassword = CryptoUtils.hashPassword(salt, newPassword,u.getPkPublic(),u.getSkPrivate());
            u.setSalt(salt);
            u.setPasswordHash(hashPassword);
            u.setMustChangePwd(Boolean.FALSE);
            userRepository.updatePassword(u);
            AuthLog log = new AuthLog();
            log.setUserId(userId);
            log.setEvent(AuthEvent.SUCCESS);
            authLogRepository.insert(log);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error: " + e.getMessage());
        } catch (SQLException ex){
            throw new DatabaseConnectionException("Error db: " + ex.getMessage());
        }
    }
}
