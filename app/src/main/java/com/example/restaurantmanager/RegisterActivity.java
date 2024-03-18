package com.example.restaurantmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
    Button buttonLogin2;

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
        buttonLogin2 = findViewById(R.id.buttonLogin2);
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

        if (false==FirestoreHelper.checkDocumentExistence("account", username)) {
            if (false==FirestoreHelper.checkDocumentExistence("client", username)) {
                if (password.equals(password2)) {
                    FirestoreHelper.addAccount(new Account("0",username, password, "", "", ""));
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