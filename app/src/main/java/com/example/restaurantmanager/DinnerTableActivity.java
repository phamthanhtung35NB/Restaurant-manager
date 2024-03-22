package com.example.restaurantmanager;

import android.os.Bundle;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import adapter.TableAdapter;
import model.MenuRestaurant;
import model.Table;

public class DinnerTableActivity extends AppCompatActivity {
    GridView gvTable;
    public static ArrayList<Table> arrTableData;
    TableAdapter adapterTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dinner_table);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        addEvent();
    }
    public void init(){
        gvTable = findViewById(R.id.gvTable);
        arrTableData = new ArrayList<>();
        arrTableData.add(new Table(1,"Bàn 1","---------------------","Trống","https://i.imgur.com/ikbFUzX.png"));
        arrTableData.add(new Table(2,"Bàn 2","--------------------- 2","Trống","https://i.imgur.com/ikbFUzX.png"));
        arrTableData.add(new Table(3,"Bàn 3","--------------------- 3","Trống","https://i.imgur.com/ikbFUzX.png"));
        arrTableData.add(new Table(4,"Bàn 4","--------------------------------------------------------------- 4","Trống","https://i.imgur.com/ikbFUzX.png"));
        arrTableData.add(new Table(5,"Bàn 5","--------------------- 5","Trống","https://i.imgur.com/ikbFUzX.png"));
        arrTableData.add(new Table(6,"Bàn 6","------------------------------------------ 6","Trống","https://i.imgur.com/ikbFUzX.png"));
        arrTableData.add(new Table(7,"Bàn 7","--------------------- 7","Trống","https://i.imgur.com/ikbFUzX.png"));
        arrTableData.add(new Table(8,"Bàn 8","--------------------- 8","Trống","https://i.imgur.com/ikbFUzX.png"));
        arrTableData.add(new Table(9,"Bàn 9","--------------------- 9","Trống","https://i.imgur.com/ikbFUzX.png"));
        arrTableData.add(new Table(10,"Bàn 10","Bàn 10","Trống","https://i.imgur.com/ikbFUzX.png"));
        adapterTable = new TableAdapter(DinnerTableActivity.this,R.layout.table,arrTableData);
        gvTable.setAdapter(adapterTable);
    }
    public void addEvent(){
    }
}