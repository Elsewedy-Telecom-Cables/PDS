package com.etc.pds.db;

import com.etc.pds.services.ConfigLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbConnect {
    private static final String URL = ConfigLoader.getProperty("DB.URL");   // DB.URL.LOCAL
    private static final String USER = ConfigLoader.getProperty("DB.USER");
    private static final String PASSWORD = ConfigLoader.getProperty("DB.PASSWORD");



    private DbConnect() {
    }

    public static Connection getConnect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("[DbConnect] Main DB Connection Error: " + e.getMessage());
            return null;
        }
    }



}


