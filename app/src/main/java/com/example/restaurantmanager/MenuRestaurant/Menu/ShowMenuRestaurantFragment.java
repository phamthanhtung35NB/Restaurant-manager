package com.example.restaurantmanager.MenuRestaurant.Menu;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.restaurantmanager.MenuRestaurant.Order.OderActivity;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import adapter.Restaurant.MenuAdapter;
import model.MenuRestaurant;

public class ShowMenuRestaurantFragment extends Fragment {
    TextView textView1;
//    ImageButton imageButtonOrder;
//    ImageButton imageButtonMenuOp;
    Button imageButtonAdd;
    ListView listViewMenu;

    public static ArrayList<MenuRestaurant> dataRestaurant ;
    public static MenuAdapter menuAdapter;
    SQLiteDatabase database=null;
    public static String accountId = "tung";
    final String type = "restaurant";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_menu_restaurant, container, false);
        init(view);
        addEvents(view);
        return view;
    }

    private void init(View view) {
        System.out.println("đầu tiên");
        //lấy Intent từ activity trước
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
        accountId = sharedPreferences.getString("uid", "");
        System.out.println(accountId);

        textView1 = view.findViewById(R.id.textView1);
//        imageButtonOrder = view.findViewById(R.id.imageButtonOrder);
        listViewMenu = view.findViewById(R.id.listViewMenu);
        dataRestaurant = new ArrayList<>();
        imageButtonAdd = view.findViewById(R.id.imageButtonAdd);
        readDataFromFireBase();
        System.out.println("cuối init");
    }
    //read data from firebase nếu null thì tạo 1 dữ liêu mẫu
    private void readDataFromFireBase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(type).document(accountId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> accountData = documentSnapshot.getData();

                            if (accountData.containsKey("menuRestaurant")) {
                                Map<String, Object> menuRestaurantData = (Map<String, Object>) accountData.get("menuRestaurant");

                                // Lặp qua các mục của menuRestaurantData và tạo các đối tượng MenuRestaurant tương ứng
                                for (Map.Entry<String, Object> entry : menuRestaurantData.entrySet()) {
                                    //key là id của menu và có value key = value id
                                    String menuId = entry.getKey();
                                    Map<String, Object> menuData = (Map<String, Object>) entry.getValue();

                                    // Lấy dữ liệu từ menuData và tạo đối tượng MenuRestaurant
                                    String name = (String) menuData.get("name");
                                    String description = (String) menuData.get("description");
                                    double price = (double) menuData.get("price");
                                    String image = (String) menuData.get("image");
System.out.println("menuId: "+menuId+" name: "+name+" description: "+description+" price: "+price+" image: "+image);
                                    // Tạo đối tượng MenuRestaurant từ dữ liệu thu được
                                    MenuRestaurant menuRestaurant_ = new MenuRestaurant(menuId, name, description, price, image);
                                    dataRestaurant.add(menuRestaurant_);
                                }
                                menuAdapter = new MenuAdapter(getActivity(), R.layout.food, dataRestaurant);
                                listViewMenu.setAdapter(menuAdapter);

                            } else {
                                Log.d(TAG, "No menuRestaurant found for account: " + accountId);
                                MenuRestaurant menuRestaurant_ = new MenuRestaurant("0","Name","Description",0.0,"https://i.imgur.com/ikbFUzX.png");
                                dataRestaurant.add(menuRestaurant_);
                            }
                        } else {
                            Log.d(TAG, "No such account exists with ID: " + accountId);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting account data for ID: " + accountId, e);
                    }
                });
        //print data
//        for (MenuRestaurant menuRestaurant : ShowMenuActivity.dataRestaurant) {
//            System.out.println(menuRestaurant.getId()+" "+menuRestaurant.getName()+" "+menuRestaurant.getDescription()+" "+menuRestaurant.getPrice()+" "+menuRestaurant.getImage());
//        }
        menuAdapter = new MenuAdapter(getActivity(), R.layout.food, dataRestaurant);
        listViewMenu.setAdapter(menuAdapter);
    }


    private void updateDataInFireBase(String menuId, MenuRestaurant updatedMenuRestaurant) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(type).document(accountId)
                .update("menuRestaurant." + menuId, updatedMenuRestaurant.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data updated successfully for account: " + accountId);
                        // Optionally refresh the UI to reflect the changes
//                        refreshData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating data for account: " + accountId, e);
                    }
                });
    }

    private void addEvents(View view) {
//        button1.setOnClickListener(v -> {
////            onClickReadData();
//
//        });
        System.out.println("1111111111111111111111111111111111111111111");
        System.out.println(accountId);
        System.out.println(type);
//        imageButtonOrder.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), OderActivity.class);
//            startActivity(intent);
////            finish();
//        });
//        imageButtonMenuOp.setOnClickListener(v -> {
////            addDataToFireBase(new MenuRestaurant("4", "Cơm chiên", "Cơm chiên + trứng", 5000.0, "https://i.imgur.com/ikbFUzX.png"));
////            updateDataInFireBase("4", new MenuRestaurant("4", "Cơm chiê----n", "Cơm chiên + trứng", 5000.0, "https://i.imgur.com/ikbFUzX.png"));
//            Intent intent = new Intent(getActivity(), AddFoodActivity.class);
//            startActivity(intent);
//        });
        imageButtonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddFoodActivity.class);
            intent.putExtra("uid", accountId);
            startActivity(intent);
//            finish();
        });

    }
}