package main.java.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/VIS";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // personal info

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(PASSWORD);
            throw new RuntimeException("Database connection failed", e);

        }
    }
}
