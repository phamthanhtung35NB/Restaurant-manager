package com.example.restaurantmanager.Client;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.MenuRestaurant.Order.OderActivity;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapter.OrderAdapter;
import adapter.OrderClientAdapter;
import model.MenuRestaurant;

public class OrderClientActivity extends AppCompatActivity {

    public static ListView listViewOrderClient;

    public static OrderClientAdapter orderClientAdapter;
    public static ArrayList<MenuRestaurant> dataOrderClient;
    Button buttonThanhToanClient;
    TextView textThongTinBanClient;
    public static String URL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_client);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        readDataFromFireBase();
    }
    void init() {
        listViewOrderClient = findViewById(R.id.listViewOrderClient);
        dataOrderClient = new ArrayList<>();
        buttonThanhToanClient = findViewById(R.id.buttonThanhToanClient);
        textThongTinBanClient = findViewById(R.id.textThongTinBanClient);
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        String text = preferences.getString("key", "");
        URL = text;
//        tách chuôiz
        String[] arr = text.split("/");
        String a = "Bàn: " + arr[1];
        textThongTinBanClient.setText(a);
    }
    void readDataFromFireBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refOrder = database.getReference(URL);
        refOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lặp qua tất cả child
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Tạo instance mới của ClassTable
                    MenuRestaurant menuOrder = new MenuRestaurant();
                    System.out.println("000000000000000000000000");
                    if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {
                        System.out.println("1111111111111111111111");
                        menuOrder.setId(childSnapshot.child("id").getValue(String.class));
                        System.out.println("2222222222222222222222");
                        menuOrder.setName(childSnapshot.child("name").getValue(String.class));
                        System.out.println("3333333333333333333333");
                        menuOrder.setDescription(childSnapshot.child("describe").getValue(String.class));
                        System.out.println("4444444444444444444444");
                        menuOrder.setImage(childSnapshot.child("image").getValue(String.class));
                        System.out.println("5555555555555555555555");
                        menuOrder.setPrice(childSnapshot.child("price").getValue(Double.class));
                        System.out.println("6666666666666666666666");
                        menuOrder.toString();
                        dataOrderClient.add(menuOrder);

                    }
                }
                orderClientAdapter = new OrderClientAdapter(OrderClientActivity.this, R.layout.food_show_order_client, dataOrderClient);
                listViewOrderClient.setAdapter(orderClientAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
            }
        });
    }
    void addEvents() {
        buttonThanhToanClient.setOnClickListener(v -> {
            // Xử lý sự kiện khi click vào nút thanh toán
        });
    }
}