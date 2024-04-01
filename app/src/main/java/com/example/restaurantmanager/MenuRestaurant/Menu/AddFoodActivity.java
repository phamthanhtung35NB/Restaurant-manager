package com.example.restaurantmanager.MenuRestaurant.Menu;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.UploadImageToFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import model.MenuRestaurant;

public class AddFoodActivity extends AppCompatActivity {
    EditText edtName, edtDescription, edtPrice;
    TextView textViewId;
    ImageView imageViewFood;
    ImageButton imageButtonSave;
    private ProgressBar progressBar;
    // Define a static final int for the camera request code
    //CAMERA_REQUEST_CODE có tác dụng như một ID để xác định rằng bạn đang yêu cầu kết quả từ máy ảnh.
    private static final int CAMERA_REQUEST_CODE = 100;
    public static String accountId = "tung";
    public static String type = "restaurant";
//    public static String imageUrl = "";
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
        edtName = findViewById(R.id.textViewNameOrder);
        edtDescription = findViewById(R.id.textViewDescriptionOrder);
        edtPrice = findViewById(R.id.textViewPriceOrder);
        imageViewFood = findViewById(R.id.imageViewFoodOrder);
        imageButtonSave = findViewById(R.id.imageButtonDelFood);
        progressBar = findViewById(R.id.progressBar);
        // Ẩn ProgressBar
        progressBar.setVisibility(View.GONE);
        textViewId.setText("Auto");
        Intent intent = getIntent();
        accountId = intent.getStringExtra("uid");
        System.out.println("-----------------------====idMaxLong: " + accountId);
    }
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    void addEvents() {
        imageViewFood.setOnClickListener(v -> {
//            TODO: xin cấp quyền máy ảnh + chụp ảnh + lưu ảnh vào strorage+ lấy link + load ảnh
            openCamera();

        });
//        imageButtonSave.setOnClickListener(v -> {
//            System.out.println("URL: " + imageUrl);
//        });
        imageButtonSave.setOnClickListener(v -> {
            //lưu dữ liệu
            String name = edtName.getText().toString();
            String description = edtDescription.getText().toString();
            double price = Double.parseDouble(edtPrice.getText().toString());
            String image = UploadImageToFirebase.imageUrl;

            String id = String.valueOf(1111);

            MenuRestaurant updatedMenuRestaurant = new MenuRestaurant(id, name, description, price, image);
            readIDMaxFromFireBase(updatedMenuRestaurant);

            Toast.makeText(AddFoodActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            edtName.setText("");
            edtDescription.setText("");
            edtPrice.setText("");
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            imageViewFood.setImageBitmap(bitmap);
            UploadImageToFirebase.uploadImageToFirebase(bitmap,"imageMenu_", accountId, progressBar);
            System.out.println("---------------------------------+++++++---------------------");


        }
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