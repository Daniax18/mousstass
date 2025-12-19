package com.moustass.view;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SignatureViewTest {
    @Test
    void defaultConstructor_shouldCreateEmptyObject() {
        SignatureView view = new SignatureView();

        assertNotNull(view);
        assertNull(view.getIdSignature());
        assertNull(view.getUserName());
        assertNull(view.getFileName());
        assertNull(view.getDateSignature());
    }

    @Test
    void parameterizedConstructor_shouldSetAllFields() {
        LocalDateTime now = LocalDateTime.now();

        SignatureView view = new SignatureView(
                1,
                "john_doe",
                "document.pdf",
                now
        );

        assertAll(
                () -> assertEquals(1, view.getIdSignature()),
                () -> assertEquals("john_doe", view.getUserName()),
                () -> assertEquals("document.pdf", view.getFileName()),
                () -> assertEquals(now, view.getDateSignature())
        );
    }

    @Test
    void setters_shouldUpdateValues() {
        LocalDateTime now = LocalDateTime.now();
        SignatureView view = new SignatureView();

        view.setIdSignature(5);
        view.setUserName("alice");
        view.setFileName("signed.txt");
        view.setDateSignature(now);

        assertAll(
                () -> assertEquals(5, view.getIdSignature()),
                () -> assertEquals("alice", view.getUserName()),
                () -> assertEquals("signed.txt", view.getFileName()),
                () -> assertEquals(now, view.getDateSignature())
        );
    }
}