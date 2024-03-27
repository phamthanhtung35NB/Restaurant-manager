package com.example.restaurantmanager;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import model.MenuRestaurant;

public class EditFoodActivity extends AppCompatActivity {
    EditText edtName, edtDescription, edtPrice;
    TextView textViewId;
    ImageView imageViewFood;
    ImageButton imageButtonSave;
    String accountId = MainActivity.accountId;
    public static final String type = "restaurant";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_food);
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

        Intent intent = getIntent();
        textViewId.setText(intent.getStringExtra("id"));
        edtName.setText(intent.getStringExtra("name"));
        edtDescription.setText(intent.getStringExtra("description"));
        double price = intent.getDoubleExtra("price", 0.0);
        String priceString = String.valueOf(price); // Convert double to String
        edtPrice.setText(priceString);

        //lấy hình ảnh từ database
        Picasso.get().load(intent.getStringExtra("image")).into(imageViewFood);

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
            String id = getIntent().getStringExtra("id");
            MenuRestaurant updatedMenuRestaurant = new MenuRestaurant(id, name, description, price, image);
            updateDataInFireBase(id, updatedMenuRestaurant);
            Intent intent = new Intent(EditFoodActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
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
}