package com.example.restaurantmanager.MenuRestaurant.Table;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.MenuRestaurant.HomeRestaurantActivity;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapter.TableAdapter;
import model.Table;

public class ShowTableActivity extends AppCompatActivity {
    GridView gvTable;
    ImageButton imageButtonExit,imageButtonAddTable;
    TextView textViewSummary;
    public static ArrayList<Table> arrTableData;
    TableAdapter adapterTable;
    public static String accountId = "";
    public static int idTableMax = 0;
    String type = "restaurant";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dinner_table);
        init();
        addEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        readDataFromFireBase(accountId);
    }
    public void init(){
        Intent intent = getIntent();
        accountId = intent.getStringExtra("uid");

        gvTable = findViewById(R.id.gvTable);
        imageButtonExit = findViewById(R.id.imageButtonExit);
        textViewSummary = findViewById(R.id.textViewSummary);
        imageButtonAddTable = findViewById(R.id.imageButtonAddTable);
        arrTableData = new ArrayList<>();
        System.out.println("------------------------------------------------------");
        System.out.println("accountId: " + accountId);

    }
    public void addEvent(){
        imageButtonExit.setOnClickListener(v -> {
            Intent intent = new Intent(ShowTableActivity.this, HomeRestaurantActivity.class);
            startActivity(intent);
            finish();
        });
        System.out.println("hết event-----------------------------");
        imageButtonAddTable.setOnClickListener(v -> {
            Intent intent = new Intent(ShowTableActivity.this, AddTableActivity.class);
            intent.putExtra("uid",accountId);
            intent.putExtra("idMax",idTableMax);
            startActivity(intent);

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


    /**
     * Đọc dữ liệu table từ Firebase
     * @param accountId uid của node cha trong realtime database của restaurant
     *           uid này được truyền từ HomeRestaurantActivity khi click vào button Table của restaurant
     */
    public void readDataFromFireBase(String accountId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(accountId);
        // Lắng nghe giá trị của tất cả child
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // lặp qua tất cả child
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // tạo instance mới của ClassTable
                    Table table = new Table();
                    if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {

                        table.setId(childSnapshot.child("id").getValue(Integer.class));
                        table.setName(childSnapshot.child("name").getValue(String.class));
                        table.setDescribe(childSnapshot.child("describe").getValue(String.class));
                        table.setStateEmpty(childSnapshot.child("stateEmpty").getValue(String.class));
                        table.setImage(childSnapshot.child("image").getValue(String.class));

                        // thêm từng bàn vào mảng arrTableData
                        arrTableData.add(table);
                    }
                }

                adapterTable = new TableAdapter(ShowTableActivity.this, R.layout.table, arrTableData);
                gvTable.setAdapter(adapterTable);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
            }
        });
    }

//    public void readDataFromFireBase(String id){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef2 = database.getReference(id);
//        myRef2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                System.out.println("111111111111111111111111111");
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    System.out.println("222222222222222222222222222");
//                    Table table = dataSnapshot1.getValue(Table.class);
//                    System.out.println("333333333333333333333333333");
//                    arrTableData.add(table);
//                    System.out.println("444444444444444444444444444");
//                }
//                System.out.println("555555555555555555555555555");
//                adapterTable = new TableAdapter(DinnerTableActivity.this, R.layout.table, arrTableData);
//                gvTable.setAdapter(adapterTable);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Toast.makeText(DinnerTableActivity.this, "onCancelled: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
////        listViewMenu.setAdapter(menuAdapter);
//    }

}