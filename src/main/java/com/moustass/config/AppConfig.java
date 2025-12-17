package com.moustass.config;

import com.moustass.exception.FileStorageException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Application configuration manager.
 * <p>
 * This class is responsible for loading and providing access to
 * application-level configuration properties defined in the
 * {@code app.properties} file.
 * </p>
 * <p>
 * It follows the Singleton design pattern to ensure that the
 * configuration is loaded only once during the application lifecycle.
 * </p>
 */
// Singleton pattern est mis en cause par Sonar, à revoir
@SuppressWarnings("java:S6548")
public class AppConfig {
    private static AppConfig instance;
    private final Properties properties = new Properties();

    /**
     * Private constructor that loads the application configuration file.
     * @throws FileStorageException if the configuration file is not found or cannot be loaded
     */
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

    /**
     * Returns the singleton instance of {@link AppConfig}.
     * @return the singleton instance of AppConfig
     */
    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    /**
     * Retrieves the value of a configuration property.
     *
     * @param key the name of the property to retrieve
     * @return the value associated with the given key or {@code null} if the key does not exist
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
