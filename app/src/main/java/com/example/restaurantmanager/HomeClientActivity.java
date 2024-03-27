package com.example.restaurantmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
                    textView.setText("Mã QR: " + content);

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