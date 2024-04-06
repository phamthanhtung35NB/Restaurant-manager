package com.example.restaurantmanager.Client;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.Notifications.FireBase;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import model.Account;
import model.HistoryRestaurant;
import model.SqliteAccountHelper;
import model.SqliteUrlOrderHelper;

public class HomeClientActivity extends AppCompatActivity {
    ImageButton imageButtonScan;
    TextView textView;
    public static final String SERVER_KEY = "AAAAl-xT4ko:APA91bGASnqgklF4OfVR6ls42PxiSI1Lzj2Aj8qYqdlCgk4LKApgGGpE1oH_GzLgBqjheSfQqHc3_qrdcsT4cwOGAbGCwgdUNpLmLx-tdGLo_NtbC-rZrqiDBtcP5qI6xI_YrefHOAtX";
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
        //tách chuỗi
        String[] arr = preferences.getString("key", "").split("/");
//        String userId = arr[0];

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
    //gửi thông báo in app cho user
    public static void sendMessageFromUser1ToUser2(String user2Token) {
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                // Create URL instance.
                URL url = new URL("https://fcm.googleapis.com/fcm/send");

                // Create connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set method as POST
                connection.setRequestMethod("POST");

                // Set headers
                connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);
                connection.setRequestProperty("Content-Type", "application/json");

                // Enable input and output streams
                connection.setDoOutput(true);

                // Create the message content
                String jsonInputString = "{\"to\": \"" + user2Token + "\", \"notification\": {\"title\": \"Message from User 1\", \"body\": \"Hello User 2!\"}}";

                // Write the output
                try(OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response
                int responseCode = connection.getResponseCode();
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    Log.d(TAG, "Response: " + response.toString());
                }

                connection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error sending FCM message", e);
            }
        }
    });

    thread.start();
}
    //gửi thông báo đến token
    public static void sendMessageToToken(String token) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create URL instance.
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");

                    // Create connection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set method as POST
                    connection.setRequestMethod("POST");

                    // Set headers
                    connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);
                    connection.setRequestProperty("Content-Type", "application/json");

                    // Enable input and output streams
                    connection.setDoOutput(true);

                    // Create the message content
                    String jsonInputString = "{\"to\": \"" + token + "\", \"notification\": {\"title\": \"test\", \"body\": \"thành công\"}}";

                    // Write the output
                    try(OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    // Get the response
                    int responseCode = connection.getResponseCode();
                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Log.d(TAG, "Response: " + response.toString());
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, "Error sending FCM message", e);
                }
            }
        });

        thread.start();
    }
    public static void sendNotification(String content) {
        //cắt chuỗi content để lấy id của nhà hàng
        System.out.println("123123123");
        System.out.println("content: " + content);

        System.out.println("userId: " + content);
        //lấy token từ uri của user
        String token = FireBase.tokenRtn;
        System.out.println("token: " + token);
//        System.out.println(FireBase.tokenRtn);
        //gửi thông báo đến token
        sendMessageToToken(content);
        sendMessageFromUser1ToUser2(content);
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
                //lấy id của nhà hàng
                String[] arr = content.split("/");
                String userId1 = arr[0];
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("restaurant").document(userId1)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Map<String, Object> accountData = documentSnapshot.getData();
                                    String token = accountData.get("token").toString();
                                    Log.d(ContentValues.TAG, "Summmmmmmmmm: " + token);
                                    sendNotification(token);
                                } else {
                                    Log.d(ContentValues.TAG, "No such document");
                                }
                            }
                        });

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