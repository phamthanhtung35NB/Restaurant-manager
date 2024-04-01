package com.example.restaurantmanager.MenuRestaurant.Table;

import static android.content.ContentValues.TAG;



import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.Client.MenuClientActivity;
import com.example.restaurantmanager.MenuRestaurant.Menu.AddFoodActivity;
import com.example.restaurantmanager.R;
import com.example.restaurantmanager.UploadImageToFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import model.MenuRestaurant;
import model.Table;

public class AddTableActivity extends AppCompatActivity {

    EditText editTextNameTable,editTextDescribe;
    ImageButton imageButtonAdd;
    ImageView imageViewPhoto;
    String accountId = "";
    private ProgressBar progressBar2;
    private static final int CAMERA_REQUEST_CODE = 100;
    int newIdMax;
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
        progressBar2 = findViewById(R.id.progressBar2);
        Intent intent = getIntent();
        accountId = intent.getStringExtra("uid");
        Toast.makeText(this, "idMax: "+accountId, Toast.LENGTH_SHORT).show();
//        readIdMax(accountId);
    }
    void addEvent(){
        imageButtonAdd.setOnClickListener(v -> {
            String name = editTextNameTable.getText().toString();
            String describe = editTextDescribe.getText().toString();
            if(name.isEmpty() || describe.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            readIDMaxFromFireBase();

//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference(accountId);
//            myRef.child("idMax").setValue(newIdMax);
        });
        imageViewPhoto.setOnClickListener(v -> {
            openCamera();
        });
    }
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            imageViewPhoto.setImageBitmap(bitmap);
            UploadImageToFirebase.uploadImageToFirebase(bitmap,"viewTable_", accountId, progressBar2);
            System.out.println("---------------------------------+++++++---------------------");


        }
    }
    private void updateIdMax(long newIdMax ) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("idTableMax", newIdMax);

        db.collection("restaurant").document(accountId)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "ID Max updated successfully!");
                        System.out.println("-----------------------====idMaxLong: set" + newIdMax);

                        createTable(newIdMax);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating ID Max", e);
                    }
                });
    }
    private void readIDMaxFromFireBase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("restaurant").document(accountId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> accountData = documentSnapshot.getData();
                            Long idMaxLong = (Long) accountData.get("idTableMax");
                            idMaxLong++;
                            updateIdMax(idMaxLong);
                        } else {
                            Log.d(TAG, "No such document");

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                    // ... (Mã xử lý lỗi hiện có của bạn)
                });
    }

    void createTable(long newIdMax) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String name = editTextNameTable.getText().toString().trim();
        String describe = editTextDescribe.getText().toString().trim();
        String image = "hghghgh";
        image = UploadImageToFirebase.imageUrl;
        if (name.isEmpty() || describe.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        //ép kiểu
        int newId = (int) newIdMax;
        Table table = new Table(newId, name, describe, "Trống", image);
        DatabaseReference myRef = database.getReference(accountId + "/" + table.getId());
        myRef.setValue(table, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(AddTableActivity.this, "onComplete: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddTableActivity.this, "onComplete: success", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }


//    void readIdMax(String id){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference(id);
//        myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//                String id1 = dataSnapshot.child("idMax").getValue().toString();
//                //set idMax +1 rồi đẩy lên firebase
//                int idMax_ = Integer.parseInt(id1)+1;
//
//                createTable(id,idMax_);
//                myRef.child("idMax").setValue(idMax_);
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//            }
//        });
//    }
//    void createTable(String id,int idMax){
//        String name = editTextNameTable.getText().toString();
//        String describe = editTextDescribe.getText().toString();
//        if(name.isEmpty() || describe.isEmpty()){
//            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Table table = new Table(idMax,name,describe,"Trống","");
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference(accountId+"/"+table.getId()+"");
//        myRef.setValue(table, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError error, DatabaseReference ref) {
//                if (error == null) {
//                    Toast.makeText(AddTableActivity.this, "onComplete: success", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(AddTableActivity.this, "onComplete: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
}