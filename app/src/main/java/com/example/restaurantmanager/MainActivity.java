package com.example.restaurantmanager;

import android.health.connect.datatypes.SleepSessionRecord;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView textViewLocation;
    ImageButton imageButtonLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView truyền layout vào
        //R là lớp tài nguyên, layout là thư mục chứa layout, activity_main là file layout
        setContentView(R.layout.activity_main);
        addEvents();
        addControls();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void addEvents() {
    }

    private void addControls() {
        textViewLocation = this.<TextView>findViewById(R.id.textViewLocation);
        imageButtonLocation = this.<ImageButton>findViewById(R.id.imageButtonLocation);
    }

    public void getLocation(View v) {
        //code lấy vị trí
        Toast.makeText(this, "Đang lấy vị trí", Toast.LENGTH_SHORT).show();
        textViewLocation.setText("Hà Nội");
    }
}