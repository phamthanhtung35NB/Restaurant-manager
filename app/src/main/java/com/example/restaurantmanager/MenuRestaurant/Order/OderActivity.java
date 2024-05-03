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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.restaurantmanager.FireBase.Notifications.MyFirebaseMessagingService;
import com.example.restaurantmanager.MenuRestaurant.MainRestaurantActivity;
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

import adapter.Restaurant.OrderAdapter;
import model.MenuRestaurant;

public class OderActivity extends AppCompatActivity {

    public static ListView listViewOrder;

    public static OrderAdapter oderAdapter;
    public static ArrayList<MenuRestaurant> dataOrder;
    ImageButton imageButtonExit;
    TextView textThongTinBan;
    Button buttonThanhToan;
    Button btshowQR;
    Button addFood;

    public static String accountId = "";
    public static String url = "";
    public static String table = "1";
    // QR code size
    private final int width = 1000;
    private final int height = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_oder);
        try {
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
        readDataFromFireBase();
        oderAdapter = new OrderAdapter(OderActivity.this, R.layout.food_order, dataOrder);
        listViewOrder.setAdapter(oderAdapter);
    }

    void init() throws WriterException {
        listViewOrder = findViewById(R.id.listViewOrder);
        imageButtonExit = findViewById(R.id.imageButtonExit);
        buttonThanhToan = findViewById(R.id.buttonThanhToan);
        btshowQR = findViewById(R.id.btshowQR);
        textThongTinBan = findViewById(R.id.textThongTinBan);
        addFood = findViewById(R.id.addFood);

        dataOrder = new ArrayList<>();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        //tách chuỗi
        String[] parts = url.split("/");
        accountId = parts[0];
        table = parts[1];

//        startChecking();
        textThongTinBan.setText("Bàn số: " + table);
//        showQR();
//        accountId = intent.getStringExtra("uid");
//        table = intent.getStringExtra("table");
//        MainActivity.dataOrder = new ArrayList<>();
    }

    void addEvents(){
        imageButtonExit.setOnClickListener(v -> {
//            quay về
                super.onBackPressed();
        });
        btshowQR.setOnClickListener(v -> {
            try {
                showQR();
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });

        buttonThanhToan.setOnClickListener(v -> {
//            Intent intent = new Intent(OderActivity.this, PaymentActivity.class);
//            intent.putExtra("url", url);
//            startActivity(intent);
        });

        addFood.setOnClickListener(v -> {
            Intent intent = new Intent(OderActivity.this, MenuAddFoodToOrderActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        });
    }


    /**
     * Hàm hiển thị dialog thông báo
     */
//    public void showDialog(){
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.activity_notification_dialog);
//
//        Window window = dialog.getWindow();
//        if (window == null) {
//            return;
//        }
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = Gravity.CENTER;
//        window.setAttributes(windowAttributes);
//        if (Gravity.BOTTOM == Gravity.BOTTOM) {
//            dialog.setCancelable(true);
//        } else {
//            dialog.setCancelable(false);
//        }
//        Button btnClose = dialog.findViewById(R.id.btnClose);
//        btnClose.setOnClickListener(v -> {
//            System.out.println("Đóng dialog");
//            dialog.dismiss();
//        });
//
//        dialog.show();
//    }

    void showQR() throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        final int width = 1000;
        final int height = 1000;
        BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_center_show_qr);

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
        TextView username = dialog.findViewById(R.id.username);
        ImageView imageViewQRCode = dialog.findViewById(R.id.imageViewQRCode);
        //set image
        imageViewQRCode.setImageBitmap(bitmap);
//       set username table
        username.setText("Bàn Số: "+table);

        dialog.show();
    }
//    void showQR() throws WriterException {
//        QRCodeWriter writer = new QRCodeWriter();
//
//        BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height);
//        ImageView imageViewQr = findViewById(R.id.imageViewQr);
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
//            }
//        }
//        imageViewQr.setImageBitmap(bitmap);
//    }

    void checkStateEmpty() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refState = database.getReference("uri/1/stateEmpty");
        refState.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentState = dataSnapshot.getValue(String.class);
                if (currentState != null && currentState.equals("đang sử dụng")) {
                    Toast.makeText(OderActivity.this, "Bàn đang sử dụng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error reading data: " + databaseError.getMessage());
            }
        });
    }
    /**
     * Đọc dữ liệu từ Firebase Database và hiển thị lên ListView
     */
    void readDataFromFireBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refOrder = database.getReference(url);
        refOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the old data as new data is available
                dataOrder.clear();
                if (dataSnapshot.hasChildren()) {
                    // Loop through all children
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // Create a new instance of ClassTable
                        MenuRestaurant menuOrder = new MenuRestaurant();
                        if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {
                            menuOrder.setId(childSnapshot.child("id").getValue(String.class));
                            menuOrder.setName(childSnapshot.child("name").getValue(String.class));
                            menuOrder.setDescription(childSnapshot.child("describe").getValue(String.class));
                            menuOrder.setImage(childSnapshot.child("image").getValue(String.class));
                            menuOrder.setPrice(childSnapshot.child("price").getValue(Double.class));

                            dataOrder.add(menuOrder);
                        }
                    }
                } else {
                    // dataSnapshot does not contain any children
                    // Handle this case as needed
                    Toast.makeText(OderActivity.this, "No data found at this path", Toast.LENGTH_SHORT).show();
                    MenuRestaurant menuOrder = new MenuRestaurant("0", "No data found", "No data found", 0, "");
                    dataOrder.add(menuOrder);
                }

                oderAdapter = new OrderAdapter(OderActivity.this, R.layout.food_order, dataOrder);
                listViewOrder.setAdapter(oderAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                System.out.println("Error reading data: " + databaseError.getMessage());
            }
        });
    }
//    void readDataFromFireBase(){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference refOrder = database.getReference(url);
//        refOrder.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Lặp qua tất cả child
//                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                    // Tạo instance mới của ClassTable
//                    MenuRestaurant menuOrder = new MenuRestaurant();
//                    if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {
//                        menuOrder.setId(childSnapshot.child("id").getValue(String.class));
//                        menuOrder.setName(childSnapshot.child("name").getValue(String.class));
//                        menuOrder.setDescription(childSnapshot.child("describe").getValue(String.class));
//                        menuOrder.setImage(childSnapshot.child("image").getValue(String.class));
//                        menuOrder.setPrice(childSnapshot.child("price").getValue(Double.class));
//
//                        menuOrder.toString();
//                        dataOrder.add(menuOrder);
//                    }
//                }
//                oderAdapter = new OrderAdapter(OderActivity.this, R.layout.food_order, dataOrder);
//                listViewOrder.setAdapter(oderAdapter);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Xử lý lỗi
//                System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
//            }
//        });
//    }

//    private Handler handler;
//    private Runnable runnable;
//
//    /**
//     * Hàm bắt đầu kiểm tra biến staticBooleanVariable trong OtherClass
//     */
//    void startChecking() {
//        // Khởi tạo Handler và Runnable
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                checkVariable();
//                handler.postDelayed(this, 1000);
//            }
//        };
//
//        // Bắt đầu kiểm tra
//        runnable.run();
//    }
//
//    /**
//     * khi Activity bị hủy
//     */
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Dừng kiểm tra khi Activity bị hủy
//        handler.removeCallbacks(runnable);
//    }
//
//    /**
//     * Hàm kiểm tra biến staticBooleanVariable trong OtherClass
//     */
//    private void checkVariable() {
//
//            if (MyFirebaseMessagingService.isNotification==true) {
//            // Biến staticBooleanVariable trong OtherClass đang có giá trị true
//            showDialog();
//            System.out.println("có tb mới");
//            MyFirebaseMessagingService.isNotification = false;
//                } else {
//            // Biến staticBooleanVariable trong OtherClass đang có giá trị false
////            showDialog();
//        }
//    }
}