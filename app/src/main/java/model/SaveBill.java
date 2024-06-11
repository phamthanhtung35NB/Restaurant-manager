package model;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class SaveBill {

//    buttonLogin.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Bitmap bitmap = getScreenShot(v);
//            store(bitmap, "myScreenshot.png");
////                login();
//        }
//
//    });
    public void getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        store(bitmap, "myScreenshot.png", view);
    }
//    public void captureScreen() {
//        View rootView = getView().getRootView();
//        rootView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
//        rootView.setDrawingCacheEnabled(false);
//
//        // Lưu bitmap vào bộ nhớ
//        store(bitmap, "myScreenshot.png");
//    }
//    public void captureScreen() {
//        LinearLayout rootView = findViewById(R.id.main);
//        rootView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
//        rootView.setDrawingCacheEnabled(false);
//
//        // Lưu bitmap vào bộ nhớ
//        store(bitmap, "myScreenshot.png");
//    }
    public void store(Bitmap bm, String fileName, View view){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            Toast.makeText(view.getContext(), "Saved", Toast.LENGTH_SHORT).show();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
