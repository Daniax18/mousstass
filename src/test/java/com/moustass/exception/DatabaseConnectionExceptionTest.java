package com.moustass.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionExceptionTest {
    @Test
    void constructor_shouldSetMessage() {
        String message = "Unable to connect to database";

        DatabaseConnectionException exception =
                new DatabaseConnectionException(message);

        assertEquals(message, exception.getMessage());
    }
}