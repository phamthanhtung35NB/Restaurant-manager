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
import com.example.restaurantmanager.MenuRestaurant.Table.ShowTableActivity;
import com.example.restaurantmanager.R;

import model.HistoryRestaurant;

public class HomeRestaurantActivity extends AppCompatActivity {

    LinearLayout lineButtonsMenu;
    LinearLayout lineButtonsTable;
    LinearLayout lineButtonsStatistical;
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
        lineButtonsStatistical = findViewById(R.id.lineButtonsStatistical);
        Intent intentGet = getIntent();
        accountId = intentGet.getStringExtra("uid");
        type = intentGet.getStringExtra("type");
        HistoryRestaurant.readSumDayFromFireBase(accountId);
    }
    void addEvents(){
        //Quản lý thực đơn
        lineButtonsMenu.setOnClickListener(v -> {
            Intent intent = new Intent(HomeRestaurantActivity.this, ShowMenuActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("uid", accountId);
            startActivity(intent);
        });
        //Quản lý bàn ăn
        lineButtonsTable.setOnClickListener(v -> {
            Intent intent = new Intent(HomeRestaurantActivity.this, ShowTableActivity.class);
            intent.putExtra("uid", accountId);
            startActivity(intent);
        });
        //Thống kê
        lineButtonsStatistical.setOnClickListener(v -> {
            Intent intent = new Intent(HomeRestaurantActivity.this, StatisticalActivity.class);
            intent.putExtra("sumDay", HistoryRestaurant.sumDay);
            startActivity(intent);
        });
    }
}