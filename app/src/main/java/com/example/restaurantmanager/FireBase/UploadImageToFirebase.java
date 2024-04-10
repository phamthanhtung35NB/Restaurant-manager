package com.example.restaurantmanager.FireBase;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.restaurantmanager.MenuRestaurant.Menu.AddFoodActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class UploadImageToFirebase {
    private static final String TAG = "FirebaseImageUploader";
    public static String imageUrl = "";

    /**
     * upload image to firebase
     * @param bitmap ảnh cần upload lên firebase storage dưới dạng bitmap
     * @param nameImage tên ảnh cần upload
     * @param accauntId id của tài khoản restaurant
     * @param progressBar progress bar để hiển thị quá trình upload ảnh
     */
    public static void uploadImageToFirebase(Bitmap bitmap,String nameImage ,String accauntId,ProgressBar progressBar) {
        //mở progress bar
        progressBar.setVisibility(View.VISIBLE);
        // Tạo tham chiếu đến Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imagesRef = storageRef.child(accauntId+"/" + generateImageName(nameImage));
        // Chuyển đổi Bitmap thành dữ liệu byte với định dạng PNG và chất lượng nén là 100
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();


        // Tải dữ liệu byte lên Firebase Storage
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "Upload failed: " + exception.getMessage());
                // Xử lý khi tải ảnh lên thất bại
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload successful");
                //tải ảnh lên thành công
                // Lấy URL của ảnh đã tải lên
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = uri.toString();
                        Log.d(TAG, "Image URL9999999999: " + imageUrl);
                    }
                });
            }
        });
        //ẩn progress bar
        progressBar.setVisibility(View.GONE);
    }

    //tạo tên ảnh là 1 giờ phut giay_ngày_thang_nam
    private static String generateImageName(String nameImage) {
        // Lấy thời gian hiện tại
        long currentTime = System.currentTimeMillis();
        // Định dạng thời gian
        SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss_dd_MM_yyyy", Locale.getDefault());
        String timestamp = sdf.format(new Date(currentTime));
        return ""+nameImage + timestamp + ".png";
    }

    public static void deleteImageFromFirebase(String imageUrl) {
        // Tạo tham chiếu tới Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Lấy tham chiếu đến ảnh dựa trên link
        StorageReference photoRef = storage.getReferenceFromUrl(imageUrl);
        // Xóa ảnh từ Firebase Storage
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Xử lý khi xóa ảnh thành công
                System.out.println("Xóa ảnh thành công");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Xử lý khi xóa ảnh thất bại
                System.out.println("Xóa ảnh thất bại: " + exception.getMessage());
            }
        });
    }
}
