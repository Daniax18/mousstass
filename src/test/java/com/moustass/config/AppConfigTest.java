package com.moustass.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    void getInstance_shouldReturnSameInstance() {
        AppConfig config1 = AppConfig.getInstance();
        AppConfig config2 = AppConfig.getInstance();

        assertSame(config1, config2, "AppConfig should be a singleton");
    }

    @Test
    void getInstance_shouldNotBeNull() {
        AppConfig config = AppConfig.getInstance();
        assertNotNull(config);
    }

    @Test
    void getProperty_SomeKeys_shouldReturnValue() {
        AppConfig config = AppConfig.getInstance();

        assertAll(
                () -> assertNotNull(config.getProperty("upload.dir"), "upload.dir should not be null"),
                () -> assertNotNull(config.getProperty("db.name"), "db.name should not be null"),
                () -> assertNotNull(config.getProperty("db.user"), "db.user should not be null"),
                () -> assertNotNull(config.getProperty("db.password"), "db.password should not be null"),
                () -> assertNotNull(config.getProperty("db.host"), "db.host should not be null")
        );
    }

    @Test
    void getProperty_nonExistingKey_shouldReturnNull() {
        AppConfig config = AppConfig.getInstance();

        String value = config.getProperty("unknown.key");
        assertNull(value);
    }
}