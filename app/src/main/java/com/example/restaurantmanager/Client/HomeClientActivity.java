package com.example.restaurantmanager.Client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import model.Account;
import model.HistoryRestaurant;
import model.SqliteAccountHelper;
import model.SqliteUrlOrderHelper;

public class HomeClientActivity extends AppCompatActivity {
    ImageButton imageButtonScan;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_client);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void init() {
        imageButtonScan = findViewById(R.id.imageButtonScan);
        textView = findViewById(R.id.textView);
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        if (preferences.getString("key", "") != "") {
            String text = preferences.getString("key", "");
            //tách chuỗi
            String[] arr = text.split("/");

            String content = arr[2];
            if (content.equals("order")){
                Intent intent = new Intent(HomeClientActivity.this, MenuClientActivity.class);
                intent.putExtra("url", content);
                startActivity(intent);
            }
            textView.setText(text);
        }
        HistoryRestaurant.readSumDayFromFireBase("Kj44x84LCzcwIXOpsR7wCU4pepB3");
    }
    void addEvents() {
        imageButtonScan.setOnClickListener(v -> {
            // Xử lý sự kiện khi click vào nút quét mã QR
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(true);
            integrator.setPrompt("Quét mã QR để xem menu");
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//            integrator.setCaptureActivity(400);

            integrator.initiateScan();

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // Xử lý kết quả quét mã QR
            String content = result.getContents();
            if (content != null) {
                // Check if table number is available from intent
                //lưu mã QR vào database
//                System.out.println("content: " + content);
//                SqliteUrlOrderHelper sqliteUrlOrderHelper =new SqliteUrlOrderHelper(this);
//                System.out.println("111111111111111111111");
//                sqliteUrlOrderHelper.addUrl(content);
//                System.out.println("222222222222222222222");
//                Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
//                System.out.println("333333333333333333333");
//                String text=sqliteUrlOrderHelper.getAllUrls();
                // Lấy SharedPreferences
                SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);

// Lưu trữ giá trị
                preferences.edit().putString("key", content).apply();
                Intent intent = new Intent(HomeClientActivity.this, MenuClientActivity.class);
                intent.putExtra("url", content);
                startActivity(intent);
                //đóng activity
                finish();

            } else {
                textView.setText("Không tìm thấy mã QR");
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
//        IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    }
//    @Override
//    public void handleResult(Result result) {
//        // Xử lý kết quả quét mã QR
//        Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        scannerView.startCamera();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        scannerView.stopCamera();
//    }
}