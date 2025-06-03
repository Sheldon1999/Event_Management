package com.eventmanagement.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseConfig {
    private static final String CONFIG_FILE = "/config.properties";
    private static final Properties props = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");
        return DriverManager.getConnection(url, username, password);
    }
}