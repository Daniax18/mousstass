package com.moustass.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InitializeDataExceptionTest {
    @Test
    void constructor_shouldSetMessage() {
        String message = "Error while initializing data";

        InitializeDataException exception =
                new InitializeDataException(message);

        assertEquals(message, exception.getMessage());
    }

}