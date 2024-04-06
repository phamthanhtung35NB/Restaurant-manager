package com.example.restaurantmanager.Client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.restaurantmanager.MenuRestaurant.Order.OderActivity;
import com.example.restaurantmanager.Notifications.MyFirebaseMessagingService;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapter.OrderAdapter;
import adapter.OrderClientAdapter;
import model.HistoryRestaurant;
import model.MenuRestaurant;

public class OrderClientActivity extends AppCompatActivity {

    public static ListView listViewOrderClient;

    public static OrderClientAdapter orderClientAdapter;
    public static ArrayList<MenuRestaurant> dataOrderClient;

    public static double tong = 0;
    Button buttonThanhToanClient;
    TextView textThongTinBanClient;
    public static String URL = "";
    public static String table = "";
    public static String accountId = "";
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
        accountId = arr[0];
        table = arr[1];
        String a = "Bàn: " +table;
        HistoryRestaurant.checkSumDayAndInitialization(accountId);
        HistoryRestaurant.checkSumAndInitialization(accountId, table);
        textThongTinBanClient.setText(a);
        HomeClientActivity.sendNotification(accountId);
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
    public static void removeOrder() {
        // Khởi tạo Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Lấy reference đến order
        DatabaseReference orderRef = database.getReference(URL);

        // Xóa tất cả các child bên trong order
        orderRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Xóa thành công
                System.out.println("Xóa order thành công!");
            } else {
                // Xóa thất bại
                Exception e = task.getException();
                System.out.println("Xóa order thất bại: " + e.getMessage());
            }
        });
    }
    void addEvents() {
        buttonThanhToanClient.setOnClickListener(v -> {
            // Xử lý sự kiện khi click vào nút thanh toán
            //xóa dữ liệu preferences my_preferences
            SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("key");
            editor.apply();
            //tính tổng price trên firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference refOrder = database.getReference(URL);
            refOrder.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Lặp qua tất cả child
                    double tong = 0;
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // Tạo instance mới của ClassTable
                        MenuRestaurant menuOrder = new MenuRestaurant();
                        if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {
                            menuOrder.setPrice(childSnapshot.child("price").getValue(Double.class));
                            tong += menuOrder.getPrice();
                        }
                    }

                    //tính tổng price
                    System.out.println("tong: " + tong);
                    //xóa dữ liệu trên firebase
//                    refOrder.removeValue();
                    //chuyển sang activity thanh toán
                    OrderClientActivity.tong = tong;
                    textThongTinBanClient.setText("Tổng tiền: " + tong);
                    HistoryRestaurant.addHistory(dataOrderClient, accountId, table, (long) tong);
                    //xóa dữ liệu brrn trong url trên firebase
                    removeOrder();

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi
                    System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
                }

            });
        });
    }
}