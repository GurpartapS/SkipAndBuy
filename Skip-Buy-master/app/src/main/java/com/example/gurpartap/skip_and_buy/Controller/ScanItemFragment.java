package com.example.gurpartap.skip_and_buy.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gurpartap.skip_and_buy.*;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.facebook.FacebookSdk.getApplicationContext;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zxing.*;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import me.dm7.barcodescanner.zbar.ZBarScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

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
