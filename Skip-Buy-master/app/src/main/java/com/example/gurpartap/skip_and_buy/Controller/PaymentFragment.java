package com.example.gurpartap.skip_and_buy.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gurpartap.skip_and_buy.Model.PayPalConfig;
import com.example.gurpartap.skip_and_buy.Model.ShoppingCartItem;
import com.example.gurpartap.skip_and_buy.Model.SqlConnection;
import com.example.gurpartap.skip_and_buy.Model.UserAccount;
import com.example.gurpartap.skip_and_buy.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentFragment extends Fragment {
    //PaymentActivity Amount
    private String paymentAmount;

    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;
    TextView subtotal, taxes, total,totalItemsView;
    Button btn, cancelBtn;
    String totalItems;
    String subtotalPrice;
    String taxesPrice;
    String totalPrice;

    public PaymentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        totalItemsView=(TextView)view.findViewById(R.id.totalItems);
        subtotal = (TextView) view.findViewById(R.id.subTotalNumber);
        taxes = (TextView) view.findViewById(R.id.taxes);
        total = (TextView) view.findViewById(R.id.total);
        btn = (Button)view.findViewById(R.id.payNowbtn);
        cancelBtn = (Button)view.findViewById(R.id.cancelbtn);

        setPaymentDetails(view);

        //String setSubtotal = "$60.00";
        subtotal.setText("$ "+subtotalPrice);

        //double subTotal = Double.parseDouble(subtotal.getText().toString().substring(1));

        //double calculatedTax = Math.round(subTotal*0.13*100)/100.00;
        taxes.setText("$ "+taxesPrice);

        //double grandTotal = subTotal+calculatedTax;
        total.setText("$ "+totalPrice);

        totalItemsView.setText(totalItems);

        //Getting the amount from editText
        paymentAmount = totalPrice;

        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                getPayment();
            }
        });
        return view;
    }

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    private void getPayment() {

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "CAD", "Total Amount:",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal PaymentActivity activity intent
        Intent intent = new Intent(getActivity(), PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);


                if (confirm != null) {
                    try {

                        String paymentDetails = confirm.toJSONObject().toString(5);
                        Log.i("paymentExample", paymentDetails);

                        String paymentTime=confirm.toJSONObject().getString("create_time");
                        Log.i("Payment Time Example: ",paymentTime);

                        Bundle bundle = new Bundle();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        System.out.println("PAYMENT DETAILS ARE: "+paymentDetails);
                        System.out.println("PAYMENT DATE TIME IS: "+dateFormat.format(date));

                        //bundle.putString("PaymentDetails", paymentDetails);
                        bundle.putString("PaymentAmount", paymentAmount);
                        bundle.putString("PaymentStatus","Complete");
                        bundle.putString("PaymentDate",dateFormat.format(date));

                        ConfirmationFragment confirmationFragment= new ConfirmationFragment();
                        confirmationFragment.setArguments(bundle);
                        this.getFragmentManager().beginTransaction()
                                .replace(R.id.contentFrameMain, confirmationFragment)
                                .addToBackStack(null)
                                .commit();
                    }catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);

                } catch (Exception e) {
                    Log.e("ANOTHER EXCEPTION", e.getMessage());
                }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user cancelled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid PaymentActivity or PayPalConfiguration was submitted. .");
            }
        }
    }

    public void setPaymentDetails(View rootView){
        String storeId="";
        String userId="";

        try {
            SqlConnection connn = new SqlConnection();

            Connection connect = connn.connect();
            Connection connect1 = connn.connect();
            Connection connect2 = connn.connect();

            if (connect != null) {

                PreparedStatement getStoreInfo = connect.prepareStatement("Select storeId from Store where storeAddress=?");

                getStoreInfo.setString(1,MainActivity.location);

                ResultSet verifyStoreResultset = getStoreInfo.executeQuery();

                if(verifyStoreResultset.next()){
                    storeId=verifyStoreResultset.getString("storeId");
                }
            }
            else {

            }

            if (connect2 != null) {

                PreparedStatement getStoreInfo = connect.prepareStatement("Select customerId from customer where customerEmail=?");

                getStoreInfo.setString(1, UserAccount.email);

                ResultSet verifyStoreResultset = getStoreInfo.executeQuery();

                if(verifyStoreResultset.next()){
                    userId=verifyStoreResultset.getString("customerId");
                }
            }
            else {

            }

            if (connect1 != null) {

                PreparedStatement getStoreInfo = connect.prepareStatement("Select * from cart where customerId=? and storeId=?");

                getStoreInfo.setString(1, userId);
                getStoreInfo.setString(2, storeId);

                ResultSet verifyStoreResultset = getStoreInfo.executeQuery();
                totalItems="0";
                subtotalPrice="0";
                while(verifyStoreResultset.next()){

                    System.out.println("IN THE PAYMENT FRAGMENT CART LOOP WITH STORE ID AS "+storeId+" CUSTOMER ID AS "+userId);
                    totalItems=Integer.toString(Integer.parseInt(totalItems)+Integer.parseInt(verifyStoreResultset.getString("quantity")));
                    subtotalPrice=Integer.toString(Integer.parseInt(subtotalPrice)+
                            Integer.parseInt(verifyStoreResultset.getString("price")));
                }

                taxesPrice=Double.toString(0.13*Integer.parseInt(subtotalPrice));

                totalPrice=Double.toString(Double.parseDouble(taxesPrice)+Double.parseDouble(subtotalPrice));

            }
            else {

            }

        }
        catch(Exception e){
            System.out.println("EXCEPTION OCCURED IN SHOPPING CART FRAGMENT -: "+e.getMessage());
        }


    }
}
