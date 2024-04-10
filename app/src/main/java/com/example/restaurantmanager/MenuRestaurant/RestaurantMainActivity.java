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

import com.example.restaurantmanager.Client.HomeClientFragment;
import com.example.restaurantmanager.Client.MainActivity;
import com.example.restaurantmanager.MenuRestaurant.Menu.ShowMenuRestaurantFragment;
import com.example.restaurantmanager.MenuRestaurant.Table.ShowTableRestaurantFragment;
import com.example.restaurantmanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class RestaurantMainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
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
    void init(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        fragmentContainer = findViewById(R.id.fragment_container);
//        replaceFragment(new HomeClientFragment(), false);
        Toast.makeText(RestaurantMainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
//        replaceFragment(new HomeClientActivity());
    }
    void addEvents(){
        //Quản lý thực đơn
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navHome){
                    System.out.println("navHome");
//                    replaceFragment(new ShowMenuRestaurantFragment(), false);
                    Toast.makeText(RestaurantMainActivity.this, "navHome", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.navMenu){
                    System.out.println("navMenu");
                    replaceFragment(new ShowMenuRestaurantFragment(), false);
                    Toast.makeText(RestaurantMainActivity.this, "navMenu", Toast.LENGTH_SHORT).show();
//                    replaceFragment(new MenuClientActivity());
                }else if (itemId == R.id.navTable){
                    System.out.println("navOrder");
                    replaceFragment(new ShowTableRestaurantFragment(), false);
                    Toast.makeText(RestaurantMainActivity.this, "navOrder", Toast.LENGTH_SHORT).show();
                }else if (itemId == R.id.navSetting){
                    System.out.println("navSetting");
                    Toast.makeText(RestaurantMainActivity.this, "navSetting", Toast.LENGTH_SHORT).show();
//                    replaceFragment(new CartClientActivity());
                }
//                replaceFragment(selectedFragment);
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