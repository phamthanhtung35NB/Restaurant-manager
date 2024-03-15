package com.example.restaurantmanager;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import adapter.MenuAdapter;
import model.MenuRestaurant;

public class MainActivity extends AppCompatActivity {

    ListView listViewMenu;
    ArrayList<MenuRestaurant> data;
    MenuAdapter menuAdapter;
//    TextView textViewLocation;
//    ImageButton imageButtonLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView truyền layout vào
        //R là lớp tài nguyên, layout là thư mục chứa layout, activity_main là file layout
        setContentView(R.layout.activity_main);
        addEvents();
        addControls();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void addEvents() {
    }

    private void addControls() {
        listViewMenu = findViewById(R.id.listViewMenu);
        data = new ArrayList<>();
        data.add(new MenuRestaurant("1", "Cơm chiên", "Cơm chiên + trứng", 50000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("2", "Cơm trắng", "Cơm", 10000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("3", "Cá", "Cá rán", 5000.9, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("4", "thịt ", "Thịt bò", 70000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("5", "lẩu", "Lẩu hải sản", 1000000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("6", "Cơm cháy", "Cơm cháy hải sản", 200000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("7", "Cơm chiên", "Cơm chiên hải sản", 50000,"https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("8", "Cơm trắng", "Cơm  hải sản", 10000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("9", "Cá", "Cơm chiên hải sản", 500000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("10", "thịt ", "Cơm  hải sản", 70000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("11", "lẩu", "Cơm chiên hải sản", 1000000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("12", "Cơm cháy", "Cơm  hải sản", 200000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("13", "Cơm chiên", "Cơm chiên hải sản", 50000,"https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("14", "Cơm trắng", "Cơm  hải sản", 10000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("15", "Cá", "Cơm chiên hải sản", 500000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("16", "thịt ", "Cơm  hải sản", 70000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("17", "lẩu", "Cơm chiên hải sản", 1000000, "https://i.imgur.com/ikbFUzX.png"));
        data.add(new MenuRestaurant("18", "Cơm cháy", "Cơm  hải sản", 200000, "https://i.imgur.com/ikbFUzX.png"));
        menuAdapter = new MenuAdapter(MainActivity.this, R.layout.food, data);
        listViewMenu.setAdapter(menuAdapter);
    }

}