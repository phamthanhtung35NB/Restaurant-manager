//package model;
//import
//
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.widget.ImageView;
//
//import com.example.restaurantmanager.R;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
//public class andleQR {
//    QRCodeWriter writer = new QRCodeWriter();
//    String content = "Đây là nội dung mã QR";
//    BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
//    ImageView imageView = findViewById(R.id.imageView);
//    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//for (int i = 0; i < width; i++) {
//        for (int j = 0; j < height; j++) {
//            bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
//        }
//    }
//imageView.setImageBitmap(bitmap);
//}
