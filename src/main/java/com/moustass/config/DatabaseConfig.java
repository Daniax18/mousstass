package com.moustass.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    public Connection databaseLink;

    public Connection getConnection(){
        AppConfig config = new AppConfig();

        String databaseName= config.getProperty("db.name");
        String databaseUser= config.getProperty("db.user");
        String databasePassword = config.getProperty("db.password");
        String host = config.getProperty("db.host");

        String url = "jdbc:mysql://" + host + "/"+ databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser, databasePassword);
        } catch (Exception e){
            throw new RuntimeException("Erreur de connexion MySQL : " + e.getMessage());
        }

        return databaseLink;
    }
}
