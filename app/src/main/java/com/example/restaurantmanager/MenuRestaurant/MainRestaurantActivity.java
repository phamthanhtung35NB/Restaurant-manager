package com.example.restaurantmanager.MenuRestaurant;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import com.example.restaurantmanager.Account.LoginActivity;
import com.example.restaurantmanager.Client.Messages.ListMessagesFragment;
import com.example.restaurantmanager.FireBase.UploadImageToFirebase;
import com.example.restaurantmanager.MenuRestaurant.Menu.ShowMenuRestaurantFragment;
import com.example.restaurantmanager.MenuRestaurant.Messages.ListMessagesRestaurantFragment;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainRestaurantActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    String fragmentCurrent = "";
    FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
//    FrameLayout drawerLayout;
    private FrameLayout fragmentContainer;
    CircleImageView imageLogoAvatar;
    TextView textUsername;
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
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.main);
        fragmentContainer = findViewById(R.id.fragment_container);

        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        imageLogoAvatar = headerView.findViewById(R.id.imageLogoAvatar);
        textUsername = headerView.findViewById(R.id.textUsername);
        textEmail = headerView.findViewById(R.id.textEmail);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeRestaurantFragment()).commit();
            navigationView.setCheckedItem(R.id.navHome);
        }

        replaceFragment(new HomeRestaurantFragment(), true);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.navMenu) {
                replaceFragment(new ShowMenuRestaurantFragment(), false);
            } else if (itemId == R.id.navTable) {
                System.out.println("Table");
                replaceFragment(new ShowTableRestaurantFragment(), false);
            } else if (itemId == R.id.navMessage) {
                replaceFragment(new ListMessagesRestaurantFragment(), false);
            } else if (itemId == R.id.navSetting) {
                System.out.println("Setting");
                showBottomDialogSetting();
//                replaceFragment(new HomeRestaurantFragment(), false);
            } else if(itemId == R.id.navHome){
                replaceFragment(new HomeRestaurantFragment(), false);
            }

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
                replaceFragment(new HomeRestaurantFragment(), false);
            } else if (itemId == R.id.nav_header_settings) {
                showBottomDialogSetting();
                drawerLayout.closeDrawers();

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showBottomDialog();
                replaceFragment(new HomeRestaurantFragment(), false);
            }
        });
        imageLogoAvatar.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
checkForNewMessages();
    }
//    private  void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, fragment);
//        fragmentTransaction.commit();
//    }

    private void checkForNewMessages() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("chat").child(accountId).getChildren()){
                    final String opoUid = dataSnapshot.getKey().trim(); // Lấy khóa của nút con hiện tại uid của người gửi
                    if (!opoUid.equals(accountId)&&snapshot.exists() ) {
                        //snapshot.exists() kiểm tra xem có dữ liệu không
                        String profilePic = snapshot.child( "profilePic").getValue(String.class);
                        System.out.println("profilePic: "+profilePic);
                        String username = snapshot.child("client").getValue(String.class);
                        System.out.println("username: "+username);
//                                    String content = snapshot.getValue(String.class);
                        String content = snapshot.child("lastMessage").getValue(String.class);
                        System.out.println("content: "+content);
                        boolean isSeen = snapshot.child("restaurantSeen").getValue(Boolean.class);
                        if (!isSeen&&!fragmentCurrent.equals("ListMessagesFragment")) {
                            showTopDialogNotification(profilePic, username, content,opoUid,accountId);
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
        //chuyển tới fragment mess
        dialog.dismiss();
        replaceFragment(new ListMessagesFragment(), false);
        fragmentCurrent = "ListMessagesFragment";
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
        SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        String address = sharedPreferences.getString("address", "");
        String location = sharedPreferences.getString("location", "");
        editTextAddress.setText(address);
        textViewLocation.setText(location);
        btnSaveAddress.setOnClickListener(view -> {
            //edit address on firebase firestore
            editAddress(editTextAddress.getText().toString());
            Toast.makeText(MainRestaurantActivity.this,"Save Address",Toast.LENGTH_SHORT).show();
        });
        btnGetLocation.setOnClickListener(view -> {
            Toast.makeText(MainRestaurantActivity.this,"Get Location",Toast.LENGTH_SHORT).show();
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

    void editAddress(String address){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("address", address);

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