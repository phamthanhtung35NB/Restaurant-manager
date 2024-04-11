package com.example.restaurantmanager.Account;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


import model.HistoryRestaurant;
import model.MenuRestaurant;

public class RegisterActivity extends AppCompatActivity {
    private TextView textViewUsername2;
    private EditText textPassword2;
    private EditText textPassword3;
    private EditText textPhone;
    private EditText textEmail;
    private Button buttonLogin2;

    private RadioButton radioButtonRestaurant;
    private RadioButton radioButtonClient;

    FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        addfire();

    }
    void init() {
        textViewUsername2 = findViewById(R.id.textViewUsername2);
        textPassword2 = findViewById(R.id.textPassword2);
        textPassword3 = findViewById(R.id.textPassword3);
        textPhone = findViewById(R.id.textPhone);
        textEmail = findViewById(R.id.textEmail);
        radioButtonClient = findViewById(R.id.radioButtonClient);
        radioButtonRestaurant = findViewById(R.id.radioButtonRestaurant);
        buttonLogin2 = findViewById(R.id.buttonLogin2);
        textViewUsername2.setHint("Username");
        textPassword2.setHint("Password");
        textPassword3.setHint("Confirm Password");
        textPhone.setHint("Phone");
        textEmail.setHint("Email");
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

    }
    void addEvents(){
        buttonLogin2.setOnClickListener(v -> {
            createAccount();
        });

    }
    void createAccount(){
        String email = textEmail.getText().toString();
        String username = textViewUsername2.getText().toString();
        String password = textPassword2.getText().toString();
        String password2 = textPassword3.getText().toString();
        String phone = textPhone.getText().toString();

        String type = "";
        if (username.equals("") || password.equals("") || password2.equals("") || phone.equals("") || email.equals("")) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(password2)) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        // kiểm tra xem username có phải là email không
        if (!email.contains("@")) {
            Toast.makeText(RegisterActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        // kiểm tra xem password có đủ mạnh không
        if (password.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        if (radioButtonClient.isChecked()) {
            type = "client";

            Toast.makeText(RegisterActivity.this, "Client", Toast.LENGTH_SHORT).show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        System.out.println("tạo tài khoản thành công-----------------------------------------");
                        // lấy user uid của người dùng hiện tại
                        String uid = mAuth.getCurrentUser().getUid();
                        // thêm thông tin người dùng vào firestore  dựa vào uid
                        Toast.makeText(RegisterActivity.this, uid, Toast.LENGTH_SHORT).show();
                        // thêm thông tin người dùng vào firestore collection là restaurant dựa vào uid
                        DocumentReference documentReference = firestore.collection("client").document(uid);
                        // thêm thông tin phone và email vào firestore document uid của người dùng
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", username);
                        user.put("phone", phone);
                        user.put("email", email);
                        documentReference.set(user).addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        });
                        //kiểm tra xem tạo thành công document chưa

                        finish(); // Kết thúc activity hiện tại sau khi đăng nhập thành công
                    } else {
                        Toast.makeText(RegisterActivity.this,"Đã có lỗi xảy ra",Toast.LENGTH_SHORT);
                    }
                }
            });
        } else if (radioButtonRestaurant.isChecked()) {
            type = "restaurant";
            Toast.makeText(RegisterActivity.this, "Restaurant", Toast.LENGTH_SHORT).show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        System.out.println("tạo tài khoản thành công-----------------------------------------");
                        // lấy user uid của người dùng hiện tại
                        String uid = mAuth.getCurrentUser().getUid();
                        // thêm thông tin người dùng vào firestore  dựa vào uid
                        Toast.makeText(RegisterActivity.this, uid, Toast.LENGTH_SHORT).show();
                        // thêm thông tin người dùng vào firestore collection là restaurant dựa vào uid
                        DocumentReference documentReference = firestore.collection("restaurant").document(uid);
                        // thêm thông tin phone và email vào firestore document uid của người dùng
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", username);
                        user.put("phone", phone);
                        user.put("email", email);
                        user.put("idMax", 0);
                        user.put("idTableMax", 0);
                        user.put("menuRestaurant", new HashMap<String, MenuRestaurant>());
//                        // thêm thông tin menu vào firestore document uid của người dùng
//                        MenuRestaurant menuRestaurant = new MenuRestaurant();
//                        user.put("menu", menuRestaurant);
                        documentReference.set(user).addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        });
                        //tao lich su
                        HistoryRestaurant.createPast30DaysDocuments(uid);
                        finish(); // Kết thúc activity hiện tại sau khi đăng nhập thành công
                    } else {
                        Toast.makeText(RegisterActivity.this,"Đã có lỗi xảy ra",Toast.LENGTH_SHORT);
                    }
                }
            });
        }
        if (type.equals("")) {
            Toast.makeText(RegisterActivity.this, "Vui lòng chọn loại tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }




//                    FirestoreHelper.addAccount(type,new Account(type,username, password, phone, email, ""));
//                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//            startActivity(intent);





    }
}