package com.example.restaurantmanager.MenuRestaurant;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
//import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import com.example.restaurantmanager.Client.MainActivity;
import com.example.restaurantmanager.MenuRestaurant.Menu.ShowMenuRestaurantFragment;
import com.example.restaurantmanager.Client.Messages.ListMessagesFragment;
import com.example.restaurantmanager.MenuRestaurant.Messages.ListMessagesRestaurantFragment;
import com.example.restaurantmanager.MenuRestaurant.Table.ShowTableRestaurantFragment;
import com.example.restaurantmanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainRestaurantActivity extends AppCompatActivity {
    FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
//    FrameLayout drawerLayout;
    private FrameLayout fragmentContainer;
    public static Fragment lastFragment = null; // Biến để lưu trạng thái Fragment cuối cùng

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
//                replaceFragment(new HomeRestaurantFragment(), false);
            } else if(itemId == R.id.navHome){
                replaceFragment(new HomeRestaurantFragment(), false);
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
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
//    private  void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, fragment);
//        fragmentTransaction.commit();
//    }
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

    private void showBottomDialog() {
    final Dialog dialog = new Dialog(this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.bottomsheetlayout);

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