package com.moustass.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthEventTest {
    @Test
    void authEvent_shouldContainExpectedValues() {
        AuthEvent[] values = AuthEvent.values();

        assertEquals(2, values.length);
        assertEquals(AuthEvent.SUCCESS, values[0]);
        assertEquals(AuthEvent.FAIL, values[1]);
    }

    @Test
    void valueOf_shouldReturnCorrectEnum() {
        AuthEvent success = AuthEvent.valueOf("SUCCESS");
        AuthEvent fail = AuthEvent.valueOf("FAIL");

        assertEquals(AuthEvent.SUCCESS, success);
        assertEquals(AuthEvent.FAIL, fail);
    }

    @Test
    void valueOf_invalidValue_shouldThrowException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> AuthEvent.valueOf("UNKNOWN")
        );
    }
}