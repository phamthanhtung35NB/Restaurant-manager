package com.example.restaurantmanager.MenuRestaurant;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import com.example.restaurantmanager.Account.LoginActivity;
import com.example.restaurantmanager.Client.Messages.ChatFragment;
import com.example.restaurantmanager.Client.Messages.ListMessagesFragment;
import com.example.restaurantmanager.FireBase.UploadImageToFirebase;
import com.example.restaurantmanager.MenuRestaurant.Menu.ShowMenuRestaurantFragment;
import com.example.restaurantmanager.MenuRestaurant.Messages.ChatRestaurantFragment;
import com.example.restaurantmanager.MenuRestaurant.Messages.ListMessagesRestaurantFragment;
import com.example.restaurantmanager.MenuRestaurant.Order.OderActivity;
import com.example.restaurantmanager.MenuRestaurant.Table.ShowTableRestaurantFragment;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.appcheck.internal.util.Logger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.checkerframework.common.subtyping.qual.Bottom;
import org.osmdroid.util.GeoPoint;

import java.util.HashMap;
import java.util.Map;

import adapter.Restaurant.TableAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Table;

public class MainRestaurantActivity extends AppCompatActivity {

    // Tạo một Dialog mới
    Dialog dialog;
    private LocationManager locationManager; // Quản lý vị trí
    private static final int PICK_IMAGE_REQUEST = 1;
    private LocationListener locationListener; // Lắng nghe thay đổi vị trí
    String fragmentCurrent = "";
//    FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
//    FrameLayout drawerLayout;
    private FrameLayout fragmentContainer;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1; // Mã yêu cầu quyền
    CircleImageView imageLogoAvatar;
    TextView textUsername;
    String locationRestaurant = "";
    TextView textEmail;
    String profilePic = "";
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    public static Fragment lastFragment = null; // Biến để lưu trạng thái Fragment cuối cùng
    String accountId = "";
//    Animation slideInAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
//    Animation slideOutAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        slideInAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in)
//        slideOutAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out);;
//        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.main);
        fragmentContainer = findViewById(R.id.fragment_container);

        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        imageLogoAvatar = headerView.findViewById(R.id.imageLogoAvatar);
        textUsername = headerView.findViewById(R.id.textUsername);
        textEmail = headerView.findViewById(R.id.textEmail);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                    lấy vị trí hiện tại
                locationRestaurant = "" + location.getLatitude() + "_" + location.getLongitude();
                System.out.println("Location: " + locationRestaurant);
//                editAddress(locationRestaurant,"location");
            }

            // Override other methods as needed
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
        // Kiểm tra và yêu cầu quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(MainRestaurantActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(MainRestaurantActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StatisticalFragment()).commit();
            navigationView.setCheckedItem(R.id.navHome);
        }

        replaceFragment(new StatisticalFragment(), true);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.navMenu) {
                fragmentCurrent = "ShowMenuRestaurantFragment";
                replaceFragment(new ShowMenuRestaurantFragment(), false);
            } else if (itemId == R.id.navTable) {
                System.out.println("Table");
                fragmentCurrent = "ShowTableRestaurantFragment";
                replaceFragment(new ShowTableRestaurantFragment(), false);
            } else if (itemId == R.id.navMessage) {
                replaceFragment(new ListMessagesRestaurantFragment(), false);
                fragmentCurrent = "ListMessagesFragment";
            } else if (itemId == R.id.navSetting) {
                System.out.println("Setting");
                showBottomDialogSetting();
//                replaceFragment(new HomeRestaurantFragment(), false);
            }
            else if(itemId == R.id.navStatistical){
                fragmentCurrent = "StatisticalFragment";
                replaceFragment(new StatisticalFragment(), false);
            }
//            else if(itemId == R.id.navHome){
//                fragmentCurrent = "HomeRestaurantFragment";
//                replaceFragment(new HomeRestaurantFragment(), false);}

            return true;
        });
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
        //set sự kiện cho navigation bên trái
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_header_home) {
                //đóng navigation
                drawerLayout.closeDrawers();
                fragmentCurrent = "HomeRestaurantFragment";
                replaceFragment(new StatisticalFragment(), false);
            } else if (itemId == R.id.nav_header_settings) {
                showBottomDialogSetting();
                drawerLayout.closeDrawers();
                fragmentCurrent = "HomeRestaurantFragment";
            } else if (itemId == R.id.nav_header_share) {
                Toast.makeText(MainRestaurantActivity.this, "Share", Toast.LENGTH_SHORT).show();

            } else if (itemId == R.id.nav_header_feedback) {
                //mở link liên kết github
                Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/phamthanhtung35NB/Restaurant-manager"));
                startActivity(intent);
            } else if(itemId == R.id.nav_header_logout){
                //xóa dữ liệu đăng nhập
//                SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                //chuyển màn hình login
                Intent intent = new Intent(MainRestaurantActivity.this, LoginActivity.class);
                startActivity(intent);

            }
            return true;
        });

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                showBottomDialog();
//                replaceFragment(new HomeRestaurantFragment(), false);
//            }
//        });
        imageLogoAvatar.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
        checkForNewMessages();
        checkForNewNotification();
    }
//    private  void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, fragment);
//        fragmentTransaction.commit();
//    }

    public void showDialogThongBaoLenMon(int id){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_center_show_tb_call_food);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == Gravity.BOTTOM) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        TextView tvNotification = dialog.findViewById(R.id.tvNotification);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        Button btnToTable = dialog.findViewById(R.id.btnToTable);
        tvNotification.setText("Bàn "+id+" đã gửi thông báo lên món!");
        btnClose.setOnClickListener(v -> {
            System.out.println("Đóng dialog");
            dialog.dismiss();
        });
        btnToTable.setOnClickListener(v -> {
            dialog.dismiss();
            //add id bàn vào SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("dataTable", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("idTable", String.valueOf(id));
            editor.commit();
            Intent intent = new Intent(MainRestaurantActivity.this, OderActivity.class);
            String qr = accountId+"/"+String.valueOf(id)+"/order";
            intent.putExtra("url", qr);
            startActivity(intent);
        });

        dialog.show();
    }
    private void checkForNewNotification() {
            System.out.println("đầu readDataFromFireBase");
            System.out.println("accountId: " + accountId);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(accountId);
            // Lắng nghe giá trị của tất cả child
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // lặp qua tất cả child
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // tạo instance mới của ClassTable
                        if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {

                            if (childSnapshot.child("stateEmpty").getValue(String.class).equals("Đang Đợi Món")){
                                //show dialog thông báo
                                showDialogThongBaoLenMon(childSnapshot.child("id").getValue(Integer.class));
                            }
                        }
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi
                    System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
                }
            });
    }

    private void checkForNewMessages() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("chat").child(accountId).getChildren()){
                    final String opoUid = dataSnapshot.getKey().trim(); // Lấy khóa của nút con hiện tại uid của người gửi
                    System.out.println("opoUid: "+opoUid);
                    if (!opoUid.equals(accountId)&&snapshot.exists() ) {
                        //snapshot.exists() kiểm tra xem có dữ liệu không
                        String profilePic = dataSnapshot.child( "profilePic").getValue(String.class);
                        String username = dataSnapshot.child("client").getValue(String.class);
                        String content = dataSnapshot.child("lastMessage").getValue(String.class);
                        String phone = dataSnapshot.child("phone").getValue(String.class);
                        boolean isSeen = dataSnapshot.child("restaurantSeen").getValue(Boolean.class);

                        if (!isSeen&&!fragmentCurrent.equals("ListMessagesFragment")) {
                            //kiểm tra xem có dialog nào đang hiển thị không
                            if (dialog != null && dialog.isShowing()) {
                                // Dialog đang hiển thị
                                dialog.dismiss();
                                // Phát âm thanh thông báo
                                MediaPlayer mediaPlayer = MediaPlayer.create(MainRestaurantActivity.this, R.raw.nofi);
                                mediaPlayer.start();
                                showTopDialogNotification(profilePic, username, content, opoUid, accountId, phone);
                            } else {
                                // Không có Dialog nào đang hiển thị
                                // Phát âm thanh thông báo
                                MediaPlayer mediaPlayer = MediaPlayer.create(MainRestaurantActivity.this, R.raw.nofi);
                                mediaPlayer.start();
                                showTopDialogNotification(profilePic, username, content, opoUid, accountId, phone);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(Logger.TAG, "Listen failed.", error.toException());
            }
        });
    }
private void showTopDialogNotification(String profilePic, String username, String content, String otherUid, String accountId, String phone) {
    dialog = new Dialog(this);
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
System.out.println("___________________");
    System.out.println("chatKey: "+otherUid);
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
        //chuyển tới fragment mess
        dialog.dismiss();
        // Tạo một instance mới của ChatFragment
        ChatRestaurantFragment chatFragment = new ChatRestaurantFragment();

        // Tạo một Bundle để chứa dữ liệu
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("phone", phone);
        bundle.putString("profilePic",profilePic);
        bundle.putString("chatKey", otherUid);

        // Đặt Arguments cho Fragment
        chatFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, chatFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



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
    // Đặt vị trí cho Dialog ở phía trên
    dialog.getWindow().setGravity(Gravity.TOP);
}
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
        Uri uri = data.getData();
        try {
            // Update your ImageView.
            if (uri != null){
                //upload image to firebase storage
                imageLogoAvatar.setImageURI(uri);
                //get bitmap from uri
                Bitmap bitmap = UploadImageToFirebase.getBitmapFromUri(uri, this);
                UploadImageToFirebase.uploadImageLogoAvataFirebase(bitmap,"logo_", accountId);

            }else {
//                imageLogoAvatar.setImageURI(uri);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    public void replaceFragment(Fragment fragment, boolean isAppInit){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        System.out.println("Fragment: " + fragment);
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
        dialog.setContentView(R.layout.dialog_bottom_setting_restaurant_layout);

        Switch darkModeSwitch = dialog.findViewById(R.id.darkModeSwitch);
        Switch closedSwitch = dialog.findViewById(R.id.closedSwitch);
//        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
//        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
//        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        EditText editTextAddress = dialog.findViewById(R.id.editTextAddress);
        Button btnSaveAddress = dialog.findViewById(R.id.btnSaveAddress);
        TextView textViewLocation = dialog.findViewById(R.id.textViewLocation);
        Button btnGetLocation = dialog.findViewById(R.id.btnGetLocation);
        Button btnSaveDescription = dialog.findViewById(R.id.btnSaveDescription);
        EditText textViewDescription = dialog.findViewById(R.id.textViewDescription);
        SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        String address = sharedPreferences.getString("address", "");
        String location = sharedPreferences.getString("location", "");
        String description = sharedPreferences.getString("description", "");
        editTextAddress.setText(address);
        textViewLocation.setText(location);
        textViewDescription.setText(description);
        btnSaveAddress.setOnClickListener(view -> {
            //edit address on firebase firestore
            editAddress(editTextAddress.getText().toString(),"address");
            //set data sharedpreferences address
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("address", editTextAddress.getText().toString());
            editor.apply();
            editTextAddress.setText(editTextAddress.getText().toString());
            Toast.makeText(MainRestaurantActivity.this,"Save Address",Toast.LENGTH_SHORT).show();
        });
        btnGetLocation.setOnClickListener(view -> {
            // Kiểm tra và yêu cầu quyền truy cập vị trí
            if (ActivityCompat.checkSelfPermission(MainRestaurantActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                // Request location permission if not granted
                ActivityCompat.requestPermissions(MainRestaurantActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
            }
            //
            editAddress(locationRestaurant,"location");

            Toast.makeText(MainRestaurantActivity.this,"Get Location",Toast.LENGTH_SHORT).show();
            //set data sharedpreferences location
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("location", locationRestaurant);
            editor.apply();
            textViewLocation.setText(locationRestaurant);
        });
        btnSaveDescription.setOnClickListener(view -> {
            //edit address on firebase firestore
            editAddress(textViewDescription.getText().toString(),"description");
            //set data sharedpreferences description
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("description", textViewDescription.getText().toString());
            editor.apply();
            textViewDescription.setText(textViewDescription.getText().toString());
            Toast.makeText(MainRestaurantActivity.this,"Save Description",Toast.LENGTH_SHORT).show();
        });
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
                Toast.makeText(MainRestaurantActivity.this,"Closed",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainRestaurantActivity.this,"Open",Toast.LENGTH_SHORT).show();
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

    void editAddress(String address, String type){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put(type, address);

        db.collection("restaurant").document(accountId)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "updated successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating ", e);
                    }
                });
    }
    private void showBottomDialog() {
    final Dialog dialog = new Dialog(this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.dialog_bottom_sheet_layout);

    LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
    LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
    LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
    ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

    videoLayout.setOnClickListener(v -> {
        dialog.dismiss();
        Toast.makeText(MainRestaurantActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();
    });

    shortsLayout.setOnClickListener(v -> {
        dialog.dismiss();
        Toast.makeText(MainRestaurantActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();
    });

    liveLayout.setOnClickListener(v -> {
        dialog.dismiss();
        Toast.makeText(MainRestaurantActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();
    });

    cancelButton.setOnClickListener(view -> dialog.dismiss());

    dialog.show();
    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().getAttributes().windowAnimations = R.anim.slide_out;
    dialog.getWindow().setGravity(Gravity.BOTTOM);
}
//    @Override
//    public void onBackPressed() {
//        Fragment currentFragment = getCurrentFragment();
//        if (currentFragment instanceof StatisticalFragment) {
//            lastFragment = new HomeRestaurantFragment();
//            replaceFragment(lastFragment, false);
//        } else {
//            super.onBackPressed();
//        }
//    }
//    public Fragment getCurrentFragment(){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//        return currentFragment;
//    }
//    void init(){
//
//
//
////        bottomNavigationView.setBackground(null);
//        fragmentContainer = findViewById(R.id.fragment_container);
//        if (lastFragment == null || (lastFragment instanceof HomeRestaurantFragment)) {
//            lastFragment = new HomeRestaurantFragment();
//            replaceFragment(lastFragment, false);
//        }
//        Toast.makeText(MainRestaurantActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
//
//    }
//    void addEvents(){
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
//                int itemId = item.getItemId();
//                if (itemId == R.id.navHome){
//                    replaceFragment(lastFragment, false);
//                } else if (itemId == R.id.navMenu){
//                    replaceFragment(new ShowMenuRestaurantFragment(), false);
//                }else if (itemId == R.id.navTable){
//                    replaceFragment(new ShowTableRestaurantFragment(), false);
//                }else if(itemId==R.id.navMessage){
//                    replaceFragment(new ListMessagesRestaurantFragment(), false);
//                }
//                else if (itemId == R.id.navSetting){
//                    // Handle the setting fragment here
//                }
//                return true;
//            }
//        });
//
//
//    }
//    public void replaceFragment(Fragment fragment, boolean isAppInit){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        if (!isAppInit){
//            fragmentTransaction.replace(R.id.fragment_container, fragment);
//        }else {
//            fragmentTransaction.add(R.id.fragment_container, fragment);
//        }
//
//        fragmentTransaction.commit();
//    }
}