package com.example.restaurantmanager.MenuRestaurant.Order;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import adapter.Client.MenuClientAdapter;
import model.MenuRestaurant;

public class MenuAddFoodToOrderActivity extends AppCompatActivity {
    public static ListView listViewClient;

    public static MenuClientAdapter menuClientAdapter;
    public static ArrayList<MenuRestaurant> dataMenuViewClient;
    TextView textViewInformation;
    ImageButton imageButtonThanhToan;
    public static String accountId = "";
    public static String type = "restaurant";
    public static String numberTable = "";
    public static String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu_add_restaurant);

        init();
        addEvents();
    }

    void init() {
        System.out.println("đầu init MenuAddFoodToOrderFragment");
        textViewInformation = findViewById(R.id.textViewInformation);
        textViewInformation.setText(URL);
        listViewClient = findViewById(R.id.listViewClient);
        imageButtonThanhToan = findViewById(R.id.imageButtonThanhToan);
        dataMenuViewClient = new ArrayList<>();
        // get data login
        SharedPreferences sharedPreferences = this.getSharedPreferences("dataLogin", this.MODE_PRIVATE);
        accountId = sharedPreferences.getString("uid", "");
        System.out.println("accountId: " + accountId);
        readDataFromFireBase();
        System.out.println("cuối init");
    }

    void addEvents() {
        imageButtonThanhToan.setOnClickListener(v -> {
            Toast.makeText(MenuAddFoodToOrderActivity.this, "Thanh toán", Toast.LENGTH_SHORT).show();
        });
    }
    //get menu from firebase fileStore
    private void readDataFromFireBase() {
        System.out.println("đầu readDataFromFireBase");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(type).document(accountId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> accountData = documentSnapshot.getData();

                            if (accountData.containsKey("menuRestaurant")) {
                                Map<String, Object> menuRestaurantData = (Map<String, Object>) accountData.get("menuRestaurant");

                                // Lặp qua các mục của menuRestaurantData và tạo các đối tượng MenuRestaurant tương ứng
                                for (Map.Entry<String, Object> entry : menuRestaurantData.entrySet()) {
                                    String menuId = entry.getKey();
                                    Map<String, Object> menuData = (Map<String, Object>) entry.getValue();

                                    // Lấy dữ liệu từ menuData và tạo đối tượng MenuRestaurant
                                    String name = (String) menuData.get("name");
                                    String description = (String) menuData.get("description");
                                    double price = (double) menuData.get("price");
                                    String image = (String) menuData.get("image");

                                    // Tạo đối tượng MenuRestaurant từ dữ liệu thu được
                                    MenuRestaurant menuRestaurant_ = new MenuRestaurant(menuId, name, description, price, image);
                                    dataMenuViewClient.add(menuRestaurant_);
                                    System.out.println("-----------------------------(-1))");
                                }
                                System.out.println("-----------------------------0");
                                menuClientAdapter = new MenuClientAdapter(MenuAddFoodToOrderActivity.this, R.layout.food_show_client, dataMenuViewClient);
                                listViewClient.setAdapter(menuClientAdapter);

                                System.out.println("----------------------1");
                            } else {
                                Log.d(TAG, "No menuRestaurant found for account: " + accountId);
                                System.out.println("----------------------No menuRestaurant found for account: " + accountId);
                                MenuRestaurant menuRestaurant_ = new MenuRestaurant("0","Name","Description",0.0,"https://i.imgur.com/ikbFUzX.png");
                                dataMenuViewClient.add(menuRestaurant_);
                            }
                        } else {
                            System.out.println("----------------------2");
                            Log.d(TAG, "No such account exists with ID: " + accountId);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting account data for ID: " + accountId, e);
                        System.out.println("------------------------------3");
                    }
                });

    }
}