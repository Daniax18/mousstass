package com.moustass.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ActivityLogTest {
    @Test
    void defaultConstructor_shouldCreateEmptyObject() {
        ActivityLog log = new ActivityLog();

        assertNotNull(log);
        assertNull(log.getId());
        assertNull(log.getUserId());
        assertNull(log.getAction());
        assertNull(log.getDetails());
        assertNull(log.getCreatedAt());
    }

    @Test
    void parameterizedConstructor_shouldSetMandatoryFields() {
        ActivityLog log = new ActivityLog(
                1,
                ActivityLog.TypeAction.FILE_DOWNLOAD,
                "Downloaded signed file"
        );

        assertAll(
                () -> assertEquals(1, log.getUserId()),
                () -> assertEquals(ActivityLog.TypeAction.FILE_DOWNLOAD, log.getAction()),
                () -> assertEquals("Downloaded signed file", log.getDetails())
        );
    }

    @Test
    void setters_shouldUpdateValues() {
        LocalDateTime now = LocalDateTime.now();
        ActivityLog log = new ActivityLog();

        log.setId(10);
        log.setUserId(2);
        log.setAction(ActivityLog.TypeAction.LOGIN_SUCCESS);
        log.setDetails("Login ok");
        log.setCreatedAt(now);

        assertAll(
                () -> assertEquals(10, log.getId()),
                () -> assertEquals(2, log.getUserId()),
                () -> assertEquals(ActivityLog.TypeAction.LOGIN_SUCCESS, log.getAction()),
                () -> assertEquals("Login ok", log.getDetails()),
                () -> assertEquals(now, log.getCreatedAt())
        );
    }

    @Test
    void typeAction_enum_shouldContainExpectedValues() {
        ActivityLog.TypeAction[] values = ActivityLog.TypeAction.values();

        assertTrue(values.length >= 1);
        assertTrue(
                java.util.Arrays.asList(values).contains(ActivityLog.TypeAction.FILE_UPLOAD)
        );
    }

}