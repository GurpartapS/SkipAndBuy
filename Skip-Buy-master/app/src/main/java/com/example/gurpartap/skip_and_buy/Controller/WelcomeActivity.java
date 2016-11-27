package com.example.gurpartap.skip_and_buy.Controller;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.gurpartap.skip_and_buy.R;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ImageView imageLogo;
        final ImageView locateLogo;
        final ImageView scanLogo;
        final ImageView payLogo;
        final TextView welcomeText;
        final TextView brandText;
        final Button tourButton;
        final Button skipButton;
        final TextView locateStore;
        final TextView scanProd;
        final TextView payText;
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));



        setContentView(R.layout.activity_welcome_screen);
        imageLogo=(ImageView)findViewById(R.id.imageView4);
        imageLogo.setVisibility(View.INVISIBLE);
        welcomeText=(TextView) findViewById(R.id.textView);
        welcomeText.setVisibility(View.INVISIBLE);

        brandText=(TextView) findViewById(R.id.textView8);
        brandText.setVisibility(View.INVISIBLE);

        locateStore=(TextView) findViewById(R.id.locateStore);
        locateStore.setVisibility(View.INVISIBLE);

        locateLogo=(ImageView)findViewById(R.id.imageView11);
        locateLogo.setVisibility(View.INVISIBLE);

        scanLogo=(ImageView)findViewById(R.id.imageView12);
        scanLogo.setVisibility(View.INVISIBLE);

        payLogo=(ImageView)findViewById(R.id.imageView13);
        payLogo.setVisibility(View.INVISIBLE);

        scanProd=(TextView) findViewById(R.id.scanProd);
        scanProd.setVisibility(View.INVISIBLE);

        payText=(TextView) findViewById(R.id.pay);
        payText.setVisibility(View.INVISIBLE);

        tourButton=(Button)findViewById(R.id.button2);
        tourButton.setVisibility(View.INVISIBLE);

        skipButton=(Button)findViewById(R.id.skipButton);
        skipButton.setVisibility(View.INVISIBLE);



        new CountDownTimer(10000, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {
                //TextView timerText=(TextView)findViewById(R.id.timerText);
                //timerText.setText(""+(millisUntilFinished/1000)+"");
                if(millisUntilFinished/1000==9){
                    welcomeText.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeIn)
                            .duration(1000)
                            .playOn(findViewById(R.id.textView));

                }
                if(millisUntilFinished/1000==7) {

                    imageLogo.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInLeft)
                            .duration(2000)
                            .playOn(findViewById(R.id.imageView4));
                }
                if(millisUntilFinished/1000==6){

                    brandText.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomIn)
                            .duration(1000)
                            .playOn(findViewById(R.id.textView8));
                    locateLogo.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInLeft)
                            .duration(2000)
                            .playOn(findViewById(R.id.imageView11));
                }
                if(millisUntilFinished/1000==5){

                    locateStore.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.RubberBand)
                            .duration(1000)
                            .playOn(findViewById(R.id.locateStore));
                    scanLogo.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInLeft)
                            .duration(2000)
                            .playOn(findViewById(R.id.imageView12));
                }
                if(millisUntilFinished/1000==4){

                    scanProd.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.RubberBand)
                            .duration(1000)
                            .playOn(findViewById(R.id.scanProd));
                    payLogo.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInLeft)
                            .duration(2000)
                            .playOn(findViewById(R.id.imageView13));
                }
                if(millisUntilFinished/1000==3){


                    payText.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.RubberBand)
                            .duration(1000)
                            .playOn(findViewById(R.id.pay));
                }
                if(millisUntilFinished/1000==2){
                    tourButton.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.Flash)
                            .duration(1000)
                            .playOn(findViewById(R.id.button2));
                }
                if(millisUntilFinished/1000==1){
                    skipButton.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.Flash)
                            .duration(1000)
                            .playOn(findViewById(R.id.skipButton));


                }

            }

            @Override
            public void onFinish() {

            }

        }.start();

        String SHOWCASE_ID="4";
        new MaterialShowcaseView.Builder(this)
                .setTarget(tourButton)
                .setDismissText("GOT IT")
                .setContentText("Click Here to take a tour of Skip & Buy")
                .setDelay(8000) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
                .show();


    }


    public void takeAppTour(View view){
        //Intent intent=new Intent(this, TourActivity.class);
        //startActivity(intent);
    }


    public void mainActivity(View view){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
