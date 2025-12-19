package com.moustass.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SignatureLogTest {
    @Test
    void defaultConstructor_shouldCreateEmptyObject() {
        SignatureLog log = new SignatureLog();

        assertNotNull(log);
        assertNull(log.getId());
        assertNull(log.getUserId());
        assertNull(log.getFileName());
        assertNull(log.getFileHash());
        assertNull(log.getSignatureValue());
        assertNull(log.getCreatedAt());
    }

    @Test
    void parameterizedConstructor_shouldSetAllFields() {
        LocalDateTime now = LocalDateTime.now();

        SignatureLog log = new SignatureLog(
                1,
                "file.pdf",
                "hash123",
                "signatureABC",
                now
        );

        assertAll(
                () -> assertEquals(1, log.getUserId()),
                () -> assertEquals("file.pdf", log.getFileName()),
                () -> assertEquals("hash123", log.getFileHash()),
                () -> assertEquals("signatureABC", log.getSignatureValue()),
                () -> assertEquals(now, log.getCreatedAt())
        );
    }
    @Test
    void setters_shouldUpdateValues() {
        LocalDateTime now = LocalDateTime.now();
        SignatureLog log = new SignatureLog();

        log.setId(10);
        log.setUserId(2);
        log.setFileName("signed.txt");
        log.setFileHash("hashXYZ");
        log.setSignatureValue("signature123");
        log.setCreatedAt(now);

        assertAll(
                () -> assertEquals(10, log.getId()),
                () -> assertEquals(2, log.getUserId()),
                () -> assertEquals("signed.txt", log.getFileName()),
                () -> assertEquals("hashXYZ", log.getFileHash()),
                () -> assertEquals("signature123", log.getSignatureValue()),
                () -> assertEquals(now, log.getCreatedAt())
        );
    }

}