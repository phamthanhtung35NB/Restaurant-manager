package com.example.restaurantmanager;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import adapter.MenuAdapter;
import model.MenuRestaurant;

public class OderActivity extends AppCompatActivity {

    public static ListView listViewOrder;

    public static MenuAdapter oderAdapter;
    public static ArrayList<MenuRestaurant> dataOrder;
    ImageButton imageButtonExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_oder);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        oderAdapter = new MenuAdapter(OderActivity.this, R.layout.food, dataOrder);
        listViewOrder.setAdapter(oderAdapter);
    }
    void init(){
        listViewOrder = findViewById(R.id.listViewOrder);
        imageButtonExit = findViewById(R.id.imageButtonExit);
        dataOrder = new ArrayList<>();
//        MainActivity.dataOrder = new ArrayList<>();
    }
    void addEvents(){
        imageButtonExit.setOnClickListener(v -> {
            //chuyển màn hình
            Intent intent = new Intent(OderActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }
}