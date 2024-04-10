package com.example.restaurantmanager.Client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantmanager.R;

import model.NumberToWordsConverter;
import model.SetTableStateEmptyRealtime;

public class PayTheBillClientActivity extends AppCompatActivity {
    TextView textViewShowBill,textViewShowBillString;
    String table="";
    double billDouble;
    String accountId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("đã tới đây0");
        super.onCreate(savedInstanceState);
        System.out.println("đã tới đây0.5");
        EdgeToEdge.enable(this);
        System.out.println("đã tới đây0.6");
        setContentView(R.layout.activity_pay_the_bill_client);
        System.out.println("đã tới đây0.7");
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        System.out.println("đã tới đây1");
    }
    void init() {
        System.out.println("trong init");
        textViewShowBill = findViewById(R.id.textViewShowBill);
        textViewShowBillString = findViewById(R.id.textViewShowBillString);
        Intent intent = getIntent();
        table = intent.getStringExtra("table");
        // get bill as a Double directly
        billDouble = intent.getDoubleExtra("bill", 0.0);
        accountId = intent.getStringExtra("accountId");
        System.out.println("hết init");
    }
    void addEvents() {
        System.out.println("trong addEvents");
        startChecking();
        System.out.println("addEvents 2");
        String billString = NumberToWordsConverter.convert(billDouble);
        System.out.println("addEvents 3");
        textViewShowBill.setText("Tổng tiền bàn số "+table+" là: "+billDouble+" VNĐ");
        textViewShowBillString.setText(billString+" Đồng");
        System.out.println("hết addEvents");

    }



    private Handler handler;
    private Runnable runnable;

    /**
     * Hàm bắt đầu kiểm tra biến staticBooleanVariable trong OtherClass
     */
    void startChecking() {
        // Khởi tạo Handler và Runnable
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
//                SetTableStateEmptyRealtime.getTableIsUsingString(accountId,table);
                handler.postDelayed(this, 2000);
                checkVariable();
            }
        };
        // Bắt đầu kiểm tra
        runnable.run();
    }

    /**
     * Hàm kiểm tra biến staticBooleanVariable trong OtherClass
     */
    private void checkVariable() {

        if (SetTableStateEmptyRealtime.tableIsUsing.equals("Trống")) {
            System.out.println("Đã thanh toán thành công");
//            Intent intent = new Intent(PayTheBillClientActivity.this, HomeClientActivity.class);
        } else {
            System.out.println("Đang chờ thanh toán");
        }
    }

    /**
     * khi Activity bị hủy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dừng kiểm tra khi Activity bị hủy
        handler.removeCallbacks(runnable);
    }
}