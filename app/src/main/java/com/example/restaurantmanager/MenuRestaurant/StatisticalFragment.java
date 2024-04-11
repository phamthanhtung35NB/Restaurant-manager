package com.example.restaurantmanager.MenuRestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurantmanager.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.HistoryRestaurant;


public class StatisticalFragment extends Fragment {
    private List<String> xValues = Arrays.asList("1", "2", "3", "4", "5");
    BarChart barChartTK;
    TextView textViewToday,textViewWeek,textViewMonth;
    double tongSumDay=0;
    double tongSumWeek=0;
    double tongSumMonth=0;
    String accountId = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistical, container, false);
        init(view);
        addEvents(view);
        return view;
    }
    void init(View view){
        System.out.println("đầu init");
        textViewToday = view.findViewById(R.id.textViewToday);
        textViewWeek = view.findViewById(R.id.textViewWeek);
        textViewMonth = view.findViewById(R.id.textViewMonth);
        initChart(view);
        System.out.println("show chart");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        accountId = sharedPreferences.getString("uid", "");


        if (HistoryRestaurant.totalSumDay != 0){
            tongSumDay = HistoryRestaurant.totalSumDay;
        }
        if (HistoryRestaurant.totalSumWeek != 0){
            tongSumWeek = HistoryRestaurant.totalSumWeek;
        }
        if (HistoryRestaurant.totalSumMonth != 0){
            tongSumMonth = HistoryRestaurant.totalSumMonth;
        }
        pushdataTo(accountId, tongSumMonth, tongSumWeek);
        RestaurantMainActivity.lastFragment = new StatisticalFragment();
        textViewToday.setText(tongSumDay + " VNĐ");
        textViewWeek.setText(tongSumWeek + " VNĐ");
        textViewMonth.setText(tongSumMonth + " VNĐ");
        System.out.println("đọc xong");
    }
    public void pushdataTo( String accountId, double totalSumMonth, double totalSumWeek){
        System.out.println("da vao day");
        FirebaseFirestore db6 = FirebaseFirestore.getInstance();
        Map<String, Object> dataSumMonth = new HashMap<>();
        dataSumMonth.put("sumMonth", totalSumMonth);
        db6.collection("history").document(accountId).collection("sumMonthCollection").document("sumMonth")
                .set(dataSumMonth);


            FirebaseFirestore db4 = FirebaseFirestore.getInstance();
            Map<String, Object> dataSumWeek = new HashMap<>();
            dataSumWeek.put("sumWeek", totalSumWeek);
            db4.collection("history").document(accountId).collection("sumWeekCollection").document("sumWeek")
                    .set(dataSumWeek);

    }
    void addEvents(View view){
    }
    void initChart(View view){
        barChartTK = view.findViewById(R.id.barChartTK);
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
}