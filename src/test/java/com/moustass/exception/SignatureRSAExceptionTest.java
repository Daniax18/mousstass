package com.moustass.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignatureRSAExceptionTest {
    @Test
    void constructor_shouldSetMessage() {
        String message = "RSA signature failed";

        SignatureRSAException exception =
                new SignatureRSAException(message);

        assertEquals(message, exception.getMessage());
    }
}