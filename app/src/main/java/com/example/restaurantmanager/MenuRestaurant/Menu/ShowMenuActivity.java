package com.example.restaurantmanager.MenuRestaurant.Menu;

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

import com.example.restaurantmanager.MenuRestaurant.Order.OderActivity;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Map;

import adapter.MenuAdapter;
import model.MenuRestaurant;

public class ShowMenuActivity extends AppCompatActivity {

    TextView textView1;
//    Button button1;
    ImageButton imageButtonOrder;
    ImageButton imageButtonMenuOp;
    ImageButton imageButtonAdd;
    ListView listViewMenu;

    public static ArrayList<MenuRestaurant> dataRestaurant ;

    public static MenuAdapter menuAdapter;
    SQLiteDatabase database=null;

    public static String accountId = "tung";
    final String type = "restaurant";
//String accountId ;
//    String type ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView truyền layout vào
        //R là lớp tài nguyên, layout là thư mục chứa layout, activity_main là file layout
        setContentView(R.layout.activity_show_menu);
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

    //read data from firebase nếu null thì tạo 1 dữ liêu mẫu
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
                                    System.out.println("-----------------------------(-1))");
                                }
                                System.out.println("-----------------------------0");
                                menuAdapter = new MenuAdapter(ShowMenuActivity.this, R.layout.food, dataRestaurant);
                                listViewMenu.setAdapter(menuAdapter);

                                System.out.println("----------------------1");
                            } else {
                                Log.d(TAG, "No menuRestaurant found for account: " + accountId);
                                System.out.println("----------------------No menuRestaurant found for account: " + accountId);
                                MenuRestaurant menuRestaurant_ = new MenuRestaurant("0","Name","Description",0.0,"https://i.imgur.com/ikbFUzX.png");
                                dataRestaurant.add(menuRestaurant_);
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
        //print data
        for (MenuRestaurant menuRestaurant : ShowMenuActivity.dataRestaurant) {
            System.out.println(menuRestaurant.getId()+" "+menuRestaurant.getName()+" "+menuRestaurant.getDescription()+" "+menuRestaurant.getPrice()+" "+menuRestaurant.getImage());
        }
        menuAdapter = new MenuAdapter(ShowMenuActivity.this, R.layout.food, dataRestaurant);
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

    private void addEvents() {
//        button1.setOnClickListener(v -> {
////            onClickReadData();
//
//        });
        System.out.println("1111111111111111111111111111111111111111111");
        System.out.println(accountId);
        System.out.println(type);
        imageButtonOrder.setOnClickListener(v -> {
            Intent intent = new Intent(ShowMenuActivity.this, OderActivity.class);
            startActivity(intent);
            finish();
        });
        imageButtonMenuOp.setOnClickListener(v -> {
//            addDataToFireBase(new MenuRestaurant("4", "Cơm chiên", "Cơm chiên + trứng", 5000.0, "https://i.imgur.com/ikbFUzX.png"));
//            updateDataInFireBase("4", new MenuRestaurant("4", "Cơm chiê----n", "Cơm chiên + trứng", 5000.0, "https://i.imgur.com/ikbFUzX.png"));
        Intent intent = new Intent(ShowMenuActivity.this, AddFoodActivity.class);
        startActivity(intent);
        });
        imageButtonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ShowMenuActivity.this, AddFoodActivity.class);
            intent.putExtra("uid", accountId);
            startActivity(intent);
            finish();
        });

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
        Cursor c = database.query("account", null, null, null, null, null, null);
        while (c.moveToNext()) {
            String data = c.getString(0) + "-" + c.getString(1) + "-" + c.getString(2) + "-" + c.getString(3) + "-" + c.getString(4) + "-" + c.getString(5);
            textView1.setText(data);
            System.out.println(data);
            return;
        }
    }
    private void init() {
        //lấy Intent từ activity trước
        Intent intent = getIntent();
        accountId = intent.getStringExtra("uid");
//        type = intent.getStringExtra("type");
        System.out.println("000000000000000000000000000000");
        System.out.println(accountId);
        System.out.println(type);
        textView1 = findViewById(R.id.textView1);
//        button1 = findViewById(R.id.button1);
        imageButtonOrder = findViewById(R.id.imageButtonOrder);
        listViewMenu = findViewById(R.id.listViewMenu);
//        listViewOrder = findViewById(R.id.listViewOrder);
        dataRestaurant = new ArrayList<>();
        imageButtonMenuOp = findViewById(R.id.imageButtonMenuOp);
        imageButtonAdd = findViewById(R.id.imageButtonAdd);

//        listViewMenu.setAdapter(menuAdapter);
    }

}