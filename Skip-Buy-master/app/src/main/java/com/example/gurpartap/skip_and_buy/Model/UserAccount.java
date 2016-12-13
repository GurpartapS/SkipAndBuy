package com.example.gurpartap.skip_and_buy.Model;

/**
 * Created by OWNER on 10/7/2016.
 */

public class UserAccount {

    public static String email;
    private  String password;

    public UserAccount() {
    }

    public UserAccount(String email, String password) {

        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
