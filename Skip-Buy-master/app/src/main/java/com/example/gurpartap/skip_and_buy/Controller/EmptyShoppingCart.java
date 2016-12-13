package com.example.gurpartap.skip_and_buy.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gurpartap.skip_and_buy.R;


/*
    *   EmptyShoppingCart has the information about empty cart
    *   It also redirects user to add item to the cart by scanning
*/

public class EmptyShoppingCart extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empty_shopping_cart, container, false);
    }

}
