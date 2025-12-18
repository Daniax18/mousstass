package com.moustass.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageExceptionTest {
    @Test
    void constructor_shouldSetMessage() {
        String message = "File storage error";

        FileStorageException exception =
                new FileStorageException(message);

        assertEquals(message, exception.getMessage());
    }
}