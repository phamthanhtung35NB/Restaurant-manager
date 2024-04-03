package com.example.restaurantmanager.MenuRestaurant;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import com.github.mikephil.charting.charts.CombinedChart;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import com.example.restaurantmanager.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticalActivity extends AppCompatActivity {
    private List<String> xValues = Arrays.asList("1", "2", "3", "4", "5");
    BarChart barChartTK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistical);
        init();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void init(){
        barChartTK = findViewById(R.id.barChartTK);
        barChartTK.getAxisRight().setDrawLabels(false);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 10));
        entries.add(new BarEntry(2, 20));
        entries.add(new BarEntry(3, 30));
        entries.add(new BarEntry(4, 40));
        entries.add(new BarEntry(5, 50));
        YAxis yAxis = barChartTK.getAxisLeft();

        //chỉnh y nhỏ nhất và lớn nhất
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(50f);
        //chỉnh độ rộng của đường kẻ ngang
        yAxis.setAxisLineWidth(2f);
        //chỉnh màu của đường kẻ ngang
        yAxis.setAxisLineColor(Color.BLACK);
        //chỉnh màu của chữ
        yAxis.setTextColor(Color.BLACK);
        //chỉnh số lượng chữ trên trục y
        yAxis.setLabelCount(10);



        BarDataSet dataSet = new BarDataSet(entries, "Label");
        dataSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

        BarData barData = new BarData(dataSet);
        barChartTK.setData(barData);
        //chỉnh độ rộng cột
        barChartTK.getBarData().setBarWidth(0.5f);
        //chỉnh khoảng cách giữa các cột
//        barChartTK.groupBars(1, 0.5f, 0.5f);

        //tắt mô tả
        barChartTK.getDescription().setEnabled(false);
        //tắt chú thích
        barChartTK.invalidate();

        //chỉnh x
        barChartTK.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        //chỉnh vị trí của x
        barChartTK.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM) ;
        //chỉnh màu của chữ
        barChartTK.getXAxis().setTextColor(Color.BLACK);
        //chỉnh góc của chữ
        barChartTK.getXAxis().setLabelRotationAngle(0);
        //chỉnh độ rộng của đường kẻ dọc
        barChartTK.getXAxis().setAxisLineWidth(2f);
        //chỉnh màu của đường kẻ dọc
        barChartTK.getXAxis().setAxisLineColor(Color.BLACK);
        //chỉnh số lượng chữ trên trục x
        barChartTK.getXAxis().setLabelCount(5);
        //chỉnh độ rộng của chữ
        barChartTK.getXAxis().setTextSize(10f);
        //chỉnh độ rộng của đường kẻ ngang
        barChartTK.getAxisLeft().setAxisLineWidth(2f);
        //chỉnh màu của đường kẻ ngang
        barChartTK.getAxisLeft().setAxisLineColor(Color.BLACK);

        barChartTK.getXAxis().setGranularity(1f);

        barChartTK.getXAxis().setGranularityEnabled(true);
    }
    void addEvents(){
    }
}