package com.example.gurpartap.skip_and_buy.Controller;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.gurpartap.skip_and_buy.Model.ShoppingCartItem;
import com.example.gurpartap.skip_and_buy.Model.SqlConnection;
import com.example.gurpartap.skip_and_buy.Model.UserAccount;
import com.example.gurpartap.skip_and_buy.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by GURPARTAP on 2016-09-16.
 */
public class ShoppingCartAdapter extends ArrayAdapter<ShoppingCartItem> {

    Context context;
    int layoutResourceId;
    ArrayList<ShoppingCartItem> data=null;
    int pos;



    public ShoppingCartAdapter(Context context, int layoutResourceId, ArrayList<ShoppingCartItem> data) {
        super(context, layoutResourceId,data);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        pos=position;
        View row=convertView;

        CardHolder holder=null;

        if(row==null){

            LayoutInflater inflater=((Activity)context).getLayoutInflater();
            row=inflater.inflate(layoutResourceId,parent,false);

            holder=new CardHolder();
            holder.icon=(ImageView)row.findViewById(R.id.iconView);
            holder.name=(TextView)row.findViewById(R.id.iconTitle);
            holder.weight=(TextView)row.findViewById(R.id.iconWeight);
            holder.price=(TextView)row.findViewById(R.id.iconPrice);
            holder.quantity=(EditText) row.findViewById(R.id.quantityEditText);

            row.setTag(holder);
        }

        else{
            holder=(CardHolder)row.getTag();
        }

        ShoppingCartItem card=data.get(position);
        holder.name.setText(card.name);
        holder.icon.setImageResource(card.icon);
        holder.weight.setText(card.weight);
        holder.price.setText("$ "+card.amount);
        holder.quantity.setText(card.productQuantity);

        Button deleteButton = (Button) row.findViewById(R.id.removeButton);
        deleteButton.setTag(position);



        deleteButton.setOnClickListener(

                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pos=(Integer)v.getTag();
                        System.out.println("RECEIVED A CLICK FROM THE BUTTON!!!");

                        String storeId = "";
                        String userId = "";
                        try {
                            SqlConnection connn = new SqlConnection();
                            Connection connect = connn.connect();
                            Connection connect1 = connn.connect();
                            Connection connect2 = connn.connect();
                            if (connect != null) {
                                PreparedStatement getStoreInfo = connect.prepareStatement("Select storeId from Store where storeAddress=?");
                                getStoreInfo.setString(1, MainActivity.location);
                                ResultSet verifyStoreResultset = getStoreInfo.executeQuery();
                                if (verifyStoreResultset.next()) {
                                    storeId = verifyStoreResultset.getString("storeId");
                                }
                            } else {
                            }
                            if (connect2 != null) {
                                PreparedStatement getStoreInfo = connect.prepareStatement("Select customerId from customer where customerEmail=?");
                                getStoreInfo.setString(1, UserAccount.email);
                                ResultSet verifyStoreResultset = getStoreInfo.executeQuery();
                                if (verifyStoreResultset.next()) {
                                    userId = verifyStoreResultset.getString("customerId");
                                }
                            } else {
                            }
                            if (connect1 != null) {
                                PreparedStatement getStoreInfo = connect.prepareStatement("delete from cart where customerId=? and storeId=? and productId=?");
                                getStoreInfo.setString(1, userId);
                                getStoreInfo.setString(2, storeId);
                                getStoreInfo.setString(3, getItem(pos).productId);
                                getStoreInfo.executeUpdate();
                            } else {
                            }
                        } catch (Exception e) {
                            System.out.println("EXCEPTION OCCURED IN SHOPPING CART FRAGMENT -: " + e.getMessage());
                        }
                        data.remove(pos);
                        notifyDataSetChanged();
                        if(data.size()==0){
                            ((MainActivity)context).emptyShoppingCart();
                        }
                    }
                }
        );







        return row;
    }


    /* public void removeListItem(View view){

     }
 */
    static class CardHolder{
        ImageView icon;
        TextView name;
        TextView weight;
        TextView price;

        EditText quantity;

    }
}