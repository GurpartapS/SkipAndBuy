package com.example.gurpartap.skip_and_buy.Controller;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.gurpartap.skip_and_buy.R;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import it.slyce.messaging.SlyceMessagingFragment;
import it.slyce.messaging.listeners.UserSendsMessageListener;
import it.slyce.messaging.message.MediaMessage;
import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.TextMessage;


/*
    ** ConnectActivity is responsible for handling communication between agent and customer
    ** ConnectActivity connects with the interface and provides functionality of profile picture and text message interface
 */

public class ConnectActivity extends AppCompatActivity {
    
    private static String[] urls = {
            "http://en.l4c.me/fullsize/googleplex-mountain-view-california-1242979177.jpg",
            "http://entropymag.org/wp-content/uploads/2014/10/outer-space-wallpaper-pictures.jpg",
            "http://www.bolwell.com/wp-content/uploads/2013/09/bolwell-metal-fabrication-raw-material.jpg",
            "http://www.bytscomputers.com/wp-content/uploads/2013/12/pc.jpg",
            "https://content.edmc.edu/assets/modules/ContentWebParts/AI/Locations/New-York-City/startpage-masthead-slide.jpg"
    };

    private volatile static int n = 0;

    SlyceMessagingFragment slyceMessagingFragment;

    private boolean hasLoadedMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_connect);

        hasLoadedMore = true;

        slyceMessagingFragment = (SlyceMessagingFragment) getFragmentManager().findFragmentById(R.id.fragment_for_slyce_messaging);
        slyceMessagingFragment.setDefaultAvatarUrl("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5");
        slyceMessagingFragment.setDefaultDisplayName("Matthew Page");
        slyceMessagingFragment.setDefaultUserId("uhtnaeohnuoenhaeuonthhntouaetnheuontheuo");

        slyceMessagingFragment.setOnSendMessageListener(new UserSendsMessageListener() {
            @Override
            public void onUserSendsTextMessage(String text) {
                Log.d("inf", "******************************** " + text);
            }

            @Override
            public void onUserSendsMediaMessage(Uri imageUri) {
                Log.d("inf", "******************************** " + imageUri);
            }
        });

        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        TextMessage textMessage = new TextMessage();
        textMessage.setText("Hi there!! How may I help you??");
        textMessage.setAvatarUrl("https://lh3.googleusercontent.com/-Y86IN-vEObo/AAAAAAAAAAI/AAAAAAAKyAM/6bec6LqLXXA/s0-c-k-no-ns/photo.jpg");
        textMessage.setDisplayName("Gary Johnson");
        textMessage.setUserId("LP");
        textMessage.setDate(new Date().getTime());
        textMessage.setSource(MessageSource.EXTERNAL_USER);
        slyceMessagingFragment.addNewMessage(textMessage);


    }

    private static Message getRandomMessage() {
        n++;
        Message message;
        if (Math.random() < 1.1) {
            TextMessage textMessage = new TextMessage();
            textMessage.setText(n + ""); // +  ": " + latin[(int) (Math.random() * 10)]);
            message = textMessage;
        } else {
            MediaMessage mediaMessage = new MediaMessage();
            mediaMessage.setUrl(urls[(int) (Math.random() * 5)]);
            message = mediaMessage;
        }
        message.setDate(new Date().getTime());
        if (Math.random() > 0.5) {
            message.setAvatarUrl("https://lh3.googleusercontent.com/-Y86IN-vEObo/AAAAAAAAAAI/AAAAAAAKyAM/6bec6LqLXXA/s0-c-k-no-ns/photo.jpg");
            message.setUserId("LP");
            message.setSource(MessageSource.EXTERNAL_USER);
        } else {
            message.setAvatarUrl("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5");
            message.setUserId("MP");
            message.setSource(MessageSource.LOCAL_USER);
        }
        return message;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
