package com.example.restaurantmanager.Client;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.restaurantmanager.Account.LoginActivity;
import com.example.restaurantmanager.Client.Messages.ListMessagesFragment;
import com.example.restaurantmanager.FireBase.FireBase;
import com.example.restaurantmanager.MenuRestaurant.HomeRestaurantFragment;
import com.example.restaurantmanager.MenuRestaurant.MainRestaurantActivity;
import com.example.restaurantmanager.MenuRestaurant.Menu.ShowMenuRestaurantFragment;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
    static boolean isCheckQR = false;
    public static final String SERVER_KEY = "AAAAl-xT4ko:APA91bGASnqgklF4OfVR6ls42PxiSI1Lzj2Aj8qYqdlCgk4LKApgGGpE1oH_GzLgBqjheSfQqHc3_qrdcsT4cwOGAbGCwgdUNpLmLx-tdGLo_NtbC-rZrqiDBtcP5qI6xI_YrefHOAtX";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.main);
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        navigationView = findViewById(R.id.navigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeClientFragment()).commit();
            navigationView.setCheckedItem(R.id.navHome);
        }
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        init();
        addEvents();
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
    void init(){

        replaceFragment(new HomeClientFragment(), true);
        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
//        replaceFragment(new HomeClientActivity());
    }
    void addEvents(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navHome){
                System.out.println("navHome");
                    replaceFragment(new HomeClientFragment(), false);
                    Toast.makeText(MainActivity.this, "navHome", Toast.LENGTH_SHORT).show();
                }else if(itemId==R.id.navMessage){
                    replaceFragment(new ListMessagesFragment(), false);
                }
                else if (itemId == R.id.navQR){
                    if (isCheckQR==false){

                    System.out.println("navSetting");
                    Toast.makeText(MainActivity.this, "navSetting", Toast.LENGTH_SHORT).show();
                    // Xử lý sự kiện khi click vào nút quét mã QR
                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                    integrator.setOrientationLocked(true);
                    integrator.setPrompt("Quét mã QR để xem menu");
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                    integrator.initiateScan();
                    replaceFragment(new HomeRestaurantFragment(), false);
                    }
                }
                return true;
            }
        });
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_header_home) {
                replaceFragment(new ShowMenuRestaurantFragment(), false);
            } else if (itemId == R.id.nav_header_settings) {
                showBottomDialogSetting();

            } else if (itemId == R.id.nav_header_share) {
                Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();

            } else if (itemId == R.id.nav_header_feedback) {
                Toast.makeText(MainActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
                //mở link liên kết github
                Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/phamthanhtung35NB/Restaurant-manager"));
                startActivity(intent);
            } else if(itemId == R.id.nav_header_logout){
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();

                //xóa dữ liệu đăng nhập
                SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                //chuyển màn hình login
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
            return true;
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheckQR==false){
// Xử lý sự kiện khi click vào nút quét mã QR
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setOrientationLocked(true);
                integrator.setPrompt("Quét mã QR để xem menu");
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//            integrator.setCaptureActivity(400);
                integrator.initiateScan();
                replaceFragment(new HomeRestaurantFragment(), false);
                }
            }
        });
    }
    void replaceFragment(Fragment fragment, boolean isAppInit){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!isAppInit){
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }else {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        }

        fragmentTransaction.commit();
    }

    private void showBottomDialogSetting() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bottom_setting_client_layout);

        Switch darkModeSwitch = dialog.findViewById(R.id.darkModeSwitch);
        Switch closedSwitch = dialog.findViewById(R.id.closedSwitch);
//        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
//        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
//        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkModeSwitch.setChecked(true);
        }

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        closedSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(MainActivity.this,"Closed",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,"Open",Toast.LENGTH_SHORT).show();
            }
        });
//        videoLayout.setOnClickListener(v -> {
//            dialog.dismiss();
//            Toast.makeText(MainRestaurantActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();
//        });
//
//        shortsLayout.setOnClickListener(v -> {
//            dialog.dismiss();
//            Toast.makeText(MainRestaurantActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();
//        });
//
//        liveLayout.setOnClickListener(v -> {
//            dialog.dismiss();
//            Toast.makeText(MainRestaurantActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();
//        });

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.anim.slide_out;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * Hàm này sẽ được gọi sau khi quét mã QR xong
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // Xử lý kết quả quét mã QR
            String content = result.getContents();
            if (content != null) {

                //tách chuỗi
                String[] arr = content.split("/");
                String userId = arr[0];
                String numberTable = arr[1];

//                tạo sharedPreferences lưu mã QR
                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("url", content);
                editor.putString("accountId", userId);
                editor.putString("numberTable", numberTable);
                editor.apply();

                //lấy token của nhà hàng từ firebase
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("restaurant").document(userId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Map<String, Object> accountData = documentSnapshot.getData();
                                    String token = accountData.get("token").toString();
                                    Log.d(ContentValues.TAG, "Summmmmmmmmm: " + token);
                                    //gửi thông báo đến nhà hàng
                                    sendNotification(token);
                                } else {
                                    Log.d(ContentValues.TAG, "No such document");
                                }
                            }
                        });
                //chuyển sang màn hình xem menu của nhà hàng\
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragment_container, new MenuClientFragment());


                fragmentTransaction.commitAllowingStateLoss();

//                // Tạo một instance mới của MenuClientFragment
//                MenuClientFragment menuClientFragment = new MenuClientFragment();
//
//
//                // Sử dụng FragmentManager để thay thế Fragment hiện tại bằng MenuClientFragment
//                FragmentManager fragmentManager =getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                // Thay thế và thêm vào back stack
//                fragmentTransaction.replace(R.id.fragment_container, menuClientFragment);
//                fragmentTransaction.addToBackStack(null);
//
//                // Commit thao tác
//                fragmentTransaction.commit();

            } else {

            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Hàm gửi thông báo đến Restaurant
     * @param content Token của nhà hàng
     */
    public static void sendNotification(String content) {
        //lấy token từ uri của user
        String token = FireBase.tokenRtn;
        System.out.println("token: " + token);
        //gửi thông báo đến token
        sendMessageToToken(content,"Thông báo","Có khách hàng mới đến quét mã QR");
        sendMessageFromUser1ToUser2(content,"Thông báo","Có khách hàng mới đến quét mã QR");
    }

    //gửi thông báo in app cho user
    public static void sendMessageFromUser1ToUser2(String user2Token,String title,String body) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create URL instance.
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    // Create connection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // Set method as POST
                    connection.setRequestMethod("POST");
                    // Set headers
                    connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);
                    connection.setRequestProperty("Content-Type", "application/json");
                    // Enable input and output streams
                    connection.setDoOutput(true);
                    // Create the message content
                    String jsonInputString = "{\"to\": \"" + user2Token + "\", \"notification\": {\"title\": \""+title+"\", \"body\": \""+body+"\"}}";
                    // Write the output
                    try(OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                    // Get the response
                    int responseCode = connection.getResponseCode();
                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Log.d(TAG, "Response: " + response.toString());
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, "Error sending FCM message", e);
                }
            }
        });
        thread.start();
    }

    //gửi thông báo đến token
    public static void sendMessageToToken(String token,String title,String body) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create URL instance.
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");

                    // Create connection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set method as POST
                    connection.setRequestMethod("POST");

                    // Set headers
                    connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);
                    connection.setRequestProperty("Content-Type", "application/json");

                    // Enable input and output streams
                    connection.setDoOutput(true);

                    // Create the message content
                    String jsonInputString = "{\"to\": \"" + token + "\", \"notification\": {\"title\": \""+title+"\", \"body\": \""+body+"\"}}";

                    // Write the output
                    try(OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                    // Get the response
                    int responseCode = connection.getResponseCode();
                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Log.d(TAG, "Response: " + response.toString());
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, "Error sending FCM message", e);
                }
            }
        });

        thread.start();
    }
}