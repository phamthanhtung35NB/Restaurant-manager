//package com.example.restaurantmanager;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.widget.ImageView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
//public class HandleQrActivity extends AppCompatActivity {
//
//    private final int width = 1000;
//    private final int height = 1000;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_handle_qr);
//
//        try {
//            // Call init() to generate and display QR code
//            init();
//        } catch (WriterException e) {
//            e.printStackTrace();
//            // Handle exception if QR code generation fails
//        }
//
//        addEvents();
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
//    void init() throws WriterException {
//        String content = "";
//        Intent intent = getIntent();
//        String tableNumber = intent.getStringExtra("uid");
//
//        // Check if table number is available from intent
//        if (tableNumber != null && !tableNumber.isEmpty()) {
//            content += tableNumber;
//        } else {
//            // Handle case where table number is missing (optional)
//            // You can display a message or default value here
//        }
//
//        QRCodeWriter writer = new QRCodeWriter();
//
//        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
//        ImageView imageViewQr = findViewById(R.id.imageViewQr);
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
//            }
//        }
//        imageViewQr.setImageBitmap(bitmap);
//    }
//
//    void addEvents() {
//        // Add your event listeners here
//    }
//}
