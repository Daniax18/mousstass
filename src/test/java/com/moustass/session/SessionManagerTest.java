package com.moustass.session;

import com.moustass.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {
    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("testUser");
        return user;
    }

    @AfterEach
    void cleanSession() {
        SessionManager.logout();
    }

    @Test
    void getCurrentUser_initially_shouldReturnNull() {
        assertNull(SessionManager.getCurrentUser());
    }

    @Test
    void isLoggedIn_initially_shouldReturnFalse() {
        assertFalse(SessionManager.isLoggedIn());
    }

    @Test
    void login_shouldSetCurrentUser() {
        User user = createUser();

        SessionManager.login(user);

        assertEquals(user, SessionManager.getCurrentUser());
    }

    @Test
    void login_shouldSetLoggedInState() {
        SessionManager.login(createUser());

        assertTrue(SessionManager.isLoggedIn());
    }

    @Test
    void getCurrentUserId_whenLoggedIn_shouldReturnUserId() {
        User user = createUser();
        SessionManager.login(user);

        assertEquals(1, SessionManager.getCurrentUserId());
    }

    @Test
    void getCurrentUsername_whenLoggedIn_shouldReturnUsername() {
        User user = createUser();
        SessionManager.login(user);

        assertEquals("testUser", SessionManager.getCurrentUsername());
    }

    @Test
    void logout_shouldClearSession() {
        SessionManager.login(createUser());

        SessionManager.logout();

        assertAll(
                () -> assertNull(SessionManager.getCurrentUser()),
                () -> assertFalse(SessionManager.isLoggedIn()),
                () -> assertNull(SessionManager.getCurrentUserId()),
                () -> assertNull(SessionManager.getCurrentUsername())
        );
    }
}