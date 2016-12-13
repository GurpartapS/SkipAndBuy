package com.example.gurpartap.skip_and_buy;

import com.example.gurpartap.skip_and_buy.Model.SqlConnection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/*
    * SqlConnectionUnitTest has tests related to SqlConnection which involves various testing scenarios
*/

public class SqlConnectionUnitTest {

    private SqlConnection connection;
    private String connectionString = "jdbc:jtds:sqlserver://teaminnovation.database.windows.net/SkipAndBuy";
    private String userName = "innovation";
    private String password = "skip&buy2016";
    private String className = "net.sourceforge.jtds.jdbc.Driver";

    // Test for the scenarios when SqlConnection should be working fine.
    @Test
    public void sqlconnection_isWorking() throws Exception {

        connection = new SqlConnection(connectionString,userName,password,className);
        assertNotEquals(connection.connect(), null);

    }

    // Test for the scenarios when SqlConnection string is incorrect.
    @Test
    public void sqlconnection_incorrectUserName() throws Exception {

        String incorrectUserName = "incorrect";
        connection = new SqlConnection(connectionString, incorrectUserName, password, className);
        assertEquals(connection.connect(), null);
    }

    // Test for the scenarios when SqlConnection password is incorrect.
    @Test
    public void sqlconnection_incorrectPassword() throws Exception {

        String incorrectPassword = "incorrect";
        connection = new SqlConnection(connectionString, userName, incorrectPassword, className);
        assertEquals(connection.connect(), null);
    }

    // Test for the scenarios when SqlConnection className is incorrect.
    @Test
    public void sqlconnection_incorrectClassName() throws Exception {

        String incorrectClassName = "This is a random class name";
        connection = new SqlConnection(connectionString, userName, password, incorrectClassName);
        assertEquals(connection.connect(), null);
    }

    // Test for the scenarios when SqlConnection string is incorrect.
    @Test
    public void sqlconnection_incorrectString() throws Exception {

        String incorrectString = "This is a random string";
        connection = new SqlConnection(incorrectString, userName, password, className);
        assertEquals(connection.connect(), null);
    }
}