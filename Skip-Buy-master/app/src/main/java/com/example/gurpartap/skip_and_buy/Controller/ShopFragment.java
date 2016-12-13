package com.example.gurpartap.skip_and_buy.Controller;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gurpartap.skip_and_buy.R;
import com.skyfishjy.library.RippleBackground;


public class ShopFragment extends Fragment {

    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopfragment, container, false);
        final RippleBackground rippleBackground = (RippleBackground) rootView.findViewById(R.id.content);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.centerImage);
        rippleBackground.startRippleAnimation();

        return rootView;
    }


}