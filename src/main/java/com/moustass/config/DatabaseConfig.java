package com.moustass.config;

import com.moustass.exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database configuration and connection provider.
 */
public class DatabaseConfig {

    /**
     * Creates and returns a connection to the MySQL database.
     * <p>
     * Database connection parameters (host, database name, user, password) are retrieved from the application configuration.
     * </p>
     *
     * @return an active {@link Connection} to the database
     * @throws DatabaseConnectionException if the JDBC driver is not found or if a database access error occurs
     */
    public Connection getConnection(){
        AppConfig config = AppConfig.getInstance();

        String databaseName= config.getProperty("db.name");
        String databaseUser= config.getProperty("db.user");
        String databasePassword = config.getProperty("db.password");
        String host = config.getProperty("db.host");

        String url = "jdbc:mysql://" + host + "/"+ databaseName;
        Connection databaseLink = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser, databasePassword);
        } catch (SQLException | ClassNotFoundException e){
            throw new DatabaseConnectionException("Erreur de connexion MySQL : " + e.getMessage());
        }

        return databaseLink;
    }
}
