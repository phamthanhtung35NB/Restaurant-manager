package com.example.restaurantmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import model.Account;
import model.FirestoreHelper;
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

    }
    void addEvents(){
        buttonLogin2.setOnClickListener(v -> {
            login();
        });

    }
    void login(){
        String username = textViewUsername2.getText().toString();
        String password = textPassword2.getText().toString();
        String password2 = textPassword3.getText().toString();
        String phone = textPhone.getText().toString();
        String email = textEmail.getText().toString();
        String type = "";
        if (radioButtonClient.isChecked()) {
            Toast.makeText(RegisterActivity.this, "Client", Toast.LENGTH_SHORT).show();
            type = "client";
        } else if (radioButtonRestaurant.isChecked()) {
            Toast.makeText(RegisterActivity.this, "Restaurant", Toast.LENGTH_SHORT).show();
        type = "restaurant";
        }
        if (username.equals("") || password.equals("") || password2.equals("") || phone.equals("") || email.equals("")) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type.equals("")) {
            Toast.makeText(RegisterActivity.this, "Vui lòng chọn loại tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(password2)) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (false==FirestoreHelper.checkDocumentExistence("account", username)) {
            if (false==FirestoreHelper.checkDocumentExistence("client", username)) {
                if (password.equals(password2)) {
                    FirestoreHelper.addAccount(type,new Account(type,username, password, phone, email, ""));
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                }
            } else {
            Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
        }

    }
}