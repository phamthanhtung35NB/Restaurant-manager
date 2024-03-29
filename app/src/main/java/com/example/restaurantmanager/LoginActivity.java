package com.example.restaurantmanager;

//import android.app.Activity;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;


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



//import adapter.MenuAdapter;

import com.example.restaurantmanager.Client.HomeClientActivity;
import com.example.restaurantmanager.MenuRestaurant.HomeRestaurantActivity;
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

import model.Account;
import model.SqliteAccountHelper;

public class LoginActivity extends AppCompatActivity {
//    private static final String TABLE_NAME_ACCOUNT = "account";
//    private static final String COLUMN_TYPE = "type";
//    private static final String COLUMN_USERNAME = "username";
//    private static final String COLUMN_PASSWORD = "password";
//    private static final String COLUMN_PHONE = "phone";
//    private static final String COLUMN_EMAIL = "email";
//    private static final String COLUMN_ADDRESS = "address";
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
    private FirebaseAuth mAuth;
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
        processCopy();
        textViewUsername = findViewById(R.id.textViewUsername);
        textPassword = findViewById(R.id.textPassword2);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateNewAccount = findViewById(R.id.buttonCreateNewAccount);
        radioButtonRestaurantLogin = findViewById(R.id.radioButtonRestaurantLogin);
        radioButtonClientLogin = findViewById(R.id.radioButtonClientLogin);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        System.out.println("init-------------------------------------------------");
        //////////////////////////////////////////KHỞI TẠO DATABASE//////////////////////////////////////////
//        SqliteAccountHelper dbHelper = new SqliteAccountHelper(this);
//        System.out.println("dbHelper-------------------------------------------------");
//            Account account= dbHelper.getAccount();
//        System.out.println("account-------------------------------------------------");
//
//            if(account!=null){
//                System.out.println("load----------------------"+account.getUsername());
////                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                if (account.getType().equals("restaurant")) {
//                    Intent intent = new Intent(LoginActivity.this, HomeRestaurantActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
        //////////////////////////////////////////KHỞI TẠO DATABASE//////////////////////////////////////////
//        database = openOrCreateDatabase("menurestaurant.db", MODE_PRIVATE, null);
//        Cursor c = database.query("account", null, null, null, null, null, null);
//        System.out.println("----------------------------3---------------------------------");
//        int a=0;
//        while (c.moveToNext()) {
//            if (c.getString(1)!=null){
//                System.out.println("load"+a+"----------------------"+c.getString(1));
//                a++;
//                Intent intent = new Intent(LoginActivity.this, HomeRestaurantActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }
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
        System.out.println("login-------------------------------------------------");
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
        if (username.equals("admin") && password.equals("admin")) {
            Intent intent = new Intent(LoginActivity.this, HomeRestaurantActivity.class);
            startActivity(intent);
            finish();
        }
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // kiểm tra user uid của người dùng thuộc collection client hay collection restaurant
                    String uid = mAuth.getCurrentUser().getUid();
                    if (radioButtonClientLogin.isChecked()) {
                        loginClient(uid);
                    } else if (radioButtonRestaurantLogin.isChecked()) {
                        loginRestaurant(uid);
                    } else {
                        Toast.makeText(LoginActivity.this, "Vui lòng chọn loại tài khoản", Toast.LENGTH_SHORT).show();
                    }
//                    finish(); // Kết thúc activity hiện tại sau khi đăng nhập thành công
                } else {
                    Toast.makeText(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    private void login() {
//        final String username = textViewUsername.getText().toString();
//        final String password = textPassword.getText().toString();
//        if (TextUtils.isEmpty(username)) {
//            textViewUsername.setError("Vui lòng nhập tên đăng nhập");
//            return;
//        }
//        if (TextUtils.isEmpty(password)) {
//            textPassword.setError("Vui lòng nhập mật khẩu");
//            return;
//        }
//        db = FirebaseFirestore.getInstance();
//        if (radioButtonClientLogin.isChecked()) {
//            loginClient(username, password);
//        } else if (radioButtonRestaurantLogin.isChecked()) {
//            loginRestaurant(username, password);
//        }
//        else {
//            Toast.makeText(LoginActivity.this, "Vui lòng chọn loại tài khoản", Toast.LENGTH_SHORT).show();
//        }
//    }

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
                    Intent intent = new Intent(LoginActivity.this, HomeRestaurantActivity.class);
                    //uid của người dùng

                    intent.putExtra("type", "restaurant");
                    intent.putExtra("uid", uid1);
                    System.out.println("bên login-----------------"+uid1);
                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Không tồn tại tài khoản nhà hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


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
                    Intent intent = new Intent(LoginActivity.this, HomeClientActivity.class);

                    intent.putExtra("type", "client");
                    intent.putExtra("uid", uid1);
                    startActivity(intent);
//                    finish();
//                                insertAccount("client", username, password, phone, email, address);
                } else {
                    Toast.makeText(LoginActivity.this, "Không tồn tại tài khoản khách hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void insertAccount(String type, String username, String password, String phone, String email, String address){
        SqliteAccountHelper databaseHelper =new SqliteAccountHelper(this);
        databaseHelper.addAccountLogin(new Account(type, username, password, phone, email, address));
        System.out.println("insertAccount------------------Done------------------------------");
    }
    //////////////////////////////////////////KHỞI TẠO DATABASE//////////////////////////////////////////
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
    //////////////////////////////////////////KHỞI TẠO DATABASE//////////////////////////////////////////
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
