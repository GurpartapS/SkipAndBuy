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

/**
 * Created by OWNER on 11/15/2016.
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


    public Customer(int customerId,String customerName, String customerEmail, String customerPassword, String customerPhone,String customerImage) {
        this.customerId=customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPassword = customerPassword;
        this.customerPhone= customerPhone;
        this.customerImage=customerImage;
    }

    public Customer(String name, String email, String password, String phoneNumber) {

        this.customerName = name;
        this.customerEmail = email;
        this.customerPassword = password;
        this.customerPhone= phoneNumber;
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
        this.customerName= name;
    }

    public String getEmail() {
        return customerEmail;
    }

    public void setEmail(String email) {
        this.customerEmail= email;
    }

    public String getPassword() {
        return customerPassword;
    }

    public void setPassword(String password) {
        this.customerPassword= password;
    }

    public String getPhoneNumber() {
        return customerPhone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.customerPhone = phoneNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public Customer getCustomerDetails(String customerEmail){

/*        Customer customer[]=null;

        String customerEmailFirst=customerEmail.split("\\.")[0];
        String customerEmailSecond=customerEmail.split("\\.")[1];
        System.out.println("CUSTOMER EMAIL IS :"+customerEmail);
        ObjectMapper mapper = new ObjectMapper();

        String urlString = "http://skipandbuyrest.mybluemix.net/customerDetails/"+customerEmailFirst+"/"+customerEmailSecond;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection conn=null;
        try {
             conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //map = mapper.readValueconn.getInputStream();

        try {
           customer = mapper.readValue(conn.getInputStream(), Customer[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

*/
        Customer customer=null;

        try {
            SqlConnection connn = new SqlConnection();

            Connection connect = connn.connect();

            if (connect != null) {

                PreparedStatement getCustomerInfo = connect.prepareStatement("Select * from customer where customerEmail=?");

                getCustomerInfo.setString(1, customerEmail);

                ResultSet verifyStoreResultset = getCustomerInfo.executeQuery();

                if (verifyStoreResultset.next()) {
                    customer=new Customer(Integer.parseInt(verifyStoreResultset.getString("customerId")),verifyStoreResultset.getString("customerName"),verifyStoreResultset.getString("customerEmail"),verifyStoreResultset.getString("customerPassword"),verifyStoreResultset.getString("customerPhone"),verifyStoreResultset.getString("customerImage"));
                }
            } else {

            }
        }
        catch (SQLException e){
            System.out.println("Exception occured in Customer in getCustomerDetails() "+e.getMessage());
        }
        catch(Exception e){
            System.out.println("Exception occured in Customer in getCustomerDetails() "+e.getMessage());
        }
        return customer;

        //return customer[0];
    }


}
