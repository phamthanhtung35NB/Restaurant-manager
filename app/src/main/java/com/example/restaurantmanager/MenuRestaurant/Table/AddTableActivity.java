package com.example.restaurantmanager.MenuRestaurant.Table;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Table;

public class AddTableActivity extends AppCompatActivity {

    EditText editTextNameTable,editTextDescribe;
    ImageButton imageButtonAdd;
    ImageView imageViewPhoto;
    String accountId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_table);
        init();
        addEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void init(){
        editTextNameTable = findViewById(R.id.editTextNameTable);
        editTextDescribe = findViewById(R.id.editTextDescribe);
        imageButtonAdd = findViewById(R.id.imageButtonAdd);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        Intent intent = getIntent();
        accountId = intent.getStringExtra("uid");
    }
    void addEvent(){
        imageButtonAdd.setOnClickListener(v -> {
            String name = editTextNameTable.getText().toString();
            String describe = editTextDescribe.getText().toString();
            if(name.isEmpty() || describe.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            readIdMax(accountId);
        });
    }
    void readIdMax(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id);
        myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String id1 = dataSnapshot.child("idMax").getValue().toString();
                //set idMax +1 rồi đẩy lên firebase
                int idMax_ = Integer.parseInt(id1)+1;
                myRef.child("idMax").setValue(idMax_);
                createTable(id,idMax_);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
    void createTable(String id,int idMax){
        String name = editTextNameTable.getText().toString();
        String describe = editTextDescribe.getText().toString();
        if(name.isEmpty() || describe.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        Table table = new Table(idMax,name,describe,"Trống","");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id);
        myRef.push().setValue(table);
        Toast.makeText(this, "Thêm bàn thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}