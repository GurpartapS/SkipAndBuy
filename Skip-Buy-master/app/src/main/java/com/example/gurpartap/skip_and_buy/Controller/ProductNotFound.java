package com.example.gurpartap.skip_and_buy.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gurpartap.skip_and_buy.R;

/*
    * ProductNotFound handles the operations regarding the view which is called
    * when the product information is not found once user scans the product using the scanner
*/

public class ProductNotFound extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_not_found, container, false);
    }

}
