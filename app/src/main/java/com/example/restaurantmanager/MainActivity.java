package com.example.restaurantmanager;

import static android.content.ContentValues.TAG;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Map;

import adapter.MenuAdapter;
import model.MenuRestaurant;

public class MainActivity extends AppCompatActivity {

    TextView textView1;
//    Button button1;
    ImageButton imageButtonOrder;

    ListView listViewMenu;

    public static ArrayList<MenuRestaurant> dataRestaurant ;

    public static MenuAdapter menuAdapter;
    SQLiteDatabase database=null;

    public static String accountId = "tung";
    public static String type = "restaurant";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView truyền layout vào
        //R là lớp tài nguyên, layout là thư mục chứa layout, activity_main là file layout
        setContentView(R.layout.activity_main);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        readDataFromFireBase();
        addAccountLoginFromSQLite();
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
                                    dataRestaurant.add(menuRestaurant_);
                                }
                                menuAdapter = new MenuAdapter(MainActivity.this, R.layout.food, dataRestaurant);
                                listViewMenu.setAdapter(menuAdapter);


                            } else {
                                Log.d(TAG, "No menuRestaurant found for account: " + accountId);
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
        System.out.println("-------------------------------------------------------------");
        for (MenuRestaurant menuRestaurant : MainActivity.dataRestaurant) {
            System.out.println(menuRestaurant.getId()+" "+menuRestaurant.getName()+" "+menuRestaurant.getDescription()+" "+menuRestaurant.getPrice()+" "+menuRestaurant.getImage());
        }
        menuAdapter = new MenuAdapter(MainActivity.this, R.layout.food, dataRestaurant);
                listViewMenu.setAdapter(menuAdapter);
    }



    private void addEvents() {
//        button1.setOnClickListener(v -> {
////            onClickReadData();
//
//        });
        imageButtonOrder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OderActivity.class);
            startActivity(intent);
        });
        System.out.println("----------------------------1---------------------------------");


    }

    //push data account and menu to firebase
//    private void onClickWriteData() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("account1");
////        Account string = new Account("1", "admin", "admin", "0123456789","áđâs@fads", "Hà Nội");
//        Account menu = new Account("1", "admin", "admin", "0123456789","áđâs@fads", "Hà Nội","1", "Cơm chiên", "Cơm chiên + trứng", 5000.0, "https://i.imgur.com/ikbFUzX.png");
////        MenuRestaurant menu1 = new MenuRestaurant("2", "Cơm trắng", "Cơm", 10000, "https://i.imgur.com/ikbFUzX.png");
//        myRef.setValue(menu, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError error, DatabaseReference ref) {
//                if (error == null) {
//                    Toast.makeText(MainActivity.this, "onComplete: success", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "onComplete: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
    

    //read data from firebase
//    public void readDataFromFireBase(String id){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference(id);
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String id1 = dataSnapshot.child("id").getValue().toString();
//                String username = dataSnapshot.child("username").getValue().toString();
//                String password = dataSnapshot.child("password").getValue().toString();
//                textView1.setText(id1 + " " + username + " " + password);
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Toast.makeText(MainActivity.this, "onCancelled: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        DatabaseReference myRef2 = database.getReference(id+"/menuRestaurant");
//        myRef2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    MenuRestaurant menuRestaurant = dataSnapshot1.getValue(MenuRestaurant.class);
//                    dataRestaurant.add(menuRestaurant);
//                }
//                menuAdapter = new MenuAdapter(MainActivity.this, R.layout.food, dataRestaurant);
//                listViewMenu.setAdapter(menuAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Toast.makeText(MainActivity.this, "onCancelled: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
////        listViewMenu.setAdapter(menuAdapter);
//    }
//    public void onClickReadData() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("account1");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
////                Account value = dataSnapshot.getValue(Account.class);
////                Log.d(TAG, "Value is: " + value);
////                textView1.setText(value.toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
////                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//    }

    void addAccountLoginFromSQLite() {
        database = openOrCreateDatabase("menurestaurant.db", MODE_PRIVATE, null);
        System.out.println("----------------------------2---------------------------------");
        Cursor c = database.query("account", null, null, null, null, null, null);
        System.out.println("----------------------------3---------------------------------");
        while (c.moveToNext()) {
            String data = c.getString(0) + "-" + c.getString(1) + "-" + c.getString(2) + "-" + c.getString(3) + "-" + c.getString(4) + "-" + c.getString(5);
            textView1.setText(data);
            System.out.println(data);
        }
    }
    private void init() {
        textView1 = findViewById(R.id.textView1);
//        button1 = findViewById(R.id.button1);
        imageButtonOrder = findViewById(R.id.imageButtonOrder);
        listViewMenu = findViewById(R.id.listViewMenu);
//        listViewOrder = findViewById(R.id.listViewOrder);
        dataRestaurant = new ArrayList<>();


//        listViewMenu.setAdapter(menuAdapter);
    }

}