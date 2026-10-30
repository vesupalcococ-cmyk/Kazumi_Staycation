package com.mycompany.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL  = "jdbc:mysql://localhost:3306/kazumi_staycation";
    private static final String USER = "root";
    private static final String PASS = "";   // XAMPP default is empty

    public static Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL driver not found. Add mysql-connector-j to pom.xml, then Clean and Build.", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}