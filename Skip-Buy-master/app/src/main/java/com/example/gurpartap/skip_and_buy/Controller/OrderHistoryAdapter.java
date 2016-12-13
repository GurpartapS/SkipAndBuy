package com.example.gurpartap.skip_and_buy.Controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gurpartap.skip_and_buy.Model.OrderHistoryItem;
import com.example.gurpartap.skip_and_buy.R;

/**
 * Created by GURPARTAP on 2016-09-16.
 */
public class OrderHistoryAdapter extends ArrayAdapter<OrderHistoryItem> {

    Context context;
    int layoutResourceId;
    OrderHistoryItem data[]=null;
    int pos;



    public OrderHistoryAdapter(Context context, int layoutResourceId, OrderHistoryItem data[]) {

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
            holder.quantity=(EditText) row.findViewById(R.id.productQuantityEditText);

            row.setTag(holder);
        }

        else{
            holder=(CardHolder)row.getTag();
        }

        OrderHistoryItem card=data[position];
        holder.name.setText(card.name);
        holder.icon.setImageResource(card.icon);
        holder.weight.setText(card.weight);
        holder.price.setText("$ "+card.amount);
        holder.quantity.setText(card.productQuantity);

        return row;
    }


   /* public void removeListItem(View view){

        System.out.println("RECEIVED A CLICK FROM THE BUTTON!!!");

        ShoppingCartAdapter.this.remove(getItem(pos));
        ShoppingCartAdapter.this.notifyDataSetChanged();

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
