package com.gr1tEnt.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "src/main/resources/config.properties";
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream inputStream = new FileInputStream(CONFIG_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String URL = properties.getProperty("db.url");
    private static final String USER = properties.getProperty("db.username");
    private static final String PASSWORD = properties.getProperty("db.password");

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error while connecting to the database " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
