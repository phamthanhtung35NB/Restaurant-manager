package com.example.restaurantmanager.Client;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import adapter.MenuClientAdapter;
import model.MenuRestaurant;
import model.SetTableStateEmptyRealtime;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuClientFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MenuClientFragment extends Fragment {
    public static ListView listViewClient;

    public static MenuClientAdapter menuClientAdapter;
    public static ArrayList<MenuRestaurant> dataMenuViewClient;
    TextView textViewInformation;
    ImageButton imageButtonGioHang;
    public static String accountId = "";
    public static String type = "restaurant";
    public static String numberTable = "";
    public static String URL = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_client, container, false);
        init(view);
        addEvents(view);
        return view;
    }

    void init(View view) {
        SharedPreferences preferences = getActivity().getSharedPreferences("my_data", getActivity().MODE_PRIVATE);
        String text = preferences.getString("key", "");
        URL=text;
        String[] parts = text.split("/");

        // Truy cập từng chuỗi con
        accountId = parts[0];
        numberTable= parts[1];
        String order = parts[2];


        textViewInformation = view.findViewById(R.id.textViewInformation);
        textViewInformation.setText(text);
        listViewClient = view.findViewById(R.id.listViewClient);
        imageButtonGioHang = view.findViewById(R.id.imageButtonGioHang);
        dataMenuViewClient = new ArrayList<>();
//        menuClientAdapter = new MenuClientAdapter(this, R.layout.food_show_client, dataMenuViewClient);
//        listViewClient.setAdapter(menuClientAdapter);
        SetTableStateEmptyRealtime.setTableIsUsing(accountId,numberTable,"Đang sử dụng");
        readDataFromFireBase();

    }
    void addEvents() {
        imageButtonGioHang.setOnClickListener(v -> {
            Intent intent = new Intent(MenuClientActivity.this, OrderClientActivity.class);
            startActivity(intent);
        });
    }

    private void readDataFromFireBase() {
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
                                menuClientAdapter = new MenuClientAdapter(MenuClientActivity.this, R.layout.food_show_client, dataMenuViewClient);
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