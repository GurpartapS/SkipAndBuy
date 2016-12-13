package com.example.gurpartap.skip_and_buy.Model;

import java.sql.*;

/*
 ** SqlConnection class is used throughout the project to make
 ** connections to the SQL server for DML & DDL operations
*/

public class SqlConnection {

    private String connectionString = null;
    private String userName = null;
    private String password = null;
    private String className = null;

    public SqlConnection() {
        connectionString = "jdbc:jtds:sqlserver://teaminnovation.database.windows.net/SkipAndBuy";
        userName = "innovation";
        password = "skip&buy2016";
        className = "net.sourceforge.jtds.jdbc.Driver";
    }

    public SqlConnection(String connectionString, String userName, String password, String className) {
        this.connectionString = connectionString;
        this.userName = userName;
        this.password = password;
        this.className = className;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Connection connect() {

        Connection connection = null;

        try {
            Class.forName(className);
            connection = DriverManager.getConnection(connectionString, userName, password);
        } catch (Exception e) {
            connection = null;
            System.out.println("Exception occured in SqlConnection connect() method as " + e.getMessage());
        }

        return connection;
    }
}
