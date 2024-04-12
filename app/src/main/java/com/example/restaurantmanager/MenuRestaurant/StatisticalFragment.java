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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import model.HistoryRestaurant;


public class StatisticalFragment extends Fragment {
    private List<String> xValues ;
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
        initLineChart(view);
        System.out.println("show chart");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        accountId = sharedPreferences.getString("uid", "");

            String dayTam0 = HistoryRestaurant.getDay(6);
            String dayTam1 = HistoryRestaurant.getDay(5);
            String dayTam2 = HistoryRestaurant.getDay(4);
            String dayTam3 = HistoryRestaurant.getDay(3);
            String dayTam4 = HistoryRestaurant.getDay(2);
            String dayTam5 = HistoryRestaurant.getDay(1);
            String dayTam6 = HistoryRestaurant.getDay(0);
            xValues = Arrays.asList(dayTam0, dayTam1, dayTam2, dayTam3, dayTam4, dayTam5, dayTam6);


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
        textViewToday.setText(formatNumber(tongSumDay) + " VNĐ");
        textViewWeek.setText(formatNumber(tongSumWeek) + " VNĐ");
        textViewMonth.setText(formatNumber(tongSumMonth) + " VNĐ");
        System.out.println("đọc xong");
    }
    public String formatNumber(double number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(number);
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
    public String formatDay(String day) {
    String[] parts = day.split("_");
    return parts[0] + "/" + parts[1];
}
//    void initChart(View view){
//        barChartTK = view.findViewById(R.id.barChartTK);
//        barChartTK.getAxisRight().setDrawLabels(false);
//        String dayTam0 = formatDay(HistoryRestaurant.getDay(6));
//        String dayTam1 = formatDay(HistoryRestaurant.getDay(5));
//        String dayTam2 = formatDay(HistoryRestaurant.getDay(4));
//        String dayTam3 = formatDay(HistoryRestaurant.getDay(3));
//        String dayTam4 = formatDay(HistoryRestaurant.getDay(2));
//        String dayTam5 = formatDay(HistoryRestaurant.getDay(1));
//        String dayTam6 = formatDay(HistoryRestaurant.getDay(0));
//        System.out.println("dayTam0: "+dayTam0 + " dayTam1: "+dayTam1 + " dayTam2: "+dayTam2 + " dayTam3: "+dayTam3 + " dayTam4: "+dayTam4 + " dayTam5: "+dayTam5 + " dayTam6: "+dayTam6);
//        xValues = Arrays.asList(dayTam0, dayTam1, dayTam2, dayTam3, dayTam4, dayTam5, dayTam6);
//
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        long dayInWeed[]=HistoryRestaurant.getTotalSumWeekArray;
//        for  (int i = 0; i<7; i++){
//            entries.add(new BarEntry(i, (float) dayInWeed[6-i]));
//        }
////        entries.add(new BarEntry(1, 10));
////        entries.add(new BarEntry(2, 20));
////        entries.add(new BarEntry(3, 30));
////        entries.add(new BarEntry(4, 40));
////        entries.add(new BarEntry(5, 50));
////        entries.add(new BarEntry(6, 40));
////        entries.add(new BarEntry(7, 30));
//
//        YAxis yAxis = barChartTK.getAxisLeft();
//
//        //chỉnh y nhỏ nhất và lớn nhất
//        yAxis.setAxisMinimum(0f);
//        yAxis.setAxisMaximum(HistoryRestaurant.totalSumWeekMax+100000);
//        //chỉnh độ rộng của đường kẻ ngang
//        yAxis.setAxisLineWidth(1f);
//        //chỉnh màu của đường kẻ ngang
//        yAxis.setAxisLineColor(Color.BLACK);
//        //chỉnh màu của chữ
//        yAxis.setTextColor(Color.BLACK);
//        //chỉnh số lượng chữ trên trục y
//        int max = (int) (HistoryRestaurant.totalSumWeekMax/100000)+1;
//        yAxis.setLabelCount(max);
//
//
//
//        BarDataSet dataSet = new BarDataSet(entries, "VNĐ");
//        dataSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);
//
//        BarData barData = new BarData(dataSet);
//        barChartTK.setData(barData);
//        //chỉnh độ rộng cột
//        barChartTK.getBarData().setBarWidth(0.5f);
//        //chỉnh khoảng cách giữa các cột
////        barChartTK.groupBars(1, 0.5f, 0.5f);
//
//        //tắt mô tả
//        barChartTK.getDescription().setEnabled(false);
//        //tắt chú thích
//        barChartTK.invalidate();
//
//        //chỉnh x cho biểu đồ cột
//        // x
//        barChartTK.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
//        //chỉnh vị trí của x
//        barChartTK.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM) ;
//        //chỉnh màu của chữ
//        barChartTK.getXAxis().setTextColor(Color.BLACK);
//        //chỉnh góc của chữ
//        barChartTK.getXAxis().setLabelRotationAngle(0);
//        //chỉnh độ rộng của đường kẻ dọc
//        barChartTK.getXAxis().setAxisLineWidth(2f);
//        //chỉnh màu của đường kẻ dọc
//        barChartTK.getXAxis().setAxisLineColor(Color.BLACK);
//        //chỉnh số lượng chữ trên trục x
//        barChartTK.getXAxis().setLabelCount(7);
//        //chỉnh độ rộng của chữ
//        barChartTK.getXAxis().setTextSize(10f);
//        //chỉnh độ rộng của đường kẻ ngang
//        barChartTK.getAxisLeft().setAxisLineWidth(2f);
//        //chỉnh màu của đường kẻ ngang
//        barChartTK.getAxisLeft().setAxisLineColor(Color.BLACK);
//
//        barChartTK.getXAxis().setGranularity(1f);
//
//        barChartTK.getXAxis().setGranularityEnabled(true);
//    }
    void initLineChart(View view){
    // Khởi tạo biểu đồ
    LineChart lineChartTK = view.findViewById(R.id.lineChartTK);
    // Tắt nhãn trên trục phải
    lineChartTK.getAxisRight().setDrawLabels(false);

    // Lấy các ngày trong tháng và định dạng lại
    ArrayList<String> xValues = new ArrayList<>();
//    for (int i = 29; i >= 0; i--) {
//        xValues.add(formatDay(HistoryRestaurant.getDay(i)));
//    }
        xValues.add(".");
    // Khởi tạo danh sách các mục nhập cho biểu đồ
    ArrayList<Entry> entries = new ArrayList<>();
    long dayInMonth[]=HistoryRestaurant.getTotalSumMonthArray;
    for  (int i = 0; i<30; i++){
        // Thêm mục nhập mới với giá trị từ mảng dayInMonth
        entries.add(new Entry(i, (float) dayInMonth[29-i]));
    }

    // Cấu hình trục Y
    YAxis yAxis = lineChartTK.getAxisLeft();
    yAxis.setAxisMinimum(0f);
    yAxis.setAxisMaximum(HistoryRestaurant.totalSumMonthMax+100000);
    yAxis.setAxisLineWidth(1f);
    yAxis.setAxisLineColor(Color.BLACK);
    yAxis.setTextColor(Color.BLACK);
    int max = (int) (HistoryRestaurant.totalSumMonthMax/100000)+1;
    yAxis.setLabelCount(max);

    // Tạo tập dữ liệu mới và cấu hình màu sắc
    LineDataSet dataSet = new LineDataSet(entries, "VNĐ");
    dataSet.setColor(Color.RED); // Set color for line
    dataSet.setDrawValues(false); // Hide line values

    // Tạo dữ liệu cho biểu đồ và thiết lập dữ liệu
    LineData lineData = new LineData(dataSet);
    lineChartTK.setData(lineData);

    // Tắt mô tả và chú thích
    lineChartTK.getDescription().setEnabled(false);
    lineChartTK.getLegend().setEnabled(false); // Hide legend
    lineChartTK.invalidate();

    // Cấu hình trục X
    lineChartTK.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
    lineChartTK.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    lineChartTK.getXAxis().setTextColor(Color.BLACK);
    lineChartTK.getXAxis().setLabelRotationAngle(0);
    lineChartTK.getXAxis().setAxisLineWidth(2f);
    lineChartTK.getXAxis().setAxisLineColor(Color.BLACK);
    lineChartTK.getXAxis().setLabelCount(30);
    lineChartTK.getXAxis().setTextSize(10f);
    lineChartTK.getAxisLeft().setAxisLineWidth(2f);
    lineChartTK.getAxisLeft().setAxisLineColor(Color.BLACK);

    // Cấu hình độ nhỏ nhất giữa các giá trị trên trục X
    lineChartTK.getXAxis().setGranularity(1f);
    lineChartTK.getXAxis().setGranularityEnabled(true);

    // Thêm hiệu ứng hoạt hình cho biểu đồ
    lineChartTK.animateXY(2000, 2000); // Add animation
}
    void initChart(View view){
    // Khởi tạo biểu đồ
    barChartTK = view.findViewById(R.id.barChartTK);
    // Tắt nhãn trên trục phải
    barChartTK.getAxisRight().setDrawLabels(false);

    // Lấy các ngày trong tuần và định dạng lại
    String dayTam0 = formatDay(HistoryRestaurant.getDay(6));
    String dayTam1 = formatDay(HistoryRestaurant.getDay(5));
    String dayTam2 = formatDay(HistoryRestaurant.getDay(4));
    String dayTam3 = formatDay(HistoryRestaurant.getDay(3));
    String dayTam4 = formatDay(HistoryRestaurant.getDay(2));
    String dayTam5 = formatDay(HistoryRestaurant.getDay(1));
    String dayTam6 = formatDay(HistoryRestaurant.getDay(0));

    // Thêm các ngày vào danh sách xValues
    xValues = Arrays.asList(dayTam0, dayTam1, dayTam2, dayTam3, dayTam4, dayTam5, dayTam6);

    // Khởi tạo danh sách các mục nhập cho biểu đồ
    ArrayList<BarEntry> entries = new ArrayList<>();
    long dayInWeed[]=HistoryRestaurant.getTotalSumWeekArray;
    for  (int i = 0; i<7; i++){
        // Thêm mục nhập mới với giá trị từ mảng dayInWeed
        entries.add(new BarEntry(i, (float) dayInWeed[6-i]));
    }

    // Cấu hình trục Y
    YAxis yAxis = barChartTK.getAxisLeft();
    yAxis.setAxisMinimum(0f);
    yAxis.setAxisMaximum(HistoryRestaurant.totalSumWeekMax+100000);
    yAxis.setAxisLineWidth(1f);
    yAxis.setAxisLineColor(Color.BLACK);
    yAxis.setTextColor(Color.BLACK);
    int max = (int) (HistoryRestaurant.totalSumWeekMax/100000)+1;
    yAxis.setLabelCount(max);

    // Tạo tập dữ liệu mới và cấu hình màu sắc
    BarDataSet dataSet = new BarDataSet(entries, "VNĐ");
    dataSet.setColor(Color.GREEN); // Set color for bars
    dataSet.setDrawValues(false); // Hide bar values

    // Tạo dữ liệu cho biểu đồ và thiết lập dữ liệu
    BarData barData = new BarData(dataSet);
    barChartTK.setData(barData);
    barChartTK.getBarData().setBarWidth(0.5f);

    // Tắt mô tả và chú thích
    barChartTK.getDescription().setEnabled(false);
    barChartTK.getLegend().setEnabled(false); // Hide legend
    barChartTK.invalidate();

    // Cấu hình trục X
    barChartTK.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
    barChartTK.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    barChartTK.getXAxis().setTextColor(Color.BLACK);
    barChartTK.getXAxis().setLabelRotationAngle(0);
    barChartTK.getXAxis().setAxisLineWidth(2f);
    barChartTK.getXAxis().setAxisLineColor(Color.BLACK);
    barChartTK.getXAxis().setLabelCount(7);
    barChartTK.getXAxis().setTextSize(10f);
    barChartTK.getAxisLeft().setAxisLineWidth(2f);
    barChartTK.getAxisLeft().setAxisLineColor(Color.BLACK);

    // Cấu hình độ nhỏ nhất giữa các giá trị trên trục X
    barChartTK.getXAxis().setGranularity(1f);
    barChartTK.getXAxis().setGranularityEnabled(true);

    // Thêm hiệu ứng hoạt hình cho biểu đồ
    barChartTK.animateXY(2000, 2000); // Add animation
}
}