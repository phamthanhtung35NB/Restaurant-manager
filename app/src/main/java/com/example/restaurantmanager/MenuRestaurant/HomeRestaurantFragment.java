package com.example.restaurantmanager.MenuRestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.restaurantmanager.Client.OrderClientFragment;
import com.example.restaurantmanager.R;

import model.HistoryRestaurant;

public class HomeRestaurantFragment extends Fragment {
    LinearLayout lineButtonsStatistical;
    String accountId = "";
    String type = "restaurant";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_restaurant, container, false);
        init(view);
        addEvents(view);
        return view;
    }

    void init(View view){
        System.out.println("đầu init");
        lineButtonsStatistical = view.findViewById(R.id.lineButtonsStatistical);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        accountId = sharedPreferences.getString("uid", "");
        //Lấy dữ liệu sum cả ngày từ firebase
        HistoryRestaurant.readAndSaveSumDay(accountId);
        System.out.println("đọc xong ngày");
        // lấy dữ liệu sum cả tuần từ firebase
        HistoryRestaurant.readAndSaveSumWeek(accountId);
        System.out.println("đọc xong tuần");
        HistoryRestaurant.readAndSaveSumMonth(accountId);
        System.out.println("đọc xong tháng");
        System.out.println("đọc xong");
    }

    void addEvents(View view){

        //Thống kê
        lineButtonsStatistical.setOnClickListener(v -> {
            // Tạo một Bundle để chứa dữ liệu
//            Bundle bundle = new Bundle();
//            bundle.putString("url", URL);
//            System.out.println("URL: "+URL);
//            bundle.putString("accountId", accountId);
//            System.out.println("accountId: "+accountId);
//            bundle.putString("numberTable", numberTable);
//            System.out.println("numberTable: "+numberTable);

            // Tạo một instance mới của FragmentC
            StatisticalFragment statisticalFragment = new StatisticalFragment();

            // Đặt Arguments cho Fragment
//            fragmentC.setArguments(bundle);

            // Sử dụng FragmentManager để thay thế Fragment hiện tại bằng FragmentC
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Thay thế và thêm vào back stack
            fragmentTransaction.replace(R.id.fragment_container, statisticalFragment);
            fragmentTransaction.addToBackStack(null);

            // Commit thao tác
            fragmentTransaction.commit();
        });
    }
}