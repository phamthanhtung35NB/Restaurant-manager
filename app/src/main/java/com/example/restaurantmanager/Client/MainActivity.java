package com.example.restaurantmanager.Client;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.example.restaurantmanager.FireBase.UploadImageToFirebase;
import com.example.restaurantmanager.MenuRestaurant.HomeRestaurantFragment;
import com.example.restaurantmanager.MenuRestaurant.MainRestaurantActivity;
import com.example.restaurantmanager.MenuRestaurant.Menu.ShowMenuRestaurantFragment;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
    static boolean isCheckQR = false;
    CircleImageView imageLogoAvatar;
    TextView textUsername;
    TextView textEmail;
    String profilePic = "";
    String accountId = "";
    public static final String SERVER_KEY = "AAAAl-xT4ko:APA91bGASnqgklF4OfVR6ls42PxiSI1Lzj2Aj8qYqdlCgk4LKApgGGpE1oH_GzLgBqjheSfQqHc3_qrdcsT4cwOGAbGCwgdUNpLmLx-tdGLo_NtbC-rZrqiDBtcP5qI6xI_YrefHOAtX";
    private static final int PICK_IMAGE_REQUEST = 1;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    //fragment hiện tại
    String fragmentCurrent = "";
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


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        //thanh bên
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        imageLogoAvatar = headerView.findViewById(R.id.imageLogoAvatar);
        textUsername = headerView.findViewById(R.id.textUsername);
        textEmail = headerView.findViewById(R.id.textEmail);
        SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        accountId = sharedPreferences.getString("uid", "");
        profilePic= sharedPreferences.getString("profilePic", "");
        textUsername.setText(username);
        textEmail.setText(email);
        if (!profilePic.isEmpty()&&profilePic!=null&&profilePic.length()>0){
            Picasso.get().load(profilePic).into(imageLogoAvatar);
        }
        init();
        addEvents();
    }
    void init(){

        replaceFragment(new HomeClientFragment(), true);
        fragmentCurrent = "HomeClientFragment";
        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
//        replaceFragment(new HomeClientActivity());
        checkForNewMessages();
    }
    void addEvents(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navHome){
                System.out.println("navHome");
                    replaceFragment(new HomeClientFragment(), false);
                    fragmentCurrent = "HomeClientFragment";
                    Toast.makeText(MainActivity.this, "navHome", Toast.LENGTH_SHORT).show();
                }else if(itemId==R.id.navMessage){
                    replaceFragment(new ListMessagesFragment(), false);
                    fragmentCurrent = "ListMessagesFragment";
                }
                else if (itemId == R.id.navQR){
                    if (isCheckQR==false){
                    fragmentCurrent = "QRFragment";
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
        imageLogoAvatar.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
        // Tạo một Dialog mới
        final Dialog dialog = new Dialog(this);
        // Yêu cầu không hiển thị tiêu đề cho Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt layout cho Dialog từ file XML
        dialog.setContentView(R.layout.dialog_bottom_setting_client_layout);

        // Tìm các thành phần trong layout của Dialog
        Switch darkModeSwitch = dialog.findViewById(R.id.darkModeSwitch);
        Switch closedSwitch = dialog.findViewById(R.id.closedSwitch);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        // Kiểm tra xem chế độ tối hiện tại có được bật hay không
        // Nếu có, đặt trạng thái của switch cho chế độ tối là checked
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkModeSwitch.setChecked(true);
        }

        // Đặt listener cho sự kiện thay đổi trạng thái của switch cho chế độ tối
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Nếu switch được chọn, bật chế độ tối
            // Nếu không, tắt chế độ tối
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Đặt listener cho sự kiện thay đổi trạng thái của switch khác
        closedSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Hiển thị thông báo tương ứng với trạng thái của switch
            if (isChecked) {
                Toast.makeText(MainActivity.this,"Closed",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,"Open",Toast.LENGTH_SHORT).show();
            }
        });

        // Đặt listener cho sự kiện click vào nút hủy
        // Khi nút hủy được nhấn, Dialog sẽ bị đóng
        cancelButton.setOnClickListener(view -> dialog.dismiss());

        // Hiển thị Dialog
        dialog.show();
        // Đặt kích thước cho Dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // Đặt màu nền cho Dialog là trong suốt
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Đặt hiệu ứng cho Dialog
        dialog.getWindow().getAttributes().windowAnimations = R.anim.slide_out;
        // Đặt vị trí cho Dialog ở phía dưới
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void checkForNewMessages() {

        databaseReference.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    final String otherUid = dataSnapshot.getKey().trim(); // Lấy khóa của nút con hiện tại
                    if (!otherUid.equals(accountId) && dataSnapshot.child("type").getValue(String.class).trim().equals("restaurant")) {
                        DatabaseReference messageRef = databaseReference.child("chat").child(otherUid).child(accountId);//.child("lastMessage");
                        messageRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String profilePic = snapshot.child( "profilePic").getValue(String.class);
                                    System.out.println("profilePic: "+profilePic);
                                    String username = snapshot.child("restaurant").getValue(String.class);
                                    System.out.println("username: "+username);
//                                    String content = snapshot.getValue(String.class);
                                    String content = snapshot.child("lastMessage").getValue(String.class);
                                    System.out.println("content: "+content);
                                    boolean isSeen = snapshot.child("clientSeen").getValue(Boolean.class);
                                    if (!isSeen&&!fragmentCurrent.equals("ListMessagesFragment")) {
                                        showTopDialogNotification(profilePic, username, content,otherUid,accountId);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w(TAG, "Listen failed.", error.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Listen failed.", error.toException());
            }
        });
    }
    private void showTopDialogNotification(String profilePic, String username, String content, String otherUid, String accountId) {
        // Tạo một Dialog mới
        final Dialog dialog = new Dialog(this);
        // Yêu cầu không hiển thị tiêu đề cho Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt layout cho Dialog từ file XML
        dialog.setContentView(R.layout.dialog_top_message_notification_layout);

        // Tìm các thành phần trong layout của Dialog
        CircleImageView profilePicFrom = dialog.findViewById(R.id.profilePicFrom);
        TextView textViewFrom = dialog.findViewById(R.id.textViewFrom);
        TextView textViewContent = dialog.findViewById(R.id.textViewContent);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        LinearLayout ll = dialog.findViewById(R.id.ll);

        //set data
        if (profilePic!=null&&profilePic.length()>0){
            Picasso.get().load(profilePic).into(profilePicFrom);
        }else {
            profilePicFrom.setImageResource(R.drawable.account);
        }
        textViewFrom.setText(username);
        textViewContent.setText(content);
        // khi ấn vào ll thì mở fragment
        ll.setOnClickListener(view -> {
            //chuển tới fragment mess

                });
        // Đặt listener cho sự kiện click vào nút hủy
        // Khi nút hủy được nhấn, Dialog sẽ bị đóng và cập nhật trạng thái đã đọc
        cancelButton.setOnClickListener(view -> {
            dialog.dismiss();
//            databaseReference.child("chat").child(accountId).child(username).child("clientSeen").setValue(true);
//            databaseReference.child("chat").child(otherUid).child(accountId).child("clientSeen").setValue(true);
        });
//        cancelButton.setOnClickListener(view -> dialog.dismiss());

        // Hiển thị Dialog
        dialog.show();
        // Đặt kích thước cho Dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // Đặt màu nền cho Dialog là trong suốt
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Đặt hiệu ứng cho Dialog
        dialog.getWindow().getAttributes().windowAnimations = R.anim.slide_in_notification;
        // Đặt vị trí cho Dialog ở phía dưới
        dialog.getWindow().setGravity(Gravity.TOP);
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                // Update your ImageView.
                if (uri != null){
                    //upload image to firebase storage
                    imageLogoAvatar.setImageURI(uri);
                    //get bitmap from uri
                    Bitmap bitmap = UploadImageToFirebase.getBitmapFromUri(uri, this);

                    UploadImageToFirebase.uploadImageLogoClientAvataFirebase(bitmap,"logo_", accountId);

                }else {
//                imageLogoAvatar.setImageURI(uri);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
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