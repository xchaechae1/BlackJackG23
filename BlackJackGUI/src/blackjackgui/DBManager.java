/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjackgui;


import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Nicholas Lee
 */



public final class DBManager {

    // Using Derby network (client) connection
    private static final String URL = "jdbc:derby://localhost:1527/BlackJackGame";
    private static final String USER_NAME = "pdc";
    private static final String PASSWORD = "pdc";

    private Connection conn;

    public DBManager() {
        establishConnection();
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void establishConnection() {
        if (this.conn == null) {
            try {
                // Load Derby client driver explicitly
                Class.forName("org.apache.derby.jdbc.ClientDriver");

                conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                System.out.println(" Connected successfully to " + URL);
            } catch (ClassNotFoundException e) {
                System.out.println(" Derby client driver not found. Add derbyclient.jar to your dependencies.");
            } catch (SQLException ex) {
                System.out.println(" Connection failed: " + ex.getMessage());
            }
        }
    }

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection closed.");
            } catch (SQLException ex) {
                System.out.println("Failed to close connection: " + ex.getMessage());
            }
        }
    }

    public ResultSet queryDB(String sql) {
        try {
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println("Query failed: " + ex.getMessage());
            return null;
        }
    }

    public void updateDB(String sql) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Update executed successfully.");
        } catch (SQLException ex) {
            System.out.println("Update failed: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        DBManager db = new DBManager();
        if (db.getConnection() != null) {
            System.out.println("Connection established!");
        } else {
            System.out.println("Connection failed!");
        }
    }
}

