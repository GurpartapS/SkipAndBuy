package com.example.gurpartap.skip_and_buy.Controller;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gurpartap.skip_and_buy.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=manager.beginTransaction();
        final PaymentFragment paymentFragment = new PaymentFragment();
        fragmentTransaction.replace(R.id.contentFrameMain, paymentFragment);
        fragmentTransaction.commit();
    }
}
