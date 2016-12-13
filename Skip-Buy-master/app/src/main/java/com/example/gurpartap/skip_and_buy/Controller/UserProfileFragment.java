package com.example.gurpartap.skip_and_buy.Controller;

import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gurpartap.skip_and_buy.Model.Customer;
import com.example.gurpartap.skip_and_buy.Model.SqlConnection;
import com.example.gurpartap.skip_and_buy.Model.UserAccount;
import com.example.gurpartap.skip_and_buy.R;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/*
    *   UserProfileFragment displays important information about the user
    *   Information displayed includes user email, phone, password and user profile image
    *   UserProfileFragment also allows user to edit the profile information
    *   It also allows user to sign out of the application
*/

public class UserProfileFragment extends Fragment {

    private String userEmail;
    private String userPassword;
    private String userName;
    private String userPhone;
    private int userProfileImage;
    private EditText emailProfile;
    private EditText nameProfile;
    private EditText phoneProfile;
    private EditText passwordProfile;
    private ImageView userProfileImageView;
    private FileInputStream fis;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView=inflater.inflate(R.layout.fragment_user_profile, container, false);
        UserAccount currentUser=new UserAccount();
        emailProfile=(EditText)rootView.findViewById(R.id.emailProfile);
        emailProfile.setText(UserAccount.email);
        userEmail=UserAccount.email;
        setProfile(rootView);
        nameProfile=(EditText)rootView.findViewById(R.id.nameProfile);
        nameProfile.setText(userName);
        passwordProfile=(EditText)rootView.findViewById(R.id.passwordProfile);
        passwordProfile.setText(userPassword);
        phoneProfile=(EditText)rootView.findViewById(R.id.phoneProfile);
        phoneProfile.setText(userPhone);

        //userProfileImageView=(ImageView)rootView.findViewById(R.id.profileImage);
        //Drawable resImg = getResources().getDrawable(userProfileImage);
        //userProfileImageView.setImageResource(userProfileImage);

        FloatingActionButton changeImageButton=(FloatingActionButton)rootView.findViewById(R.id.changeImageButtton);
        changeImageButton.setVisibility(View.GONE);
        Button saveProfileButton=(Button)rootView.findViewById(R.id.saveProfileButton);

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailProfile=(EditText)rootView.findViewById(R.id.emailProfile);
                EditText nameProfile=(EditText)rootView.findViewById(R.id.nameProfile);
                EditText passwordProfile=(EditText)rootView.findViewById(R.id.passwordProfile);
                EditText phoneProfile=(EditText)rootView.findViewById(R.id.phoneProfile);

                if (nameProfile.getText().toString().equals("")||emailProfile.getText().toString().equals("")||passwordProfile.getText().toString().equals("")||phoneProfile.getText().toString().equals("")) {
                    String message="";

                    if(phoneProfile.getText().toString().equals("")){
                        phoneProfile.requestFocus();
                        phoneProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);
                        message="Please Enter Phone Number";
                        //phoneProfile.setTextColor(Color.RED);
                    }

                    if(passwordProfile.getText().toString().equals("")){
                        passwordProfile.requestFocus();
                        passwordProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);
                        message="Please Enter Password";
                        //passwordProfile.setTextColor(Color.RED);
                    }

                    if(emailProfile.getText().toString().equals("")){
                        emailProfile.requestFocus();
                        emailProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);
                        message="Please Enter Email";
                        //emailProfile.setTextColor(Color.RED);
                    }

                    if(nameProfile.getText().toString().equals("")){
                        nameProfile.requestFocus();
                        nameProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);
                        message="Please Enter Name";
                        //nameProfile.setTextColor(Color.RED);
                    }

                    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {
                    String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
                    String PHONE_REGEX="(\\d{3}){2}\\d{4}";
                    Boolean emailValidation = emailProfile.getText().toString().matches(EMAIL_REGEX);
                    if (emailValidation == false) {
                        Snackbar.make(view, "Invalid Email", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        emailProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);
                        //emailProfile.setTextColor(Color.RED);
                        emailProfile.requestFocus();
                    }
                    else if (passwordProfile.getText().toString().length()<6) {
                        Snackbar.make(view, "Please Enter Strong Password", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        passwordProfile.requestFocus();
                        passwordProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);
                        //passwordProfile.setTextColor(Color.RED);
                    }

                    else if (phoneProfile.getText().toString().matches(PHONE_REGEX)==false) {
                        Snackbar.make(view, "Invalid Phone Number", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        phoneProfile.requestFocus();
                        phoneProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);
                        //phoneProfile.setTextColor(Color.RED);
                    }

                    else {

                        try {

                            FloatingActionButton changeImageButton=(FloatingActionButton)rootView.findViewById(R.id.changeImageButtton);
                            changeImageButton.setVisibility(View.GONE);

                            Button saveProfileButton=(Button)rootView.findViewById(R.id.saveProfileButton);
                            saveProfileButton.setVisibility(View.GONE);

                            Button editProfileButton=(Button)rootView.findViewById(R.id.editProfileButton);
                            editProfileButton.setVisibility(View.VISIBLE);

                            emailProfile.setEnabled(false);
                            nameProfile.setEnabled(false);
                            passwordProfile.setEnabled(false);
                            phoneProfile.setEnabled(false);

                            nameProfile.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                            emailProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);
                            phoneProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);
                            passwordProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);

                            UserProfileFragment userProfile=new UserProfileFragment();
                            System.out.println("SAVING INFORMATION NOW!!!!");
                            new AsyncTaskRunner(view).execute();

                        }catch (Exception e) {
                            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }
            }
        });

        saveProfileButton.setVisibility(View.GONE);

        Button editProfileButton=(Button)rootView.findViewById(R.id.editProfileButton);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailProfile=(EditText)rootView.findViewById(R.id.emailProfile);
                EditText nameProfile=(EditText)rootView.findViewById(R.id.nameProfile);
                EditText passwordProfile=(EditText)rootView.findViewById(R.id.passwordProfile);
                EditText phoneProfile=(EditText)rootView.findViewById(R.id.phoneProfile);
                //ImageView userProfileImage=(ImageView)rootView.findViewById(R.id.profileImage);
                //File file = new File(userProfileImage.getDrawable().toString());



                /*
                try {
                    fis = new FileInputStream(file);
                }
                catch(FileNotFoundException e){
                    System.out.println("FILE NOT FOUND EXCEPTION!!! "+e.getMessage());
                }
                */

                FloatingActionButton changeImageButton=(FloatingActionButton)rootView.findViewById(R.id.changeImageButtton);
                changeImageButton.setVisibility(View.VISIBLE);

                Button saveProfileButton=(Button)rootView.findViewById(R.id.saveProfileButton);
                saveProfileButton.setVisibility(View.VISIBLE);

                Button editProfileButton=(Button)rootView.findViewById(R.id.editProfileButton);
                editProfileButton.setVisibility(View.GONE);

                emailProfile.setEnabled(true);
                nameProfile.setEnabled(true);
                passwordProfile.setEnabled(true);
                phoneProfile.setEnabled(true);

                nameProfile.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);
                emailProfile.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                phoneProfile.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                passwordProfile.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
        });

        emailProfile.setEnabled(false);
        nameProfile.setEnabled(false);
        passwordProfile.setEnabled(false);
        phoneProfile.setEnabled(false);
        nameProfile.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        return rootView;
    }

    public void setProfile(View rootView){

        Customer customer=new Customer();
        customer=customer.getCustomerDetails(UserAccount.email);

        userEmail=UserAccount.email;
        userName=customer.getName();
        userPhone=customer.getPhoneNumber();
        userPassword=customer.getPassword();
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;



        public AsyncTaskRunner(View view) {
            super();
            try {
                System.out.println("IN THE ASYNC TASK RUNNER NOW");
                SqlConnection connn = new SqlConnection();

                Connection connect = connn.connect();
                Connection connect1 = connn.connect();

                if (connect != null) {

                    PreparedStatement verifyUser = connect.prepareStatement("Select * from customer where customerEmail=?");
                    verifyUser.setString(1,emailProfile.getText().toString());

                    ResultSet verifyUserResultset = verifyUser.executeQuery();

                    Boolean accountExists=false;

                    if(verifyUserResultset.next()&&!UserAccount.email.equalsIgnoreCase(emailProfile.getText().toString())) {

                        Snackbar.make(view, "Account already exists", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        accountExists=true;
                    }
                    else {
                        System.out.println("IN THE ASYNC TASK RUNNER ELSE NOW");
                        Customer newCustomer=new Customer(nameProfile.getText().toString(),emailProfile.getText().toString(),
                                passwordProfile.getText().toString(), phoneProfile.getText().toString());

                        PreparedStatement statement = connect1.prepareStatement("update customer set customerName=?, customerEmail=? , customerPassword=?  " +
                                " , customerPhone=? where customerEmail=?");


                        System.out.println("INSERTING INFORMATION NOW!!!!");
                        System.out.println(newCustomer.getName());
                        statement.setString(1, newCustomer.getName());
                        statement.setString(2, newCustomer.getEmail());
                        statement.setString(3, newCustomer.getPassword());
                        statement.setString(4, newCustomer.getPhoneNumber());
                        statement.setString(5, UserAccount.email);

                        //statement.setBinaryStream(6,fis,);

                        System.out.println(UserAccount.email);

                        if (statement.executeUpdate() > 0) {

                                    Snackbar.make(view, "Profile Successfully Saved", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();


                        } else {
                            Snackbar.make(view, "Profile Update Unsuccessful", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }

                }
                else {
                    Snackbar.make(view, "Connection Problem", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            }
            catch(Exception e){

            }
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {

            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }


}
