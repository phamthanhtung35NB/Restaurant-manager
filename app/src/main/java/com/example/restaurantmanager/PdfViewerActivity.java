//package com.example.restaurantmanager;
//
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import android.os.Bundle;
//import android.os.Environment;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.github.barteksc.pdfviewer.PDFView;
//import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//public class PdfViewerActivity extends AppCompatActivity {
//
//    private PDFView pdfView;
//    private Button btnSave;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pdf_viewer);
//
//        pdfView = findViewById(R.id.pdfView);
//        btnSave = findViewById(R.id.btnSave);
//
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bill.pdf";
//        displayPdf(path);
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                savePdfToInternalStorage(path);
//            }
//        });
//    }
//
//    private void displayPdf(String path) {
//        File file = new File(path);
//        pdfView.fromFile(file)
//                .onLoad(new OnLoadCompleteListener() {
//                    @Override
//                    public void loadComplete(int nbPages) {
//                        Toast.makeText(PdfViewerActivity.this, "File loaded", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .load();
//    }
//
//    private void savePdfToInternalStorage(String path) {
//        try {
//            File file = new File(path);
//            InputStream in = new FileInputStream(file);
//            OutputStream out = new FileOutputStream(getFilesDir().getPath() + "/bill.pdf");
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = in.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            out.flush();
//            out.close();
//            in.close();
//
//            Toast.makeText(this, "File saved to internal storage", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}