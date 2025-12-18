package com.moustass.session;

import com.moustass.model.User;
import com.moustass.repository.UserRepository;

/**
 * Manages the current user session.
 * <p>
 * This class provides a simple session management mechanism by storing
 * the currently authenticated user in memory. It is designed to be used
 * in a static context and cannot be instantiated.
 * </p>
 */
public final class SessionManager {
    private static User currentUser;

    private SessionManager() {}

    /**
     * Registers a user as the currently authenticated user.
     * @param user the authenticated user to store in the session
     */
    public static void login(User user) {
        currentUser = user;
    }

    /**
     * Clears the current user session.
     * <p>
     * After this call, no user is considered authenticated.
     * </p>
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * Returns the currently authenticated user.
     * @return the current {@link User}, or {@code null} if no user is logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks whether a user is currently authenticated.
     * @return {@code true} if a user is logged in, {@code false} otherwise
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Returns the identifier of the currently authenticated user.
     * @return the user identifier, or {@code null} if no user is logged in
     */
    public static Integer getCurrentUserId() {
        return isLoggedIn() ? currentUser.getId() : null;
    }

    /**
     * Returns the username of the currently authenticated user.
     * @return the username, or {@code null} if no user is logged in
     */
    public static String getCurrentUsername() {
        return isLoggedIn() ? currentUser.getUsername() : null;
    }
}
