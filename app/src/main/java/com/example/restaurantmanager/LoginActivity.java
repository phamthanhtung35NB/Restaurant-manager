package com.example.restaurantmanager;

//import android.app.Activity;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

//import androidx.annotation.Nullable;
//import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
//
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.restaurantmanager.OderActivity2;

//import com.example.restaurantmanager.ui.login.LoginViewModel;
//import com.example.restaurantmanager.ui.login.LoginViewModelFactory;
//import com.example.restaurantmanager.databinding.ActivityLoginBinding;

//import adapter.MenuAdapter;

import model.Account;
import model.FirestoreHelper;
import model.MenuRestaurant;

public class LoginActivity extends AppCompatActivity {
    private TextView textViewUsername;
    private EditText textPassword;
    Button buttonLogin;
    Button buttonCreateNewAccount;

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
//        addfire();

    }
    void init() {
        textViewUsername = findViewById(R.id.textViewUsername);
        textPassword = findViewById(R.id.textPassword2);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateNewAccount = findViewById(R.id.buttonCreateNewAccount);
    }
    void addEvents(){
        buttonLogin.setOnClickListener(v -> {
            login();
//            addfire();
        });
        buttonCreateNewAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }
    void login(){
        String username = textViewUsername.getText().toString();
        String password = textPassword.getText().toString();

        FirestoreHelper.checkCredentials(username, password, new FirestoreHelper.OnCheckCompleteListener() {
            @Override
            public void onCheckComplete(boolean result) {
                if (result) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
