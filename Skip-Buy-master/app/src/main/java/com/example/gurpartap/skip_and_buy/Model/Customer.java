package com.example.gurpartap.skip_and_buy.Model;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.gurpartap.skip_and_buy.Controller.MainActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/*
    * This class maps to the customer table
    * and instance variables of this class
    * are mapped to the columns of the customer table
*/

public class Customer {
    private int customerId;
    private String customerName;
    private String customerEmail;
    private String customerPassword;
    private String customerPhone;
    private String customerImage;

    public Customer() {
    }


    public Customer(int customerId, String customerName, String customerEmail, String customerPassword, String customerPhone, String customerImage) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPassword = customerPassword;
        this.customerPhone = customerPhone;
        this.customerImage = customerImage;
    }

    public Customer(String name, String email, String password, String phoneNumber) {

        this.customerName = name;
        this.customerEmail = email;
        this.customerPassword = password;
        this.customerPhone = phoneNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return customerName;
    }

    public void setName(String name) {
        this.customerName = name;
    }

    public String getEmail() {
        return customerEmail;
    }

    public void setEmail(String email) {
        this.customerEmail = email;
    }

    public String getPassword() {
        return customerPassword;
    }

    public void setPassword(String password) {
        this.customerPassword = password;
    }

    public String getPhoneNumber() {
        return customerPhone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.customerPhone = phoneNumber;
    }

    public Customer getCustomerDetails(String customerEmail) {

        Customer customer = null;

        try {
            SqlConnection connn = new SqlConnection();

            Connection connect = connn.connect();

            if (connect != null) {

                PreparedStatement getCustomerInfo = connect.prepareStatement("Select * from customer where customerEmail=?");

                getCustomerInfo.setString(1, customerEmail);

                ResultSet verifyStoreResultset = getCustomerInfo.executeQuery();

                if (verifyStoreResultset.next()) {
                    customer = new Customer(Integer.parseInt(verifyStoreResultset.getString("customerId")), verifyStoreResultset.getString("customerName"), verifyStoreResultset.getString("customerEmail"), verifyStoreResultset.getString("customerPassword"), verifyStoreResultset.getString("customerPhone"), verifyStoreResultset.getString("customerImage"));
                }
            } else {

            }
        } catch (SQLException e) {
            System.out.println("Exception occured in Customer in getCustomerDetails() " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception occured in Customer in getCustomerDetails() " + e.getMessage());
        }
        return customer;

    }


}
