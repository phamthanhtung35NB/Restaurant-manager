package com.example.restaurantmanager.MenuRestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.MenuRestaurant.Menu.ShowMenuActivity;
import com.example.restaurantmanager.MenuRestaurant.Table.DinnerTableActivity;
import com.example.restaurantmanager.R;

public class HomeRestaurantActivity extends AppCompatActivity {

    LinearLayout lineButtonsMenu;
    LinearLayout lineButtonsTable;
    String accountId = "";
    String type = "restaurant";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_restaurant);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void init(){
        lineButtonsMenu = findViewById(R.id.lineButtonsMenu);
        lineButtonsTable = findViewById(R.id.lineButtonsTable);
        Intent intentGet = getIntent();
        accountId = intentGet.getStringExtra("uid");
        type = intentGet.getStringExtra("type");
    }
    void addEvents(){
        lineButtonsMenu.setOnClickListener(v -> {

            Intent intent = new Intent(HomeRestaurantActivity.this, ShowMenuActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("uid", accountId);
            startActivity(intent);
        });
        lineButtonsTable.setOnClickListener(v -> {
            Intent intent = new Intent(HomeRestaurantActivity.this, DinnerTableActivity.class);
            intent.putExtra("uid", accountId);
            startActivity(intent);
        });
    }
}