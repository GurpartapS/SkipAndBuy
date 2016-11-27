package com.example.gurpartap.skip_and_buy.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gurpartap.skip_and_buy.Model.SqlConnection;
import com.example.gurpartap.skip_and_buy.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class StoreFragment extends Fragment {

    private String storeName;
    private String storeLocation;
    private String storeInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View rootView=inflater.inflate(R.layout.fragment_store, container, false);

        setStore(rootView);

        TextView storeDescription=(TextView)rootView.findViewById(R.id.storeDescription);
        storeDescription.setText(storeInfo);
        TextView storeTitle=(TextView)rootView.findViewById(R.id.storeTitle);
        storeTitle.setText(storeName);
        TextView address=(TextView)rootView.findViewById(R.id.address);
        address.setText(storeLocation);
        Button shopButton=(Button)rootView.findViewById(R.id.shopNowButton);
        Button viewFlyerButton=(Button)rootView.findViewById(R.id.viewFlyerButton);
        //ImageView menuButton=(ImageView)rootView.findViewById(R.id.imageView);

        String SHOWCASE_ID="32";
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this.getActivity(), SHOWCASE_ID);

        sequence.setConfig(config);

        sequence.addSequenceItem(shopButton,
                "Start scanning products by clicking here!!", "GOT IT");

        sequence.addSequenceItem(viewFlyerButton,
                "View today's flyer and view best deals in store", "GOT IT");

        /*sequence.addSequenceItem(menuButton,
        "Navigate to your order history, shopping cart and profile from here","GOT IT");
        */
        sequence.start();

        return rootView;
    }

    public void setStore(View rootView){

        try {
            SqlConnection connn = new SqlConnection();

            Connection connect = connn.connect();
            Connection connect1 = connn.connect();

            System.out.println("THE LOCATION IS:"+MainActivity.location);

            if (connect != null) {

                PreparedStatement getUserInfo = connect.prepareStatement("Select * from Store where storeAddress=?");

                getUserInfo.setString(1,MainActivity.location);
                System.out.println("THE LOCATION IS:"+MainActivity.location);

                ResultSet verifyUserResultset = getUserInfo.executeQuery();
                System.out.println("PRINTING THE RESULTS OF USER ACCOUNT QUERY");
                if(verifyUserResultset.next()){
                    System.out.println("STORE IS SUCCESSFULLY LOCATED");
                    storeLocation=MainActivity.location;
                    storeName=verifyUserResultset.getString("storeName");
                    storeInfo=verifyUserResultset.getString("storeDescription");
                }
                else{
                    System.out.println("UNABLE TO LOCATE THE STORE");
                    ((MainActivity)getActivity()).storeNotFound();
                }

            }
            else {
                Snackbar.make(rootView, "Connection Problem in store", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        catch(Exception e){
            System.out.println("EXCEPTION IN STORE FRAGMENT "+e.getMessage());
        }
    }
}