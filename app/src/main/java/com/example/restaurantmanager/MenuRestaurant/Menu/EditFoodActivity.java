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
import android.widget.Button;
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

import com.example.restaurantmanager.MenuRestaurant.MainRestaurantActivity;
import com.example.restaurantmanager.R;
import com.example.restaurantmanager.FireBase.UploadImageToFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import model.MenuRestaurant;

public class EditFoodActivity extends AppCompatActivity {
    EditText edtName, edtDescription, edtPrice;
    TextView textViewId;
    ImageView imageViewFood;
    Button imageButtonSave;
    private ProgressBar progressBarEditFood;
    String imageShow;

    // Define a static final int for the camera request code
    //CAMERA_REQUEST_CODE có tác dụng như một ID để xác định rằng bạn đang yêu cầu kết quả từ máy ảnh.
    private static final int CAMERA_REQUEST_CODE = 100;
    String accountId = ShowMenuRestaurantFragment.accountId;
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
        System.out.println("đầu init");
        //ánh xạ
        textViewId = findViewById(R.id.textViewId);
        edtName = findViewById(R.id.textViewNameOrder);
        edtDescription = findViewById(R.id.textViewDescriptionOrder);
        edtPrice = findViewById(R.id.textViewPriceOrder);
        imageViewFood = findViewById(R.id.imageViewFoodOrder);
        imageButtonSave = findViewById(R.id.imageButtonDelFood);
        progressBarEditFood = findViewById(R.id.progressBarEditFood);
        // Ẩn ProgressBar
        progressBarEditFood.setVisibility(View.GONE);
        System.out.println("giua init");
        //lấy dữ liệu từ intent và hiển thị lên view
        Intent intent = getIntent();
        textViewId.setText(intent.getStringExtra("id"));
        edtName.setText(intent.getStringExtra("name"));
        edtDescription.setText(intent.getStringExtra("description"));
        double price = intent.getDoubleExtra("price", 0.0);
        String priceString = String.valueOf(price); // Convert double to String
        edtPrice.setText(priceString);
        //lấy hình ảnh từ database
        imageShow=intent.getStringExtra("image");
        //lấy hình ảnh từ database
        if (imageShow.length()>5){
            Picasso.get()
                    .load(imageShow)
                    .into(imageViewFood);
        }else {
            imageViewFood.setImageResource(R.drawable.food_load);
        }
        System.out.println("cuối init");
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
            if (image.length()>5){
                imageShow=image ;
            }
            String id = getIntent().getStringExtra("id");
            MenuRestaurant updatedMenuRestaurant = new MenuRestaurant(id, name, description, price, imageShow);
            updateDataInFireBase(id, updatedMenuRestaurant);
            Intent intent = new Intent(EditFoodActivity.this, MainRestaurantActivity.class);
            startActivity(intent);
            finish();
        });
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
                UploadImageToFirebase.uploadImageToFirebase(bitmap, "imageMenu_", accountId, progressBarEditFood);
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
                    UploadImageToFirebase.uploadImageToFirebase(bitmap, "imageMenu_", accountId, progressBarEditFood);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Update món ăn trong menu của nhà hàng lên Firebase
     * @param menuId id của món ăn cần update (key của map menuRestaurant)
     * @param updatedMenuRestaurant thông tin món ăn mới
     */
    private void updateDataInFireBase(String menuId, MenuRestaurant updatedMenuRestaurant) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(type).document(accountId).update("menuRestaurant." + menuId, updatedMenuRestaurant.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Update thành công cho món ăn: " + menuId);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating data for account: " + accountId, e);
                    }
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
}