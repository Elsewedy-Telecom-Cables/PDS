package com.elsewedyt.pdsapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbConnect {
    private static Connection connection = null;
    private static Connection externalConnection = null;

    // Server Connection
    //private static final String HOST = "10.1.212.147";  // Server IP
   // private static final String HOST = "ETCSVR";  // Server NAME
  //  private static final String HOST = "localhost";  // local
    private static final String HOST = "SWD100950";  // local
    private static final int PORT = 1433;
    public static final String DB_NAME_CONECCTION = "pds";  // DB Name
    private static final String USER = "sa";   // Server and local User
    private static final String PASSWORD = "Pro@12345"; // Server and local Pass


public static Connection getConnect() {
    try {
        String url = String.format(
                "jdbc:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=true",
                HOST, PORT, DB_NAME_CONECCTION, USER, PASSWORD
        );
        return DriverManager.getConnection(url);
    } catch (SQLException e) {
        System.err.println("getConnect Error: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}



}


