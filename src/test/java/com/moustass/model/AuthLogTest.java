package com.moustass.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AuthLogTest {
    @Test
    void defaultConstructor_shouldCreateEmptyObject() {
        AuthLog log = new AuthLog();

        assertNotNull(log);
        assertNull(log.getId());
        assertNull(log.getUserId());
        assertNull(log.getEvent());
        assertNull(log.getIpAddress());
        assertNull(log.getCreatedAt());
    }

    @Test
    void parameterizedConstructor_shouldSetAllFields() {
        LocalDateTime now = LocalDateTime.now();

        AuthLog log = new AuthLog(
                1,
                10,
                AuthEvent.SUCCESS,
                "127.0.0.1",
                now
        );

        assertAll(
                () -> assertEquals(1, log.getId()),
                () -> assertEquals(10, log.getUserId()),
                () -> assertEquals(AuthEvent.SUCCESS, log.getEvent()),
                () -> assertEquals("127.0.0.1", log.getIpAddress()),
                () -> assertEquals(now, log.getCreatedAt())
        );
    }

    @Test
    void setters_shouldUpdateValues() {
        LocalDateTime now = LocalDateTime.now();
        AuthLog log = new AuthLog();

        log.setId(5);
        log.setUserId(20);
        log.setEvent(AuthEvent.FAIL);
        log.setIpAddress("192.168.1.10");
        log.setCreatedAt(now);

        assertAll(
                () -> assertEquals(5, log.getId()),
                () -> assertEquals(20, log.getUserId()),
                () -> assertEquals(AuthEvent.FAIL, log.getEvent()),
                () -> assertEquals("192.168.1.10", log.getIpAddress()),
                () -> assertEquals(now, log.getCreatedAt())
        );
    }
}