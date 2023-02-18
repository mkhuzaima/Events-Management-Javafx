package com.example.management;

import java.sql.*;

public class DBConnection {
    public static Connection connection = null;
    public PreparedStatement preStatement = null;
    public ResultSet result = null;
    private final static String CONNECTION_URL = "jdbc:mysql://localhost:3306/event_management";
    public DBConnection(){
        try {
            connection = DriverManager.getConnection(CONNECTION_URL,"root","root");
        } catch (SQLException e) {
            System.out.println("Cannot Connect to the Database:  " + e.getMessage());
            e.printStackTrace();
        }
    }
}

