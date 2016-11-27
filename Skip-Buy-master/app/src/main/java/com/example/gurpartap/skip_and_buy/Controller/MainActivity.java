package com.example.gurpartap.skip_and_buy.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gurpartap.skip_and_buy.Model.UserAccount;
import com.example.gurpartap.skip_and_buy.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.paypal.android.sdk.payments.*;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment=new Fragment();
    String scannerResult="";
    String paypalResult="";
    LocationManager mlocManager;
    public static String location;
    public static String scannedProductCode;
    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private Cursor cursor;

    private String paymentAmount;

    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        scannerResult="";

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();


        getSupportActionBar().setTitle("Shop");
        fragment=new ShopFragment();
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();
        System.out.println("LOCATION STARTED");

        //new CountDownTimer(3000, 1000) {

    //public void onTick(long millisUntilFinished) {
        //TextView timerText=(TextView)findViewById(R.id.timerText);
        //timerText.setText(""+(millisUntilFinished/1000)+"");
      //  if(millisUntilFinished/1000==9){
            mlocManager =

                    (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            System.out.println("LOCATION ONE");
            LocationListener mlocListener = new MainActivity.MyLocationListener();
            System.out.println("LOCATION TWO");
            try {
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                System.out.println("LOCATION THREE");
            } catch (SecurityException e) {
                System.out.println("SADsadsd "+e.getMessage());
            }


    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        cursor = getContentResolver().query(uri, projection,null,null,null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    //}

    /*@Override
    public void onFinish() {
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();
        getSupportActionBar().setTitle("Shop");
        fragment=new StoreFragment();

        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commit();
        Bundle b = new Bundle();
        b.putString("storeTitle","Walmart Canada");
        b.putString("storeDescription","Groceries & Household Goods");
        b.putString("address","7899 McLaughlin Rd, Brampton, ON L6Y 5H9");
        fragment.setArguments(b);
    }

    }.start();
        }
*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        getSupportActionBar().setTitle("Shop");
        fragment=new ShopFragment();
        int id = item.getItemId();

        if (id == R.id.nav_shop) {
            getSupportActionBar().setTitle("Shop");
            fragment=new ShopFragment();
            locateStore();

        } else if (id == R.id.nav_history) {


        } else if (id == R.id.nav_cart) {
            getSupportActionBar().setTitle("Shopping Cart");
            fragment=new ShoppingCartFragment();
        } else if (id == R.id.nav_profile) {
            getSupportActionBar().setTitle("Profile");
            fragment=new UserProfileFragment();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void replaceAddCardFragment(View view){
        getSupportActionBar().setTitle("Select PaymentActivity Method");
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        Fragment fragment=new Fragment();

        Snackbar.make(view, "Adding new card", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        fragment=new ShoppingCartFragment();
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();
    }



    public void viewStoreFlyer(View view){
        getSupportActionBar().setTitle("Flyer");
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        Fragment fragment=new Fragment();

        fragment=new Flyer();
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();
    }

    public void confirmOrder(){

        getSupportActionBar().setTitle("Payment Confirmation");
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        Fragment fragment=new Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("PaymentDetails", "Walmart");
        bundle.putString("PaymentAmount", "20");

        fragment=new ConfirmationFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();

    }

    public void scanItemActivity(View view){
        /*Intent intent=new Intent(this, scanItemScreen.class);
        startActivity(intent);*/
        getSupportActionBar().setTitle("Scan Item");
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        Fragment fragment=new Fragment();
        fragment=new ScanItemFragment();
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();
    }

    public void paymentFragment(View view){
        /*Intent intent=new Intent(this, scanItemScreen.class);
        startActivity(intent);*/
        getSupportActionBar().setTitle("Order Summary");
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        Fragment fragment=new Fragment();
        fragment=new PaymentFragment();
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();
    }

    public void locateStoreAgain(View view){

        getSupportActionBar().setTitle("Store");
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        Fragment fragment=new Fragment();
        fragment=new StoreFragment();
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();

        locateStore();
    }
    public void scanProductAgain(View view){

        /*Intent intent=new Intent(this, scanItemScreen.class);
        startActivity(intent);*/
        getSupportActionBar().setTitle("Scan Item");
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        Fragment fragment=new Fragment();
        fragment=new ScanItemFragment();
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();
    }

    public void locateStore(){
        System.out.println("CHECKING FOR THE STORE PROFILE");
        if(fragment.getClass().toString().equalsIgnoreCase("class com.example.gurpartap.skip_and_buy.Controller.ShopFragment")) {

            System.out.println("CHECKED AND FOUND FOR THE STORE PROFILE");

            mlocManager =

                    (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            System.out.println("LOCATION ONE");
            LocationListener mlocListener = new MainActivity.MyLocationListener();
            System.out.println("LOCATION TWO");
            try {
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                System.out.println("LOCATION THREE");
            } catch (SecurityException e) {
                System.out.println("SADsadsd " + e.getMessage());
            }
        }
        else{
            System.out.println("CANNOT FIND THE STORE PROFILE");
        }
    }

    public void viewCartActivity(View view){

            getSupportActionBar().setTitle("Shopping Cart");
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

            FragmentTransaction transaction = manager.beginTransaction();

            Fragment fragment = new Fragment();
            fragment = new ShoppingCartFragment();

            transaction.replace(R.id.contentFrameMain, fragment);
            transaction.commitAllowingStateLoss();

            ProductInfoFragment productInfo=new ProductInfoFragment();
            productInfo.saveShoppingCart();

    }
    public void viewProductInfo(){
        /*Intent intent=new Intent(this, scanItemScreen.class);
        startActivity(intent);*/
        getSupportActionBar().setTitle("Product Information");
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        Fragment fragment=new Fragment();
        fragment=new ProductInfoFragment();
        Bundle b = new Bundle();
        b.putString("productName","Excel Gum");
        b.putString("productDescription","Happiness right out of the pack. Enjoy the rich, flaky, chewgum in your mouth taste of Excel in a variety of delicious flavours.");
        b.putString("productWeight","100g");
        b.putString("productPrice","$2 / Pc.");
        fragment.setArguments(b);
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();
    }

    public void storeNotFound(){

        getSupportActionBar().setTitle("Store Not Found");
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        Fragment fragment=new Fragment();
        System.out.println("THE STORE IS NOT FOUND AND NOW CHANGING THE FRAGMENT");
        fragment=new StoreNotFound();
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();
    }

    public void productNotFound(){

        getSupportActionBar().setTitle("Product Not Found");
        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        FragmentTransaction transaction=manager.beginTransaction();

        Fragment fragment=new Fragment();
        System.out.println("THE PRODUCT IS NOT FOUND AND NOW CHANGING THE FRAGMENT");
        fragment=new ProductNotFound();
        transaction.replace(R.id.contentFrameMain,fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult= IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        if(scanningResult!=null){

            Toast toast= Toast.makeText(getApplicationContext(),"SCANNING RESULT \n"+scanningResult.getContents().toString(),Toast.LENGTH_SHORT);

            toast.show();
            scannerResult=scanningResult.getContents().toString();
        }
        else{

            System.out.println("THIS IS ELSEE STATEMENT");
            //If the result is from paypal
            //if (requestCode == PAYPAL_REQUEST_CODE) {
                System.out.println("THIS IS ELSEE STATEMENT 1");
                //If the result is OK i.e. user has not canceled the payment
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("THIS IS ELSEE STATEMENT");
                    //Getting the payment confirmation
                    PaymentConfirmation confirm = intent.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                    //if confirmation is not null
                    if (confirm != null) {
                        System.out.println("ENTERERD THIS METHOD 23");
                        try {
                            //Getting the payment details
                            System.out.println("ENTERERD THIS METHOD 45");
                            String paymentDetails = confirm.toJSONObject().toString(5);
                            Log.i("paymentExample", paymentDetails);

                            System.out.println("ENTERERD THIS METHOD");
                            paypalResult="worked";
                        } catch (JSONException e) {
                            Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                        }
                        catch(Exception e){
                            System.out.println("EXCEPTION "+e.getMessage());
                        }
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.i("paymentExample", "The user cancelled.");
                } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                    Log.i("paymentExample", "An invalid PaymentActivity or PayPalConfiguration was submitted. .");
                }
            //}
        }

        //if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                System.out.println("SETTING IMAGE NOW!!!");
                Uri selectedImageUri = intent.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("IMAGE PATH IS: "+selectedImageUri.toString());
                ImageView userImage=(ImageView)findViewById(R.id.profileImage);
                userImage.setImageURI(selectedImageUri);
            }
       // }
    }


    public void signOutActivity(View view){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void selectProfilePicture(View view) {

        // in onCreate or any event where your want the user to
        // select a file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onResume() {
        if(scannerResult==""){

            if(paypalResult=="")
            {

            }
            else{
                confirmOrder();
                paypalResult="";
            }
        }
        else{

            scannedProductCode=scannerResult;

            viewProductInfo();

            scannerResult="";
        }

        super.onResume();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
    public class MyLocationListener implements LocationListener

    {

        @Override

        public void onLocationChanged(Location loc)

        {
            System.out.println("LOCATION TRACKED ONE");


            double Lat=loc.getLatitude();

            double Long=loc.getLongitude();
            String Text="";
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            System.out.println("LOCATION IS ALREADY FOUND: LATITUDE IS :"+Lat+" LONGITUDE IS: "+Long);

            try
            {

                addresses = geocoder.getFromLocation(Lat, Long, 1);
                System.out.println("LOCATION TRACKED TWO");

                if(fragment.getClass().toString().equalsIgnoreCase("class com.example.gurpartap.skip_and_buy.Controller.ShopFragment")){
                        android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

                        FragmentTransaction transaction=manager.beginTransaction();
                        getSupportActionBar().setTitle("Shop");
                        fragment=new StoreFragment();

                        transaction.replace(R.id.contentFrameMain,fragment);
                        transaction.commitAllowingStateLoss();

                    }
                    System.out.println("LOCATION TRACKED THREE");
                    String address = addresses.get(0).getAddressLine(0);
                    String address11 = addresses.get(0).getAddressLine(1);
                    String city = addresses.get(0).getLocality();
                    Text = "My current location is: " +

                            "Latitud = " + loc.getLatitude() +
                            "Longitud = " + loc.getLongitude()+
                            "address = " + address +
                            "address1 = " + address11 +
                            "city = " + city;

                    System.out.println("THE ADDRESS IS: " +address+" "+address11);

                    location=address+" "+address11;

                    try {
                        mlocManager.removeUpdates(this);
                    }
                    catch(SecurityException e){
                        Toast.makeText(getApplicationContext(),

                                "SECURITY EXCEPTION OCCURED",

                                Toast.LENGTH_SHORT).show();
                    }
                    Bundle b = new Bundle();
                    b.putString("storeTitle","Walmart Canada");
                    b.putString("storeDescription","Groceries & Household Goods");
                    b.putString("address",address+" "+address11);
                    fragment.setArguments(b);

            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }


        @Override

        public void onProviderDisabled(String provider)

        {
            Toast.makeText(getApplicationContext(),

                    "Please Turn On GPS",
                    Toast.LENGTH_SHORT).show();

            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
            /*Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);*/



        }

        @Override

        public void onProviderEnabled(String provider)

        {


        }


        @Override

        public void onStatusChanged(String provider, int status, Bundle extras)

        {


        }
    }


}
