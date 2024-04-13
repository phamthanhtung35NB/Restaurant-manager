package com.example.restaurantmanager.Account;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.NotificationManager;
import android.content.Intent;


import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



//import adapter.Restaurant.MenuAdapter;

//import com.example.restaurantmanager.Client.HomeClientActivity;
import com.example.restaurantmanager.Client.MainActivity;
//import com.example.restaurantmanager.MenuRestaurant.HomeRestaurantActivity;
import com.example.restaurantmanager.MenuRestaurant.MainRestaurantActivity;
import com.example.restaurantmanager.FireBase.Notifications.MyFirebaseMessagingService;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import model.SqliteAccountHelper;

public class LoginActivity extends AppCompatActivity {

    private final String  DB_PATH_SUFFIX = "/databases/";
//    SQLiteDatabase database=null;
    private final String DATABASE_NAME="menurestaurant.db";
    private TextView textViewUsername;
    private EditText textPassword;
    Button buttonLogin;
    Button buttonCreateNewAccount;
    private FirebaseFirestore db;
    private RadioButton radioButtonRestaurantLogin;
    private RadioButton radioButtonClientLogin;
    SQLiteDatabase database=null;
    //mAuth là biến để xác thực người dùng
    private FirebaseAuth mAuth;
    //PERMISSION_REQUEST_CODE là mã yêu cầu quyền
    private static final int PERMISSION_REQUEST_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Check if the notification policy access has been granted for the app.
        if (notificationManager != null && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, gọi notify()
                System.out.println("onRequestPermissionsResult-------------------------------------------------");
            } else {
                // Quyền không được cấp,
            }
        }
    }

    void init() {
//        processCopy();
        textViewUsername = findViewById(R.id.textViewUsername);
        textPassword = findViewById(R.id.textPassword2);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateNewAccount = findViewById(R.id.buttonCreateNewAccount);
        radioButtonRestaurantLogin = findViewById(R.id.radioButtonRestaurantLogin);
        radioButtonClientLogin = findViewById(R.id.radioButtonClientLogin);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        SharedPreferences preferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        if (preferences.getString("uid", "") != "") {
            String text = preferences.getString("uid", "");
            textViewUsername.setText(preferences.getString("email", ""));
            textPassword.setText(preferences.getString("password", ""));
        }
    }


    void addEvents() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }

        });
        buttonCreateNewAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }
    private void login() {
        textPassword.setText("1234567");
        String username = textViewUsername.getText().toString();
        String password = textPassword.getText().toString();
        if (TextUtils.isEmpty(username)) {
            textViewUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            textPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }
        //1 account cố định để test
//        if (username.equals("admin") && password.equals("admin")) {
//            Intent intent = new Intent(LoginActivity.this, HomeRestaurantActivity.class);
//            startActivity(intent);
//            finish();
//        }
        //kiểm tra xem có kết nối internet không

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // kiểm tra user uid của người dùng thuộc collection client hay collection restaurant
                    String uid = mAuth.getCurrentUser().getUid();
                    //kiểm tra xem có sharedPreferences chưa nếu chưa thì tạo mới


                    SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", textViewUsername.getText().toString());
                    editor.putString("password", textPassword.getText().toString());
                    editor.putString("uid", uid);
                    editor.apply();
                    if (radioButtonClientLogin.isChecked()) {
                        loginClient(uid);
                    } else if (radioButtonRestaurantLogin.isChecked()) {
                        loginRestaurant(uid);
                    } else {
                        Toast.makeText(LoginActivity.this, "Vui lòng chọn loại tài khoản", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //////////////////////////////////////////ĐĂNG NHẬP VỚI TÀI KHOẢN NHÀ HÀNG//////////////////////////////////////////
    void loginRestaurant(String uid){
        DocumentReference documentReference = db.collection("restaurant").document(uid);
        //kiểm tra xem uid có tồn tại trong collection client không nếu không thì kiểm tra trong collection restaurant
        documentReference.get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                // nếu uid tồn tại trong collection client thì lấy thông tin phone và email của người dùng
                DocumentSnapshot document = task1.getResult();
                if (document.exists()) {
                    String phone = document.getString("phone");
                    String email = document.getString("email");

                    Toast.makeText(LoginActivity.this, "Login với tài khoản nhà hàng phone: " + phone, Toast.LENGTH_SHORT).show();
//                                insertAccount("client", username, password, phone, email, address);
                    String uid1 = mAuth.getCurrentUser().getUid();
                    Intent intent = new Intent(LoginActivity.this, MainRestaurantActivity.class);
//                    //uid của người dùng
//                    intent.putExtra("type", "restaurant");
//                    intent.putExtra("uid", uid1);
                    //lấy token của thiết bị và gửi lên server
                    MyFirebaseMessagingService.fetchTokenAndSendToServer(LoginActivity.this, "restaurant");
                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Không tồn tại tài khoản nhà hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //////////////////////////////////////////ĐĂNG NHẬP VỚI TÀI KHOẢN KHÁCH HÀNG//////////////////////////////////////////
    void loginClient(String uid){
        DocumentReference documentReference = db.collection("client").document(uid);
        //kiểm tra xem uid có tồn tại trong collection client không nếu không thì kiểm tra trong collection restaurant
        documentReference.get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                // nếu uid tồn tại trong collection client thì lấy thông tin phone và email của người dùng
                DocumentSnapshot document = task1.getResult();
                if (document.exists()) {
                    String phone = document.getString("phone");
                    String email = document.getString("email");
                    Toast.makeText(LoginActivity.this, "Login với tài khoản khách hàng phone: " + phone, Toast.LENGTH_SHORT).show();
                    String uid1 = mAuth.getCurrentUser().getUid();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("type", "client");
                    intent.putExtra("uid", uid1);

                    //lấy token của thiết bị và gửi lên server (server sẽ lưu token của thiết bị) để gửi thông báo
                    MyFirebaseMessagingService.fetchTokenAndSendToServer(LoginActivity.this, "client");
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Không tồn tại tài khoản khách hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    void insertAccount(String type, String username, String password, String phone, String email, String address){
//        SqliteAccountHelper databaseHelper =new SqliteAccountHelper(this);
//        databaseHelper.addAccountLogin(new Account(type, username, password, phone, email, address));
//        System.out.println("insertAccount------------------Done------------------------------");
//    }
    //////////////////////////////////////////KHỞI TẠO DATABASE SQLITE//////////////////////////////////////////
    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Sao chép cơ sở dữ liệu vào hệ thống thành công", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabasePath();
            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            // Truyền bytes dữ liệu từ input đến output
            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

}
//    onCreate(): Được gọi khi Activity được tạo. Đây là nơi bạn thường khởi tạo các thành phần của giao diện người dùng và các dữ liệu khác.
//    onStart(): Được gọi khi Activity trở nên hiển thị cho người dùng.
//    onResume(): Được gọi khi người dùng bắt đầu tương tác với Activity.
//    onPause(): Được gọi khi hệ thống chuẩn bị tiếp tục một Activity khác.
//    onStop(): Được gọi khi Activity không còn hiển thị cho người dùng.
//    onDestroy(): Được gọi trước khi Activity bị hủy.