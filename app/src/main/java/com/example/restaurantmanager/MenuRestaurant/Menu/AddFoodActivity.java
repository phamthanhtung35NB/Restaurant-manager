package com.example.restaurantmanager.MenuRestaurant.Menu;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    void addEvents() {
        imageViewFood.setOnClickListener(v -> {
            openCamera();
        });
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

    /**
     * Hàm openCamera() dùng để mở ứng dụng máy ảnh mặc định của thiết bị.
     * Khi người dùng chụp ảnh, ảnh sẽ được trả về dưới dạng Bitmap.
     *
     */
    private void openCamera() {
        // Tạo Intent để mở ứng dụng máy ảnh mặc định
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Kiểm tra xem thiết bị có ứng dụng máy ảnh để xử lý Intent không
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Gửi Intent để mở ứng dụng máy ảnh và chụp ảnh
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            // Trường hợp không có ứng dụng máy ảnh nào được tìm thấy, thông báo cho người dùng
            Toast.makeText(this, "Không tìm thấy ứng dụng máy ảnh.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Hàm onActivityResult() được gọi sau khi một Activity khác trả về kết quả cho Activity hiện tại.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     *                    Đây là mã yêu cầu mà bạn đã gửi với startActivityForResult().
     *                    Nó giúp bạn xác định rằng kết quả này đến từ đâu.
     *                    Ví dụ: nếu bạn gửi yêu cầu với mã 100, bạn sẽ nhận lại mã 100 ở đây.
     *
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     *                   Đây là mã kết quả trả về từ Activity con thông qua phương thức setResult().
     *                   Ví dụ: nếu Activity con trả về kết quả thành công, bạn sẽ nhận được RESULT_OK.
     *                   Nếu Activity con trả về kết quả thất bại, bạn sẽ nhận được RESULT_CANCELED.
     *                   Nếu Activity con trả về kết quả thành công và có dữ liệu, bạn cũng sẽ nhận được RESULT_OK.
     *
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *             Một Intent, có thể trả về dữ liệu kết quả cho người gọi
     *              (các dữ liệu khác nhau có thể được đính kèm vào Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Lấy dữ liệu ảnh từ Intent
            Bundle extras = data.getExtras();
            if (extras != null) {
                // Lấy ảnh từ dữ liệu ảnh
                Bitmap bitmap = (Bitmap) extras.get("data");
                // Hiển thị ảnh trong ImageView
                imageViewFood.setImageBitmap(bitmap);
                // Upload ảnh lên Firebase
                UploadImageToFirebase.uploadImageToFirebase(bitmap, "imageMenu_", accountId, progressBar);
            } else {
                // Nếu dữ liệu ảnh trả về từ Intent không có, thực hiện lấy ảnh gốc từ Camera
                Uri imageUri = data.getData();
                try {
                    // Sử dụng ImageReader để đọc ảnh gốc từ Camera
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    // Hiển thị ảnh trong ImageView
                    imageViewFood.setImageBitmap(bitmap);

                    // Upload ảnh lên Firebase
                    UploadImageToFirebase.uploadImageToFirebase(bitmap, "imageMenu_", accountId, progressBar);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//
//            imageViewFood.setImageBitmap(bitmap);
//            UploadImageToFirebase.uploadImageToFirebase(bitmap,"imageMenu_", accountId, progressBar);
//            System.out.println("---------------------------------+++++++---------------------");
//
//
//        }
//    }


    ///////////////////////////////////////////////ADD MENU TO FIREBASE////////////////////////////////////////////
    /**
     * Get ID Max from Firebase
     * Update ID Max in Firebase
     * Add data to Firebase
     */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Đọc ID Max từ Firebase
     * @param menuRestaurant thông tin món ăn mới
     *
     */
    private void readIDMaxFromFireBase(MenuRestaurant menuRestaurant) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(type).document(accountId).get()
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
                });
    }

    /**
     * Update ID Max trong Firebase
     * @param newIdMax ID Max mới
     * @param menuRestaurant thông tin món ăn mới
     */
    private void updateIdMax(long newIdMax,MenuRestaurant menuRestaurant) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("idMax", newIdMax);

        db.collection(type).document(accountId).update(data)
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

    /**
     * Update món ăn trong menu của nhà hàng lên Firebase
     *
     * @param menuRestaurant thông tin món ăn mới
     */
    private void addDataToFireBase(MenuRestaurant menuRestaurant) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(type).document(accountId).update("menuRestaurant." + menuRestaurant.getId(), menuRestaurant.toMap())
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

}