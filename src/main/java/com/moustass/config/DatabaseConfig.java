package com.moustass.config;

import com.moustass.exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

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
