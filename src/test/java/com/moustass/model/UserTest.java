package com.moustass.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void defaultConstructor_shouldCreateEmptyUser() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getFirstname());
        assertNull(user.getLastname());
        assertNull(user.getUsername());
        assertNull(user.getPasswordHash());
        assertNull(user.getSalt());
        assertNull(user.getPkPublic());
        assertNull(user.getSkPrivate());
        assertNull(user.getMustChangePwd());
        assertNull(user.getIsAdmin());
        assertNull(user.getCreatedAt());
    }

    @Test
    void parameterizedConstructor_shouldSetAllFields() {
        LocalDateTime now = LocalDateTime.now();

        User user = new User(
                1,
                "John",
                "Doe",
                "jdoe",
                "hash123",
                "saltXYZ",
                "publicKey",
                "privateKey",
                true,
                false,
                now
        );

        assertAll(
                () -> assertEquals(1, user.getId()),
                () -> assertEquals("John", user.getFirstname()),
                () -> assertEquals("Doe", user.getLastname()),
                () -> assertEquals("jdoe", user.getUsername()),
                () -> assertEquals("hash123", user.getPasswordHash()),
                () -> assertEquals("saltXYZ", user.getSalt()),
                () -> assertEquals("publicKey", user.getPkPublic()),
                () -> assertEquals("privateKey", user.getSkPrivate()),
                () -> assertTrue(user.getMustChangePwd()),
                () -> assertFalse(user.getIsAdmin()),
                () -> assertEquals(now, user.getCreatedAt())
        );
    }

    @Test
    void setters_shouldUpdateAllFields() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();

        user.setId(10);
        user.setFirstname("Alice");
        user.setLastname("Smith");
        user.setUsername("asmith");
        user.setPasswordHash("hash456");
        user.setSalt("saltABC");
        user.setPkPublic("pk");
        user.setSkPrivate("sk");
        user.setMustChangePwd(false);
        user.setIsAdmin(true);
        user.setCreatedAt(now);

        assertAll(
                () -> assertEquals(10, user.getId()),
                () -> assertEquals("Alice", user.getFirstname()),
                () -> assertEquals("Smith", user.getLastname()),
                () -> assertEquals("asmith", user.getUsername()),
                () -> assertEquals("hash456", user.getPasswordHash()),
                () -> assertEquals("saltABC", user.getSalt()),
                () -> assertEquals("pk", user.getPkPublic()),
                () -> assertEquals("sk", user.getSkPrivate()),
                () -> assertFalse(user.getMustChangePwd()),
                () -> assertTrue(user.getIsAdmin()),
                () -> assertEquals(now, user.getCreatedAt())
        );
    }
}