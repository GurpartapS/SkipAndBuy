package com.example.gurpartap.skip_and_buy.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gurpartap.skip_and_buy.R;

/*
    * Flyer fragment takes care of the digital flyers
*/
public class Flyer extends Fragment {



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_flyer, container, false);


        return rootView;
    }



}
