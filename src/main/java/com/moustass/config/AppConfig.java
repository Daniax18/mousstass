package com.moustass.config;

import com.moustass.exception.FileStorageException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    // Make this class Static to avoid new instantiation every time we want data from app.properties ??
    private Properties properties = new Properties();

    public AppConfig() {
        try (InputStream is = getClass().getResourceAsStream("/com/moustass/app.properties")) {
            if (is == null) {
                throw new FileStorageException("app.properties not found");
            }
            properties.load(is);
        } catch (IOException e) {
            throw new FileStorageException(e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
