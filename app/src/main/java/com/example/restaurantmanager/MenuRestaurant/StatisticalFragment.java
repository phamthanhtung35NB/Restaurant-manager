package com.example.restaurantmanager.MenuRestaurant;

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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
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
        MainRestaurantActivity.lastFragment = new StatisticalFragment();
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
        System.out.println("accountId: "+accountId);
        System.out.println("totalSumMonth: "+totalSumMonth);
        System.out.println("totalSumWeek: "+totalSumWeek);
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