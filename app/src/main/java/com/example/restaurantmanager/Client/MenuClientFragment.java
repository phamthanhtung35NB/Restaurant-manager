package com.example.restaurantmanager.Client;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantmanager.MenuRestaurant.Order.OderActivity;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import adapter.Client.MenuClientAdapter;
import model.MenuRestaurant;
import model.SetTableStateEmptyRealtime;


public class MenuClientFragment extends Fragment {
    public static ListView listViewClient;

    public static MenuClientAdapter menuClientAdapter;
    public static ArrayList<MenuRestaurant> dataMenuViewClient;
//    TextView textViewInformation;
    Button imageButtonFragmentOder;
//    ImageButton imageButtonBack;
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
        System.out.println("đầu init");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        String ktra = sharedPreferences.getString("url", "");
        if (ktra.length()>3) {
            System.out.println("vào ______________");
            URL = ktra;
            System.out.println("URL: "+URL);
            accountId = sharedPreferences.getString("accountId", "");
            System.out.println("accountId: "+accountId);
            numberTable = sharedPreferences.getString("numberTable", "");
        }
//        textViewInformation = view.findViewById(R.id.textViewInformation);
//        textViewInformation.setText(URL);
        listViewClient = view.findViewById(R.id.listViewClient);
        imageButtonFragmentOder = view.findViewById(R.id.imageButtonFragmentOder);
//        imageButtonBack = view.findViewById(R.id.imageButtonBack);
        dataMenuViewClient = new ArrayList<>();
        MainActivity.isCheckQR= true;
//        menuClientAdapter = new MenuClientAdapter(this, R.layout.food_show_client, dataMenuViewClient);
//        listViewClient.setAdapter(menuClientAdapter);
        SetTableStateEmptyRealtime.setTableIsUsing(accountId,numberTable,"Đã Quét QR");
        readDataFromFireBase();
        System.out.println("cuối init");
    }

    void addEvents(View view) {
        //button GIỎ HÀNG
        imageButtonFragmentOder.setOnClickListener(v -> {
            // Tạo một Bundle để chứa dữ liệu
            Bundle bundle = new Bundle();
            bundle.putString("url", URL);
            System.out.println("URL: "+URL);
            bundle.putString("accountId", accountId);
            System.out.println("accountId: "+accountId);
            bundle.putString("numberTable", numberTable);
            System.out.println("numberTable: "+numberTable);

            // Tạo một instance mới của FragmentC
            OrderClientFragment fragmentC = new OrderClientFragment();

            // Đặt Arguments cho Fragment
            fragmentC.setArguments(bundle);

            // Sử dụng FragmentManager để thay thế Fragment hiện tại bằng FragmentC
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Thay thế và thêm vào back stack
            fragmentTransaction.replace(R.id.fragment_container, fragmentC);
            fragmentTransaction.addToBackStack(null);

            // Commit thao tác
            fragmentTransaction.commit();
        });
//        imageButtonBack.setOnClickListener(v -> {
//            super.getActivity().onBackPressed();
//        });
    }

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
                                menuClientAdapter = new MenuClientAdapter(getActivity(), R.layout.food_show_client, dataMenuViewClient);
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