package com.example.gurpartap.skip_and_buy.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;

/*
    *   ScanItemFragment initiates the ZXing barcode scanner which helps
    *   user to scan the item bar code and redirect to the product inforamtion fragment
*/

public class ScanItemFragment extends Fragment {
    IntentIntegrator scanIntegrator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        scanIntegrator=new IntentIntegrator(getActivity());
        scanIntegrator.setTitle("Skip & Buy");
        scanIntegrator.initiateScan();
        return inflater.inflate(com.example.gurpartap.skip_and_buy.R.layout.fragment_scan_item, container, false);
    }
    }
