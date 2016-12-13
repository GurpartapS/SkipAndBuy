package com.example.gurpartap.skip_and_buy.Controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.gurpartap.skip_and_buy.Model.SqlConnection;
import com.example.gurpartap.skip_and_buy.Model.UserAccount;
import com.example.gurpartap.skip_and_buy.R;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


/*
    * This activity class is used when user logs in to the application
    * It establishes connection to database and validates user's credentials
*/

public class LoginActivity extends AppCompatActivity {
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    SqlConnection conn;
    Connection connect;

    /**
     * Mobile Service Table used to access data
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        YoYo.with(Techniques.Bounce)
                .duration(2000)
                .playOn(findViewById(R.id.imageView2));
        YoYo.with(Techniques.Bounce)
                .duration(2000)
                .playOn(findViewById(R.id.textView6));

        setupUI(findViewById(R.id.loginScreen));
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        emailText = (EditText) findViewById(R.id.emailTextField);
        passwordText = (EditText) findViewById(R.id.passwordTextField);
        loginButton = (Button) findViewById(R.id.loginButton);

        emailText.setTextColor(Color.WHITE);
        emailText.getBackground().setColorFilter(getResources().getColor(R.color.editTextField), PorterDuff.Mode.SRC_ATOP);
        passwordText.setTextColor(Color.WHITE);
        passwordText.getBackground().setColorFilter(getResources().getColor(R.color.editTextField), PorterDuff.Mode.SRC_ATOP);

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordText.setTextColor(Color.WHITE);
                passwordText.getBackground().setColorFilter(getResources().getColor(R.color.editTextField), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailText.setTextColor(Color.WHITE);
                emailText.getBackground().setColorFilter(getResources().getColor(R.color.editTextField), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginButton.setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View view) {

                        final View spinnerView = view;
                        new CountDownTimer(1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                new LoginActivity.AsyncTaskRunner(spinnerView).execute();
                            }

                        }.start();


                    }
                });

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        public AsyncTaskRunner(View view) {

            if (emailText.getText().toString().equals("") || passwordText.getText().toString().equals("")) {

                Snackbar.make(view, "PLEASE ENTER DETAILS", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if (passwordText.getText().toString().equals("")) {

                    passwordText.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);

                    passwordText.setTextColor(Color.RED);
                    passwordText.requestFocus();
                }
                if (emailText.getText().toString().equals("")) {

                    emailText.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);

                    emailText.setTextColor(Color.RED);

                    emailText.requestFocus();
                }
            } else {
                String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
                Boolean emailValidation = emailText.getText().toString().matches(EMAIL_REGEX);
                if (emailValidation == false) {
                    Snackbar.make(view, "INVALID EMAIL", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    emailText.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);

                    emailText.setTextColor(Color.RED);
                    emailText.requestFocus();

                } else {
                    UserAccount currentUser = new UserAccount(emailText.getText().toString(), passwordText.getText().toString());

                    emailText.setTextColor(Color.WHITE);
                    emailText.getBackground().setColorFilter(getResources().getColor(R.color.editTextField), PorterDuff.Mode.SRC_ATOP);
                    passwordText.setTextColor(Color.WHITE);
                    passwordText.getBackground().setColorFilter(getResources().getColor(R.color.editTextField), PorterDuff.Mode.SRC_ATOP);

                    try {
                        conn = new SqlConnection();
                        connect = conn.connect();

                        PreparedStatement statement = connect.prepareStatement("Select * from dbo.agent where " +
                                "agentEmail=? and agentPassword=?");


                        statement.setString(1, currentUser.getEmail());
                        statement.setString(2, currentUser.getPassword());

                        ResultSet resultSet = statement.executeQuery();

                        if (!resultSet.next()) {

                            Snackbar.make(view, "Invalid Details", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            emailText.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);

                            emailText.setTextColor(Color.RED);
                            passwordText.getBackground().setColorFilter(getResources().getColor(R.color.errorTextField), PorterDuff.Mode.SRC_ATOP);

                            passwordText.setTextColor(Color.RED);

                        } else {
                            UserAccount.email = currentUser.getEmail();
                            Snackbar.make(view, "Login Successful", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                    } catch (Exception e) {
                        Snackbar.make(view, "ERROR " + e.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }
            }
        }

        @Override
        protected String doInBackground(String... params) {


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            //loadingBackScreen.setVisibility(View.GONE);
            //loadingImage.setVisibility(View.GONE);


        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void signUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}