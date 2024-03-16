package com.example.restaurantmanager;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import adapter.MenuAdapter;
import model.Account;
import model.MenuRestaurant;

public class MainActivity extends AppCompatActivity {

    TextView textView1;
    Button button1;

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

        addControls();
        addEvents();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        readDataFromFireBase("account1");
    }

    private void addEvents() {
        button1.setOnClickListener(v -> {
            onClickReadData();
        });

    }

    //push data account and menu to firebase
    private void onClickWriteData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account1");
//        Account string = new Account("1", "admin", "admin", "0123456789","áđâs@fads", "Hà Nội");
        Account menu = new Account("1", "admin", "admin", "0123456789","áđâs@fads", "Hà Nội","1", "Cơm chiên", "Cơm chiên + trứng", 5000.0, "https://i.imgur.com/ikbFUzX.png");
//        MenuRestaurant menu1 = new MenuRestaurant("2", "Cơm trắng", "Cơm", 10000, "https://i.imgur.com/ikbFUzX.png");
        myRef.setValue(menu, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(MainActivity.this, "onComplete: success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "onComplete: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //read data from firebase
    public void readDataFromFireBase(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id1 = dataSnapshot.child("id").getValue().toString();
                String username = dataSnapshot.child("username").getValue().toString();
                String password = dataSnapshot.child("password").getValue().toString();
                textView1.setText(id1 + " " + username + " " + password);
                // lấy dữ liệu từ firebase account1
                //address: "Hà Nội"
                //email: "áđâs@fads"
                //id: "1"
                //menuRestaurant: {1={description="Cơm chiên + trứng", id="1", image="https://i.imgur.com/ikbFUzX.png", name="Cơm chiên", price=5000.0}}
                //password: "admin"
                //phone: "0123456789"
                //username: "admin"
//                Account value = dataSnapshot.getValue(Account.class);
//                if (value != null) {
//                    // access member variables of the Account object
//                    String accountId = value.getId();
//                    String username = value.getUsername();
//                    String password = value.getPassword();
//                    String phone = value.getPhone();
//                    String email = value.getEmail();
//                    String address = value.getAddress();
////                    Toast.makeText(MainActivity.this, "onDataChange: " + accountId + " " + username + " " + password + " " + phone + " " + email + " " + address, Toast.LENGTH_SHORT).show();
//                    // ... and so on for other member variables
//                } else {
//                    // Handle the case where no data is found at the specified node
//                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "onCancelled: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference myRef2 = database.getReference(id+"/menuRestaurant");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    MenuRestaurant menuRestaurant = dataSnapshot1.getValue(MenuRestaurant.class);
                    data.add(menuRestaurant);
                }
                menuAdapter = new MenuAdapter(MainActivity.this, R.layout.food, data);
                listViewMenu.setAdapter(menuAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "onCancelled: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onClickReadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account1");
//        myRef.setValue("Hello, World!");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                Account value = dataSnapshot.getValue(Account.class);
//                Log.d(TAG, "Value is: " + value);
//                textView1.setText(value.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void addControls() {
//        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");


        textView1 = findViewById(R.id.textView1);
        button1 = findViewById(R.id.button1);
        listViewMenu = findViewById(R.id.listViewMenu);
        data = new ArrayList<>();
//        listViewMenu = findViewById(R.id.listViewMenu);
//        data = new ArrayList<>();
//        data.add(new MenuRestaurant("1", "Cơm chiên", "Cơm chiên + trứng", 50000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("2", "Cơm trắng", "Cơm", 10000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("3", "Cá", "Cá rán", 5000.9, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("4", "thịt ", "Thịt bò", 70000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("5", "lẩu", "Lẩu hải sản", 1000000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("6", "Cơm cháy", "Cơm cháy hải sản", 200000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("7", "Cơm chiên", "Cơm chiên hải sản", 50000,"https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("8", "Cơm trắng", "Cơm  hải sản", 10000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("9", "Cá", "Cơm chiên hải sản", 500000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("10", "thịt ", "Cơm  hải sản", 70000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("11", "lẩu", "Cơm chiên hải sản", 1000000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("12", "Cơm cháy", "Cơm  hải sản", 200000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("13", "Cơm chiên", "Cơm chiên hải sản", 50000,"https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("14", "Cơm trắng", "Cơm  hải sản", 10000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("15", "Cá", "Cơm chiên hải sản", 500000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("16", "thịt ", "Cơm  hải sản", 70000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("17", "lẩu", "Cơm chiên hải sản", 1000000, "https://i.imgur.com/ikbFUzX.png"));
//        data.add(new MenuRestaurant("18", "Cơm cháy", "Cơm  hải sản", 200000, "https://i.imgur.com/ikbFUzX.png"));
//        menuAdapter = new MenuAdapter(MainActivity.this, R.layout.food, data);
//        listViewMenu.setAdapter(menuAdapter);
    }

}