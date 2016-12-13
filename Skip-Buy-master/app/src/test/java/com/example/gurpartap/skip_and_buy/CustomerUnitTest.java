package com.example.gurpartap.skip_and_buy;

import com.example.gurpartap.skip_and_buy.Model.Customer;
import com.example.gurpartap.skip_and_buy.Model.SqlConnection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/*
    * CustomerUnitytest has tests related to Customer information which involves various testing scenarios
*/

public class CustomerUnitTest {

    private String customerEmail;
    private Customer customer;

    // Test for the scenarios when Customer exists in the database and is present.
    @Test
    public void customer_exists() throws Exception {

        customer=new Customer();
        String customerEmail="aulakh161@gmail.com";
        assertNotEquals(customer.getCustomerDetails(customerEmail), null);

    }

    // Test for the scenarios when Customer does not exists int he database and is not present.
    @Test
    public void customer_doesNotExists() throws Exception {

        customer=new Customer();
        String customerEmail="random@email.com";
        assertEquals(customer.getCustomerDetails(customerEmail), null);

    }

}