package com.moustass.session;

import com.moustass.model.User;
import com.moustass.repository.UserRepository;

public final class SessionManager {
    private static User currentUser;

    private SessionManager() {}

    public static void login(User user) {
        currentUser = user;
    }

    // A ENLEVER CETTE FONCTION APRES INTEGRATION
    public static void setCurrentUser(int id){
        UserRepository userRepository = new UserRepository();
        User getCurrent = userRepository.findById(id);
        SessionManager.login(getCurrent);
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static Integer getCurrentUserId() {
        return isLoggedIn() ? currentUser.getId() : null;
    }

    public static String getCurrentUsername() {
        return isLoggedIn() ? currentUser.getUsername() : null;
    }
}
