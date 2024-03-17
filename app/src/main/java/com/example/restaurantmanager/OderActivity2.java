package com.example.restaurantmanager;

//import static com.example.restaurantmanager.MainActivity.oderAdapter;

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

public class OderActivity2 extends AppCompatActivity {

    public static ListView listViewOrder;

    public static MenuAdapter oderAdapter;
    ImageButton imageButtonExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_oder2);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        oderAdapter = new MenuAdapter(OderActivity2.this, R.layout.food, MainActivity.dataOrder);
        listViewOrder.setAdapter(oderAdapter);
    }
    void init(){
        listViewOrder = findViewById(R.id.listViewOrder);
        imageButtonExit = findViewById(R.id.imageButtonExit);
//        MainActivity.dataOrder = new ArrayList<>();
    }
    void addEvents(){
        imageButtonExit.setOnClickListener(v -> {
            //chuyển màn hình
            Intent intent = new Intent(OderActivity2.this, MainActivity.class);
            startActivity(intent);
        });

    }
}