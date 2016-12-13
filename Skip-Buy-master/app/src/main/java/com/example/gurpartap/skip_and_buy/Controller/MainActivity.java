package com.example.gurpartap.skip_and_buy.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.gurpartap.skip_and_buy.Model.Customer;
import com.example.gurpartap.skip_and_buy.Model.SqlConnection;
import com.example.gurpartap.skip_and_buy.Model.UserAccount;
import com.example.gurpartap.skip_and_buy.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.paypal.android.sdk.payments.*;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.slyce.messaging.SlyceMessagingFragment;
import it.slyce.messaging.message.MediaMessage;
import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.TextMessage;

/*
    *   MainActivity handles most of the operations for Skip & Buy
    *   MainActivity handles fragment swapping when options are selected from the navbar
    *   MainActivity also handles operations of switching to other activities
    *   MainActivity also handles button on click operations for various frament buttons
*/


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = new Fragment();
    String scannerResult = "";
    String paypalResult = "";
    int CASE_1 = 0;
    LocationManager mlocManager;
    public static String location;
    public static String scannedProductCode;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1888;
    public static boolean tourDoneStore = false;
    public static boolean tourDone = false;

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

    File photoFile;

    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;

    private TextView userProfileEmail;
    private TextView userProfileName;
    private String selectedImagePath;
    private Cursor cursor;


    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        scannerResult = "";

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();


        getSupportActionBar().setTitle("Store");
        fragment = new ShopFragment();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
        System.out.println("LOCATION STARTED");


        new CountDownTimer(1500, 1000) {

            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 <= 0) {
                    android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

                    FragmentTransaction transaction = manager.beginTransaction();
                    getSupportActionBar().setTitle("Shop");
                    fragment = new StoreFragment();

                    transaction.replace(R.id.contentFrameMain, fragment);
                    transaction.commitAllowingStateLoss();
                    location = "813 Sheridan College Drive Brampton, ON L6Y 5H9";
                } else {
                    mlocManager =

                            (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    LocationListener mlocListener = new MainActivity.MyLocationListener();
                    try {
                        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                    } catch (SecurityException e) {
                        System.out.println("Exception Occured: " + e.getMessage());
                    }
                }
            }

            public void onFinish() {

            }

        }.start();

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

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
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


    @Override
    public void onBackPressed() {

        System.out.println("BACK IS PRESSED!!!  " + fragment.getClass().toString());

        if (fragment.getClass().toString().equalsIgnoreCase("class com.example.gurpartap.skip_and_buy.Controller.StoreFragment")) {
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

            FragmentTransaction transaction = manager.beginTransaction();
            getSupportActionBar().setTitle("Shop");
            fragment = new StoreFragment();

            transaction.replace(R.id.contentFrameMain, fragment);
            transaction.commitAllowingStateLoss();

        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        userProfileName = (TextView) findViewById(R.id.userProfileName);
        userProfileEmail = (TextView) findViewById(R.id.userProfileEmail);

        userProfileEmail.setText(UserAccount.email);

        Customer customer = new Customer();

        customer = customer.getCustomerDetails(UserAccount.email);

        userProfileName.setText(customer.getName());


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
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        getSupportActionBar().setTitle("Shop");
        fragment = new ShopFragment();
        int id = item.getItemId();
        boolean share = false;
        if (id == R.id.nav_shop) {
            getSupportActionBar().setTitle("Shop");
            fragment = new ShopFragment();
            locateStore();

        } else if (id == R.id.nav_history) {
            getSupportActionBar().setTitle("Order History");
            fragment = new OrderHistoryFragment();
        } else if (id == R.id.nav_cart) {
            getSupportActionBar().setTitle("Shopping Cart");
            fragment = new ShoppingCartFragment();
        } else if (id == R.id.nav_profile) {
            getSupportActionBar().setTitle("Profile");
            fragment = new UserProfileFragment();
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(this, ConnectActivity.class);
            startActivity(intent);
            share = true;
        } else if (id == R.id.nav_send) {

        }
        if (share == true) {
            share = false;
        } else {
            transaction.replace(R.id.contentFrameMain, fragment);
            transaction.commitAllowingStateLoss();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceAddCardFragment(View view) {
        getSupportActionBar().setTitle("Select PaymentActivity Method");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();

        Snackbar.make(view, "Adding new card", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        fragment = new ShoppingCartFragment();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void takePictureActivity(View view) {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
    }


    public void viewStoreFlyer(View view) {
        getSupportActionBar().setTitle("Flyer");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();

        fragment = new Flyer();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void confirmOrder() {

        getSupportActionBar().setTitle("Payment Confirmation");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("PaymentDetails", "Walmart");
        bundle.putString("PaymentAmount", "20");

        fragment = new ConfirmationFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();

    }

    public void scanItemActivity(View view) {
        /*Intent intent=new Intent(this, scanItemScreen.class);
        startActivity(intent);*/
        getSupportActionBar().setTitle("Scan Item");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();
        fragment = new ScanItemFragment();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void paymentFragment(View view) {
        /*Intent intent=new Intent(this, scanItemScreen.class);
        startActivity(intent);*/
        getSupportActionBar().setTitle("Order Summary");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();
        fragment = new PaymentFragment();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void showOrderHistory(View view) {

        getSupportActionBar().setTitle("Order History");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        fragment = new OrderHistoryFragment();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }


    public void locateStoreAgain(View view) {

        new CountDownTimer(1500, 1000) {

            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 <= 0) {
                    android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

                    FragmentTransaction transaction = manager.beginTransaction();
                    getSupportActionBar().setTitle("Shop");
                    fragment = new StoreFragment();

                    transaction.replace(R.id.contentFrameMain, fragment);
                    transaction.commitAllowingStateLoss();
                    location = "813 Sheridan College Drive Brampton, ON L6Y 5H9";
                } else {
                    mlocManager =

                            (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    LocationListener mlocListener = new MainActivity.MyLocationListener();
                    try {
                        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                    } catch (SecurityException e) {
                        System.out.println("Exception Occured: " + e.getMessage());
                    }
                }
            }

            public void onFinish() {

            }

        }.start();
    }

    public void shopNow(View view) {

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        getSupportActionBar().setTitle("Shop");
        fragment = new ShopFragment();
        locateStore();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void scanProductAgain(View view) {

        /*Intent intent=new Intent(this, scanItemScreen.class);
        startActivity(intent);*/
        getSupportActionBar().setTitle("Scan Item");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();
        fragment = new ScanItemFragment();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void locateStore() {

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 <= 0) {
                    android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

                    FragmentTransaction transaction = manager.beginTransaction();
                    getSupportActionBar().setTitle("Shop");
                    fragment = new StoreFragment();

                    transaction.replace(R.id.contentFrameMain, fragment);
                    transaction.commitAllowingStateLoss();
                    location = "45 Trailside Walk Brampton, ON L6S 6H6";
                } else {
                    mlocManager =

                            (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    LocationListener mlocListener = new MainActivity.MyLocationListener();
                    try {
                        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                    } catch (SecurityException e) {
                        System.out.println("Exception Occured: " + e.getMessage());
                    }
                }
            }

            public void onFinish() {

            }

        }.start();


        /*
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
        */
    }

    public void viewCartActivity(View view) {

        getSupportActionBar().setTitle("Shopping Cart");
        FragmentManager manager = getSupportFragmentManager();

        EditText productQuantity = (EditText) findViewById(R.id.productQuantity);
        //Bundle b = new Bundle();
        //b.putString("productQuantity", productQuantity.getText().toString());

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();
        fragment = new ShoppingCartFragment();

        //fragment.setArguments(b);
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();

        ProductInfoFragment productInfo = new ProductInfoFragment();
        productInfo.saveShoppingCart(productQuantity.getText().toString());

    }

    public void viewProductInfo() {
        /*Intent intent=new Intent(this, scanItemScreen.class);
        startActivity(intent);*/
        getSupportActionBar().setTitle("Product Information");
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();
        fragment = new ProductInfoFragment();
        Bundle b = new Bundle();
        b.putString("productName", "Excel Gum");
        b.putString("productDescription", "Happiness right out of the pack. Enjoy the rich, flaky, chewgum in your mouth taste of Excel in a variety of delicious flavours.");
        b.putString("productWeight", "100g");
        b.putString("productPrice", "$2 / Pc.");
        fragment.setArguments(b);
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void emptyShoppingCart() {
        getSupportActionBar().setTitle("Shopping Cart");
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();

        fragment = new EmptyShoppingCart();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void emptyOrderHistory() {
        getSupportActionBar().setTitle("Order History");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();

        fragment = new EmptyShoppingCart();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void storeNotFound() {

        getSupportActionBar().setTitle("Store Not Found");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();
        System.out.println("THE STORE IS NOT FOUND AND NOW CHANGING THE FRAGMENT");
        fragment = new StoreNotFound();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void searchProduct() {

        getSupportActionBar().setTitle("Search Product");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();
        System.out.println("THE STORE IS NOT FOUND AND NOW CHANGING THE FRAGMENT");
        fragment = new search_product();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void productNotFound() {

        getSupportActionBar().setTitle("Product Not Found");
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new Fragment();
        System.out.println("THE PRODUCT IS NOT FOUND AND NOW CHANGING THE FRAGMENT");
        fragment = new ProductNotFound();
        transaction.replace(R.id.contentFrameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {


            scannerResult = scanningResult.getContents().toString();
        } else {


            if (resultCode == Activity.RESULT_OK) {

                PaymentConfirmation confirm = intent.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);


                if (confirm != null) {

                    try {
                        String paymentDetails = confirm.toJSONObject().toString(5);
                        paypalResult = "worked";
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    } catch (Exception e) {
                        System.out.println("EXCEPTION " + e.getMessage());
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

            Uri selectedImageUri = intent.getData();
            selectedImagePath = getPath(selectedImageUri);

            ImageView userImage = (ImageView) findViewById(R.id.profileImage);
            ImageView userImage1 = (ImageView) findViewById(R.id.imageViewProfile);

            userImage1.setImageURI(selectedImageUri);
            userImage.setImageURI(selectedImageUri);
        }

        if (requestCode == REQUEST_TAKE_PHOTO) {
            FileOutputStream out = null;
            Bitmap photo = (Bitmap) intent.getExtras().get("data");

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));

            VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
            service.setApiKey("a3b9aeeef34ad848206ba27d6c6f7caad6abfa80");

            System.out.println("Classify an image");
            ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
                    .images(finalFile).build();
            VisualClassification result = service.classify(options).execute();


            boolean wrapInScrollView = true;
            String pictureResult;
            if (result != null) {

                pictureResult = result.getImages().get(0).getClassifiers().get(0).getClasses().get(0).getName();
            } else {
                pictureResult = "";
            }
            SqlConnection connn = new SqlConnection();

            Connection connect1 = connn.connect();

            if (connect1 != null) {

                PreparedStatement getCartProductInfo = null;
                try {
                    if (pictureResult.equalsIgnoreCase("Apple")) {
                        pictureResult = "Apples";
                    }
                    getCartProductInfo = connect1.prepareStatement("Select * from products where Product_Name=?");
                    getCartProductInfo.setString(1, pictureResult);
                    ResultSet verifyCartProductResultSet = getCartProductInfo.executeQuery();
                    if (verifyCartProductResultSet.next()) {
                        MaterialDialog dialog = new MaterialDialog.Builder(this)
                                .title("Product Information").titleColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null))

                                .positiveText("Yes")
                                .customView(R.layout.picture_product, wrapInScrollView)
                                .negativeText("Search")
                                .icon(ResourcesCompat.getDrawable(getResources(), R.drawable.shoppingcartwelcomeicon, null)).maxIconSize(40)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        scannedProductCode = "";

                                        try {
                                            SqlConnection connn = new SqlConnection();
                                            Connection connect = connn.connect();
                                            Connection connect1 = connn.connect();
                                            Connection connect2 = connn.connect();

                                            if (connect != null) {

                                                PreparedStatement getStoreInfo = connect.prepareStatement("Select Product_Id from Products where Product_Name=?");

                                                getStoreInfo.setString(1, "Apples");

                                                ResultSet verifyStoreResultset = getStoreInfo.executeQuery();

                                                if (verifyStoreResultset.next()) {
                                                    scannedProductCode = verifyStoreResultset.getString("Product_Id");
                                                }
                                            } else {

                                            }
                                        } catch (Exception e) {
                                            System.out.println("EXCEPTION OCCURED IN DIALOG BOX POSITIVE BUTTON " + e.getMessage());
                                        }
                                        viewProductInfo();
                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        searchProduct();
                                    }
                                })
                                .build();

                        TextView pictureText = (TextView) dialog.getCustomView().findViewById(R.id.pictureFoundTextView);


                        pictureText.setText(pictureResult.substring(0, 1).toUpperCase() + pictureResult.substring(1));

                        TextView pictureTextDid = (TextView) dialog.getCustomView().findViewById(R.id.didYouMeanTextView);

                        pictureTextDid.setText("Did you mean " + pictureResult.substring(0, 1).toUpperCase() + pictureResult.substring(1) + "?");

                        dialog.show();
                    } else {
                        MaterialDialog dialog = new MaterialDialog.Builder(this)
                                .title("Product Information").titleColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null))
                                .content("Sorry!! we are not able to find your product. But we found "+pictureResult)
                                .positiveText("Try Again!!")
                                .negativeText("Search")
                                .icon(ResourcesCompat.getDrawable(getResources(), R.drawable.shoppingcartwelcomeicon, null)).maxIconSize(40)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);

                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        searchProduct();
                                    }
                                })
                                .build();

                        dialog.show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            } else {

            }


        }


        // }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void signOutActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
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
        if (scannerResult == "") {

            if (paypalResult == "") {

            } else {
                confirmOrder();
                paypalResult = "";
            }
        } else {

            scannedProductCode = scannerResult;

            viewProductInfo();

            scannerResult = "";
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


            double Lat = loc.getLatitude();

            double Long = loc.getLongitude();
            String Text = "";
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            System.out.println("LOCATION IS ALREADY FOUND: LATITUDE IS :" + Lat + " LONGITUDE IS: " + Long);

            try {

                addresses = geocoder.getFromLocation(Lat, Long, 1);
                System.out.println("LOCATION TRACKED TWO");

                if (fragment.getClass().toString().equalsIgnoreCase("class com.example.gurpartap.skip_and_buy.Controller.ShopFragment")) {
                    android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

                    FragmentTransaction transaction = manager.beginTransaction();
                    getSupportActionBar().setTitle("Shop");
                    fragment = new StoreFragment();

                    transaction.replace(R.id.contentFrameMain, fragment);
                    transaction.commitAllowingStateLoss();

                }
                System.out.println("LOCATION TRACKED THREE");
                String address = addresses.get(0).getAddressLine(0);
                String address11 = addresses.get(0).getAddressLine(1);
                String city = addresses.get(0).getLocality();
                Text = "My current location is: " +

                        "Latitud = " + loc.getLatitude() +
                        "Longitud = " + loc.getLongitude() +
                        "address = " + address +
                        "address1 = " + address11 +
                        "city = " + city;

                System.out.println("THE ADDRESS IS: " + address + " " + address11);

                //location=address+" "+address11;
                location = "813 Sheridan College Drive Brampton, ON L6Y 5H9";
                try {
                    mlocManager.removeUpdates(this);
                } catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(),

                            "SECURITY EXCEPTION OCCURED",

                            Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
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
