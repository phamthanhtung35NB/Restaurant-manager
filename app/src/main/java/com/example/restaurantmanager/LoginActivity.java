package com.example.restaurantmanager;

//import android.app.Activity;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



//import adapter.MenuAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import model.Account;
import model.FirestoreHelper;
import model.MenuRestaurant;
import model.SqliteAccountHelper;
//import model.SqliteHelper;

public class LoginActivity extends AppCompatActivity {
    private static final String TABLE_NAME_ACCOUNT = "account";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADDRESS = "address";
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
    private SqliteAccountHelper dbHelper;
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
    }
    void init() {
        textViewUsername = findViewById(R.id.textViewUsername);
        textPassword = findViewById(R.id.textPassword2);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateNewAccount = findViewById(R.id.buttonCreateNewAccount);
        radioButtonRestaurantLogin = findViewById(R.id.radioButtonRestaurantLogin);
        radioButtonClientLogin = findViewById(R.id.radioButtonClientLogin);

        System.out.println("0----------------------------------");
        processCopy();
//Mở CSDL lên để dùng
//        database = openOrCreateDatabase("menurestaurant.db", MODE_PRIVATE, null);
// Tạo ListView
        System.out.println("1----------------------------------");
// Truy vấn CSDL và cập nhật hiển thị lên Listview
//        Cursor c = database.query("account", null, null, null, null, null, null);
//        System.out.println("2----------------------------------");
//        c.moveToFirst();
//        String data = "";
//        while (c.isAfterLast() == false) {
//            data = c.getString(0) + "-" + c.getString(1) + "-" + c.getString(2)+"-"+c.getString(3)+"-"+c.getString(4);
//            System.out.println(data);
//            c.moveToNext();
//        }
//        c.close();
    }
    private void processCopy() {
//private app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
//                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    void addEvents() {
        buttonLogin.setOnClickListener(v -> {
            login();
//            addfire();
        });
        buttonCreateNewAccount.setOnClickListener(v -> {
//            addRestaurant();
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }
    private void login() {
        final String username = textViewUsername.getText().toString();
        final String password = textPassword.getText().toString();
        if (TextUtils.isEmpty(username)) {
            textViewUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            textPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }
        final String type1 = "";
        db = FirebaseFirestore.getInstance();
        if (radioButtonClientLogin.isChecked()) {
            loginClient(username, password);
        } else if (radioButtonRestaurantLogin.isChecked()) {
            loginRestaurant(username, password);
        }
        else {
            Toast.makeText(LoginActivity.this, "Vui lòng chọn loại tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void CopyDataBaseFromAsset() {
// TODO Auto-generated method stub
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
    void loginRestaurant(String username, String password){
        db.collection("restaurant")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        System.out.println("onComplete-------------------------------------------------");
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String storedPassword = document.getString("password");
                                String username1 = document.getString("username");
                                String phone = document.getString("phone");
                                String email = document.getString("email");
                                String address = document.getString("address");
                                // So sánh password nhập vào với password lưu trong Firestore
                                if (storedPassword != null && storedPassword.equals(password)) {
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    System.out.println("Đăng nhập thành công-----------------------------------------");
                                    insertAccount("restaurant", username1, storedPassword, phone, email, address);
                                    System.out.println("insertAccount----5645654645------------------------------------");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Kết thúc activity hiện tại sau khi đăng nhập thành công
                                } else {
                                    // Sai mật khẩu
                                    Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Tài khoản không tồn tại
                                Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Lỗi khi thực hiện truy vấn
                            Toast.makeText(LoginActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    void loginClient(String username, String password){
        db.collection("client")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        System.out.println("onComplete-------------------------------------------------");
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String storedPassword = document.getString("password");
                                String phone = document.getString("phone");
                                String email = document.getString("email");
                                String address = document.getString("address");
                                // So sánh password nhập vào với password lưu trong Firestore
                                if (storedPassword != null && storedPassword.equals(password)) {
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    insertAccount("client", username, password, phone, email, address);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Kết thúc activity hiện tại sau khi đăng nhập thành công
                                } else {
                                    // Sai mật khẩu
                                    Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Tài khoản không tồn tại
                                Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Lỗi khi thực hiện truy vấn
                            Toast.makeText(LoginActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    void insertAccount(String type, String username, String password, String phone, String email, String address){
        System.out.println("insertAccount-------------------------------------------------");
        SQLiteDatabase database= null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_ADDRESS, address);
        long result = database.insert("account", null, contentValues);
        if (result == -1) {
            System.out.println("Fail to insert record!");
            Toast.makeText(this, "Fail to insert record!", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("Inserted record successfully");
            Toast.makeText(this, "Record inserted successfully!", Toast.LENGTH_SHORT).show();
        }
        System.out.println("insertAccount-------------------------------------------------");
    }
//    void login(){
//        String username = textViewUsername.getText().toString();
//        String password = textPassword.getText().toString();
//        String type = "";
//
//        if (radioButtonClientLogin.isChecked()) {
//            Toast.makeText(LoginActivity.this, "Client", Toast.LENGTH_SHORT).show();
//            type = "client";
//        } else if (radioButtonRestaurantLogin.isChecked()) {
//            Toast.makeText(LoginActivity.this, "Restaurant", Toast.LENGTH_SHORT).show();
//            type = "restaurant";
//        }
//        if (type.equals("")) {
//            Toast.makeText(LoginActivity.this, "Vui lòng chọn loại tài khoản", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Kiểm tra xem username và password có rỗng không
//        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
//            // Gọi phương thức getAccount để lấy thông tin tài khoản từ Firestore
//            FirestoreHelper.getAccount(type, username, new FirestoreHelper.AccountCallback() {
////                Toast.makeText(LoginActivity.this, "Đang kiểm tra tài khoản", Toast.LENGTH_SHORT).show();
//                @Override
//                public void onAccountReceived(Account account) {
//                    if (account != null && account.getPassword().equals(password)) {
//                        SqliteAccountHelper.addAccountLogin(account.getType(), account.getUsername(), account.getPassword(), account.getPhone(), account.getEmail(), account.getAddress());
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//                    // Xử lý trường hợp lỗi khi lấy thông tin tài khoản
//                }
//            });
//        } else {
//            Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//        }
//    }




    //add restaurant to firebase
//    private void addRestaurant() {
//        String id = "2";
//        String name = "Bún";
//        String description = "Bún Hà Nội";
//        double price = 30000;
//        String image = "https://firebasestorage.googleapis.com/v0/b/restaurantmanager-1e3e7.appspot.com/o/restaurant%2Ftung%2Fbuncha.jpg?alt=media&token=3e3e3e3e-3e3e-3e3e-3e3e-3e3e3e3e3e3e";
//        MenuRestaurant menuRestaurant = new MenuRestaurant(id, name, description, price, image);
//        FirestoreHelper.addMenuRestaurant( "tung", menuRestaurant);
//    }


}
