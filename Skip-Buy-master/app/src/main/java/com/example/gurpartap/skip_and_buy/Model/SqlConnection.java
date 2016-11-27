package com.example.gurpartap.skip_and_buy.Model;

// Use the JDBC driver
import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;

public class SqlConnection {


    public SqlConnection() {
    }

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public  Connection connect() {
        String connectionString =
                "jdbc:jtds:sqlserver://teaminnovation.database.windows.net/SkipAndBuy:1433;"
                        + "database=SkipAndBuy;"
                        + "user=innovation@teaminnovation;"
                        + "password=skip&buy2016;"
                        + "encrypt=true;"
                        + "trustServerCertificate=false;"
                        + "hostNameInCertificate=*.database.windows.net;"
                        + "loginTimeout=30;";

        // Declare the JDBC objects.
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://teaminnovation.database.windows.net/SkipAndBuy","innovation","skip&buy2016");
            System.out.println("GOING FOR SELECT STATMENT");
            // Create and execute a SELECT SQL statement.
            /*String selectSql = "SELECT * from dbo.products";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectSql);
            System.out.println("DONE WITH QUERY");
            // Print results from select statement
            while (resultSet.next())
            {
                System.out.println("THIS IS RESULT SET");
                System.out.println(resultSet.getString(1));
            }*/
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        /*finally {
            // Close the connections after the data has been handled.
            if (resultSet != null) try { resultSet.close(); } catch(Exception e) {}
            if (statement != null) try { statement.close(); } catch(Exception e) {}
            if (connection != null) try { connection.close(); } catch(Exception e) {}
        }*/
        return connection;
    }
}
