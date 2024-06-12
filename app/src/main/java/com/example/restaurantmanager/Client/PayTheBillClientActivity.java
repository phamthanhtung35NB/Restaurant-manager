package com.example.restaurantmanager.Client;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//import model.NumberToWordsConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;

import adapter.Client.MenuClientAdapter;
import adapter.Client.OrderClientAdapter;
import adapter.MenuBillAdapter;
import model.MenuRestaurant;
import model.SetTableStateEmptyRealtime;

public class PayTheBillClientActivity extends AppCompatActivity {
    public static ListView listViewClient;
    public static MenuBillAdapter menuClientAdapter;
    public static ArrayList<MenuRestaurant> dataMenuViewClient;
    TextView textViewShowBill,txtDate;
    Button btnSaveBill;
    String numberTable="";
    String billDouble;
    String accountId="";
    public static ListView listViewClientPayTheBill;
    public static String type = "restaurant";
    public static String URL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay_the_bill_client);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void init() {
        System.out.println("trong init");
        textViewShowBill = findViewById(R.id.textViewShowBill);
        listViewClientPayTheBill = findViewById(R.id.listViewClientPayTheBill);
        btnSaveBill = findViewById(R.id.btnSaveBill);
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String ktra = sharedPreferences.getString("url", "");
        if (ktra.length()>3) {
            URL = ktra;
            System.out.println("URL: "+URL);
            accountId = sharedPreferences.getString("accountId", "");
            numberTable = sharedPreferences.getString("numberTable", "");
            billDouble = sharedPreferences.getString("tong", "");
        }
        listViewClient = findViewById(R.id.listViewClient);
        dataMenuViewClient = new ArrayList<>();
        MainActivity.isCheckQR= true;
        readDataFromFireBase();
        System.out.println("cuối init");
    }
    void addEvents() {
//        startChecking();
//        String billString = NumberToWordsConverter.convert(billDouble);
        textViewShowBill.setText(billDouble+" VNĐ");
        System.out.println("hết addEvents");
        btnSaveBill.setOnClickListener(v -> {
            // Lưu hóa đơn
            View yourLayout = findViewById(R.id.main);
                    Bitmap bitmap = getScreenShot(yourLayout);
                    store(bitmap, "myScreenshot.png");
                    Toast.makeText(this, "Đã Lưu hóa đơn ở thư mục Screenshots", Toast.LENGTH_LONG).show();
        });
    }

public Bitmap getScreenShot(View view) {
    View screenView = view.getRootView();
    screenView.setDrawingCacheEnabled(true);
    Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
    screenView.setDrawingCacheEnabled(false);
    return bitmap;
}

public void store(Bitmap bm, String fileName) {
    String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/Screenshots";
//    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
    File dir = new File(dirPath);
    if(!dir.exists()){
        dir.mkdirs();
    }
    File file = new File(dirPath, fileName);
    try {
        FileOutputStream fOut = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        fOut.flush();
        fOut.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    void readDataFromFireBase(){
        System.out.println("đầu readDataFromFireBase");
        System.out.println("URL: " + URL);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refOrder = database.getReference(URL);
        refOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Xóa dữ liệu cũ
                dataMenuViewClient.clear();
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
                        dataMenuViewClient.add(menuOrder);

                    }
                }
                menuClientAdapter = new MenuBillAdapter(PayTheBillClientActivity.this, R.layout.food_show_bill, dataMenuViewClient);
                listViewClientPayTheBill.setAdapter(menuClientAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
            }
        });
    }

    private Handler handler;
    private Runnable runnable;

    /**
     * Hàm bắt đầu kiểm tra biến staticBooleanVariable trong OtherClass
     */
    void startChecking() {
        // Khởi tạo Handler và Runnable
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
//                SetTableStateEmptyRealtime.getTableIsUsingString(accountId,table);
                handler.postDelayed(this, 2000);
                checkVariable();
            }
        };
        // Bắt đầu kiểm tra
        runnable.run();
    }

    /**
     * Hàm kiểm tra biến staticBooleanVariable trong OtherClass
     */
    private void checkVariable() {

        if (SetTableStateEmptyRealtime.tableIsUsing.equals("Trống")) {
            System.out.println("Đã thanh toán thành công");
//            Intent intent = new Intent(PayTheBillClientActivity.this, HomeClientActivity.class);
        } else {
            System.out.println("Đang chờ thanh toán");
        }
    }

    /**
     * khi Activity bị hủy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dừng kiểm tra khi Activity bị hủy
        handler.removeCallbacks(runnable);
    }
}