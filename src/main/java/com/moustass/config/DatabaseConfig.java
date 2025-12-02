package com.moustass.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName= "moustass_database";
        String databaseUser= "root";
        String databasePassword = "06Sept2004root";
        String url = "jdbc:mysql://localhost/"+ databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser, databasePassword);
        } catch (Exception e){
            throw new RuntimeException("Erreur de connexion MySQL : " + e.getMessage());
        }

        return databaseLink;
    }
}
