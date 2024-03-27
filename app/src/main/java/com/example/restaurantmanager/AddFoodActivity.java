package com.example.restaurantmanager;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import model.MenuRestaurant;

public class AddFoodActivity extends AppCompatActivity {
    EditText edtName, edtDescription, edtPrice;
    TextView textViewId;
    ImageView imageViewFood;
    ImageButton imageButtonSave;
    public static String accountId = "tung";
    public static String type = "restaurant";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_food);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void init() {
        textViewId = findViewById(R.id.textViewId);
        edtName = findViewById(R.id.edtName);
        edtDescription = findViewById(R.id.edtDescription);
        edtPrice = findViewById(R.id.edtPrice);
        imageViewFood = findViewById(R.id.imageViewFood);
        imageButtonSave = findViewById(R.id.imageButtonSave);
        textViewId.setText("Auto");
        Intent intent = getIntent();
        accountId = intent.getStringExtra("uid");
        System.out.println("-----------------------====idMaxLong: " + accountId);
    }
    void addEvents() {
        imageViewFood.setOnClickListener(v -> {
//            TODO: xin cấp quyền máy ảnh + chụp ảnh + lưu ảnh vào strorage(name: đầu gmail+id)+ lấy link + load ảnh

        });
        imageButtonSave.setOnClickListener(v -> {
            //lưu dữ liệu
            String name = edtName.getText().toString();
            String description = edtDescription.getText().toString();
            double price = Double.parseDouble(edtPrice.getText().toString());
            String image = getIntent().getStringExtra("image");

            String id = String.valueOf(1111);

            MenuRestaurant updatedMenuRestaurant = new MenuRestaurant(id, name, description, price, image);
            readIDMaxFromFireBase(updatedMenuRestaurant);
            Toast.makeText(AddFoodActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            edtName.setText("");
            edtDescription.setText("");
            edtPrice.setText("");
        });
    }
    private void addDataToFireBase(MenuRestaurant menuRestaurant) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(type).document(accountId)
                .update("menuRestaurant." + menuRestaurant.getId(), menuRestaurant.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data added successfully for account: " + accountId);
                        System.out.println("-----------------------====idMaxLong: add " + menuRestaurant.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding data for account: " + accountId, e);
                    }
                });
    }
    private void updateIdMax(long newIdMax,MenuRestaurant menuRestaurant) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("idMax", newIdMax);

        db.collection(type).document(accountId)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "ID Max updated successfully!");
                        Toast.makeText(AddFoodActivity.this, "ID Max updated successfully!", Toast.LENGTH_SHORT).show();
                        System.out.println("-----------------------====idMaxLong: set" + newIdMax);
                        menuRestaurant.setId(String.valueOf(newIdMax));
                        addDataToFireBase( menuRestaurant);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating ID Max", e);
                    }
                });
    }

    private void readIDMaxFromFireBase(MenuRestaurant menuRestaurant) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(type).document(accountId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> accountData = documentSnapshot.getData();
                            Long idMaxLong = (Long) accountData.get("idMax");
                            idMaxLong++;
                            updateIdMax(idMaxLong,menuRestaurant);
                        Toast.makeText(AddFoodActivity.this, "idMaxInt: " + idMaxLong, Toast.LENGTH_SHORT).show();
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


}