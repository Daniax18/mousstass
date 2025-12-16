package com.moustass.config;

import com.moustass.exception.FileStorageException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Singleton pattern est mis en cause par Sonar, à revoir
@SuppressWarnings("java:S6548")
public class AppConfig {
    private static AppConfig instance;
    private final Properties properties = new Properties();

    private AppConfig() {
        try (InputStream is = getClass().getResourceAsStream("/com/moustass/app.properties")) {
            if (is == null) {
                throw new FileStorageException("app.properties not found");
            }
            properties.load(is);
        } catch (IOException e) {
            throw new FileStorageException(e.getMessage());
        }
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
