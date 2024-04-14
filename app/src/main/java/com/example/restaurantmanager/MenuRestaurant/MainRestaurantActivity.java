package com.example.restaurantmanager.MenuRestaurant;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.restaurantmanager.MenuRestaurant.Menu.ShowMenuRestaurantFragment;
import com.example.restaurantmanager.Client.Messages.ListMessagesFragment;
import com.example.restaurantmanager.MenuRestaurant.Table.ShowTableRestaurantFragment;
import com.example.restaurantmanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainRestaurantActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
    public static Fragment lastFragment = null; // Biến để lưu trạng thái Fragment cuối cùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_main);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof StatisticalFragment) {
            lastFragment = new HomeRestaurantFragment();
            replaceFragment(lastFragment, false);
        } else {
            super.onBackPressed();
        }
    }
    public Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        return currentFragment;
    }
    void init(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        fragmentContainer = findViewById(R.id.fragment_container);
        if (lastFragment == null || (lastFragment instanceof HomeRestaurantFragment)) {
            lastFragment = new HomeRestaurantFragment();
            replaceFragment(lastFragment, false);
        }
        Toast.makeText(MainRestaurantActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
//        ListMessagesFragment.getProfileData();
    }
    void addEvents(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navHome){
                    replaceFragment(lastFragment, false);
                } else if (itemId == R.id.navMenu){
                    replaceFragment(new ShowMenuRestaurantFragment(), false);
                }else if (itemId == R.id.navTable){
                    replaceFragment(new ShowTableRestaurantFragment(), false);
                }else if(itemId==R.id.navMessage){
                    replaceFragment(new ListMessagesFragment(), false);
                }
                else if (itemId == R.id.navSetting){
                    // Handle the setting fragment here
                }
                return true;
            }
        });
    }
    public void replaceFragment(Fragment fragment, boolean isAppInit){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!isAppInit){
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }else {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        }

        fragmentTransaction.commit();
    }
}