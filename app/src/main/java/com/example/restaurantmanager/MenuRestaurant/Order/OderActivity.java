package com.example.restaurantmanager.MenuRestaurant.Order;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.Notifications.MyFirebaseMessagingService;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

import adapter.OrderAdapter;
import model.MenuRestaurant;

public class OderActivity extends AppCompatActivity {

    public static ListView listViewOrder;

    public static OrderAdapter oderAdapter;
    public static ArrayList<MenuRestaurant> dataOrder;
    ImageButton imageButtonExit;
    TextView textThongTinBan;
    Button buttonThanhToan;
    public static String accountId = "";
    public static String url = "";
    public static String table = "1";
    private final int width = 1000;
    private final int height = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_oder);
        try {
            // Call init() to generate and display QR code
            init();
        } catch (WriterException e) {
            e.printStackTrace();
            // Handle exception if QR code generation fails
        }
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        System.out.println("url: " + url);

        readDataFromFireBase();
        oderAdapter = new OrderAdapter(OderActivity.this, R.layout.food_order, dataOrder);
        listViewOrder.setAdapter(oderAdapter);

        // Khởi tạo Handler và Runnable
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                checkVariable();
                handler.postDelayed(this, 1000);
            }
        };

        // Bắt đầu kiểm tra
        runnable.run();

    }
    public void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_notification_dialog);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == Gravity.BOTTOM) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        dialog.show();
    }
    void init() throws WriterException {
        listViewOrder = findViewById(R.id.listViewOrder);
        imageButtonExit = findViewById(R.id.imageButtonExit);
        buttonThanhToan = findViewById(R.id.buttonThanhToan);
        textThongTinBan = findViewById(R.id.textThongTinBan);
        dataOrder = new ArrayList<>();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
//tách chuỗi
        String[] parts = url.split("/");
        accountId = parts[0];
        table = parts[1];

        textThongTinBan.setText("Bàn số: " + table);
        showQR();
//        accountId = intent.getStringExtra("uid");
//        table = intent.getStringExtra("table");
//        MainActivity.dataOrder = new ArrayList<>();
    }
    void showQR() throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();

        BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height);
        ImageView imageViewQr = findViewById(R.id.imageViewQr);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }
        imageViewQr.setImageBitmap(bitmap);
    }

    void readDataFromFireBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refOrder = database.getReference(url);
        refOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lặp qua tất cả child
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Tạo instance mới của ClassTable
                    MenuRestaurant menuOrder = new MenuRestaurant();
                    System.out.println("000000000000000000000000");
                    if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {
                        System.out.println("1111111111111111111111");
                        menuOrder.setId(childSnapshot.child("id").getValue(String.class));
                        System.out.println("2222222222222222222222");
                        menuOrder.setName(childSnapshot.child("name").getValue(String.class));
                        System.out.println("3333333333333333333333");
                        menuOrder.setDescription(childSnapshot.child("describe").getValue(String.class));
                        System.out.println("4444444444444444444444");
                        menuOrder.setImage(childSnapshot.child("image").getValue(String.class));
                        System.out.println("5555555555555555555555");
                        menuOrder.setPrice(childSnapshot.child("price").getValue(Double.class));
                        System.out.println("6666666666666666666666");
                        menuOrder.toString();
                        dataOrder.add(menuOrder);

                    }
                }
                oderAdapter = new OrderAdapter(OderActivity.this, R.layout.food_order, dataOrder);
                listViewOrder.setAdapter(oderAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
            }
        });
    }
    void addEvents(){
//        imageButtonExit.setOnClickListener(v -> {
//            //chuyển màn hình
//            Intent intent = new Intent(OderActivity.this, MainActivity.class);
//            startActivity(intent);
//        });

    }
    private Handler handler;
    private Runnable runnable;





    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dừng kiểm tra khi Activity bị hủy
        handler.removeCallbacks(runnable);
    }

    private void checkVariable() {

        if (MyFirebaseMessagingService.isNotification==true) {
            // Biến staticBooleanVariable trong OtherClass đang có giá trị true
            showDialog();
            System.out.println("có tb mới");
            MyFirebaseMessagingService.isNotification = false;
        } else {
            // Biến staticBooleanVariable trong OtherClass đang có giá trị false
//            showDialog();
        }
    }
}