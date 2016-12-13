package com.example.gurpartap.skip_and_buy.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gurpartap.skip_and_buy.Model.SqlConnection;
import com.example.gurpartap.skip_and_buy.Model.UserAccount;
import com.example.gurpartap.skip_and_buy.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class ConfirmationFragment extends Fragment {

    public ConfirmationFragment() {
        // Required empty public constructor
    }

    TextView textViewId,textViewStatus,textViewAmount, textViewTime;
    String paymentAmount;
    String paymentStatus;
    String paymentDateTime;
    String paymentId;
    public static String receiptId="";
    public static String userId="";
    List<String> productList;
    String productQuantity;

    Button ok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        textViewStatus= (TextView)view. findViewById(R.id.paymentStatus);
        textViewAmount = (TextView) view.findViewById(R.id.paymentAmount);
        textViewTime = (TextView) view.findViewById(R.id.paymentTime);
        textViewId = (TextView) view.findViewById(R.id.orderNumberText);

        ok = (Button)view.findViewById(R.id.okBtn);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        paymentDateTime = dateFormat.format(date);
        paymentStatus = "Complete";

        setPaymentDetails(view);

        textViewId.setText(receiptId);
        textViewTime.setText(paymentDateTime);
        textViewAmount.setText("$ "+paymentAmount);
        textViewStatus.setText(paymentStatus);

        String allProductList=userId+","+receiptId;
/*
        for(int i=0;i<productList.size();i++){
            allProductList=allProductList+productList.get(i);

            if(productList.size()-1==i){

            }
            else{
                allProductList=allProductList+",";
            }
        }
*/
        ImageView imageView = (ImageView) view.findViewById(R.id.qrCode);
        try {
            Bitmap bitmap = encodeAsBitmap(allProductList);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void showDetails() throws JSONException {
        //Views
        //Showing the details from json object
        textViewId.setText("101012");
        textViewStatus.setText(paymentStatus);
        textViewAmount.setText("$ "+paymentAmount);
        textViewTime.setText(paymentDateTime);
    }


    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 200, 200, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 200, 0, 0, w, h);
        return bitmap;
    }

    public void setPaymentDetails(View rootView){
        String storeId="";
        userId="";

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

            productList=new ArrayList<String>();

            if (connect1 != null) {

                PreparedStatement getStoreInfo = connect.prepareStatement("Select * from cart where customerId=? and storeId=?");

                getStoreInfo.setString(1, userId);
                getStoreInfo.setString(2, storeId);

                ResultSet verifyStoreResultset = getStoreInfo.executeQuery();

                String subtotalPrice="0";
                productQuantity="0";

                while(verifyStoreResultset.next()){
                    productList.add(verifyStoreResultset.getString("productId"));
                    System.out.println("IN THE PAYMENT FRAGMENT CART LOOP WITH STORE ID AS "+storeId+" CUSTOMER ID AS "+userId);
                    subtotalPrice=Integer.toString(Integer.parseInt(subtotalPrice)+
                            Integer.parseInt(verifyStoreResultset.getString("price")));
                    productQuantity=Integer.toString(Integer.parseInt(verifyStoreResultset.getString("quantity"))+Integer.parseInt(productQuantity));
                }

                String taxesPrice=Double.toString(0.13*Integer.parseInt(subtotalPrice));

                paymentAmount=Double.toString(Double.parseDouble(taxesPrice)+Double.parseDouble(subtotalPrice));

            }
            else {

            }



            if (connect1 != null) {

                PreparedStatement getStoreInfo = connect.prepareStatement("Insert into receipt values(?,?,?,?,?,?)");

                getStoreInfo.setString(1, paymentDateTime);
                getStoreInfo.setString(2, paymentStatus);
                getStoreInfo.setString(3, paymentAmount);
                getStoreInfo.setString(4, userId);
                getStoreInfo.setString(5, storeId);
                getStoreInfo.setString(6, productQuantity);

                getStoreInfo.executeUpdate();


                if (connect1 != null) {

                    PreparedStatement getReceiptId= connect.prepareStatement("Select receiptId from receipt where customerId=? and storeId=? and receiptDateTime=?");

                    getReceiptId.setString(1, userId);
                    getReceiptId.setString(2, storeId);
                    getReceiptId.setString(3, paymentDateTime);

                    ResultSet verifyReceiptIdResult= getReceiptId.executeQuery();

                    while(verifyReceiptIdResult.next()){
                        receiptId=verifyReceiptIdResult.getString("receiptId");
                    }
                }
                else {

                }



                if (connect1 != null) {
                    PreparedStatement creatOrderHistory = connect1.prepareStatement("Insert into orderHistory values(?,?,?,?,?,?,?)");

                    creatOrderHistory.setString(1, paymentDateTime);
                    creatOrderHistory.setString(2, paymentStatus);
                    creatOrderHistory.setString(3, paymentAmount);
                    creatOrderHistory.setString(4, receiptId);
                    creatOrderHistory.setString(5, userId);
                    creatOrderHistory.setString(6, storeId);
                    creatOrderHistory.setString(7, productQuantity);

                    creatOrderHistory.executeUpdate();
                }
                else {

                }


                for(int i=0;i<productList.size();i++) {

                    PreparedStatement storeReceiptProducts = connect1.prepareStatement("Insert into receiptProducts values(?,?,?)");

                    storeReceiptProducts.setString(1, receiptId);
                    storeReceiptProducts.setString(2, productList.get(i));
                    storeReceiptProducts.setString(3, "");

                    storeReceiptProducts.executeUpdate();
                }

            }
            else {

            }


            if (connect1 != null) {
                PreparedStatement getStoreInfo = connect.prepareStatement("delete from cart where customerId=? and storeId=?");

                getStoreInfo.setString(1, userId);
                getStoreInfo.setString(2, storeId);

                getStoreInfo.executeUpdate();

            }
            else {

            }
        }
        catch(Exception e){
            System.out.println("EXCEPTION OCCURED IN SHOPPING CART FRAGMENT -: "+e.getMessage());
        }


    }
}
