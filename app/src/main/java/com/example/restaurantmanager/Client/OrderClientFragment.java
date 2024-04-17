package com.example.restaurantmanager.Client;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.restaurantmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapter.Client.OrderClientAdapter;
import model.HistoryRestaurant;
import model.MenuRestaurant;
import model.SetTableStateEmptyRealtime;


public class OrderClientFragment extends Fragment {

    public static ListView listViewOrderClient;

    public static OrderClientAdapter orderClientAdapter;
    public static ArrayList<MenuRestaurant> dataOrderClient;

    public static double tong0 = 0;
    Button buttonThanhToanClient;
    TextView textThongTinBanClient;
    public static String accountId = "";
    public static String type = "restaurant";
    public static String numberTable = "";
    public static String URL = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_client, container, false);
        init(view);
        addEvents(view);
        return view;
    }

    void init(View view) {
        System.out.println("đầu init order");
        // đọc dữ liệu từ FragmentMenuClient
        Bundle bundle = getArguments();
        if (bundle != null) {
            URL = bundle.getString("url");
            System.out.println("URL: " + URL);
            accountId = bundle.getString("accountId");
            System.out.println("accountId: " + accountId);
            numberTable = bundle.getString("numberTable");
            System.out.println("numberTable: " + numberTable);

            // Sử dụng các giá trị tại đây...
        }
        listViewOrderClient = view.findViewById(R.id.listViewOrderClient);
        dataOrderClient = new ArrayList<>();
        buttonThanhToanClient = view.findViewById(R.id.buttonThanhToanClient);
        textThongTinBanClient = view.findViewById(R.id.textThongTinBanClient);
        //đọc dữ liệu từ FragmentMenuClient

        HistoryRestaurant.checkSumDayAndInitialization(accountId);
        HistoryRestaurant.checkSumAndInitialization(accountId, numberTable);
        readDataFromFireBase();
        System.out.println("cuối init order");
//        textThongTinBanClient.setText(showNameTable);
    }

    void addEvents(View view) {
        buttonThanhToanClient.setOnClickListener(v -> {
            // Xử lý sự kiện khi click vào nút thanh toán
            //xóa dữ liệu preferences my_preferences
//            SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.remove("key");
//            editor.apply();
            //tính tổng price trên firebase
            // xóa dữ liệu SharedPreferences
            SharedPreferences preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("url");
            editor.remove("accountId");
            editor.remove("numberTable");
            editor.apply();
            SetTableStateEmptyRealtime.setTableIsUsing(accountId, numberTable, "Trống");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference refOrder = database.getReference(URL);
            //cho phép quét mã QR
            MainActivity.isCheckQR = false;
            refOrder.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Lặp qua tất cả child
                    double tong = 0;
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // Tạo instance mới của ClassTable
                        MenuRestaurant menuOrder = new MenuRestaurant();
                        if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {
                            menuOrder.setPrice(childSnapshot.child("price").getValue(Double.class));
                            tong += menuOrder.getPrice();
                        }
                    }

                    //tính tổng price
                    System.out.println("tong: " + tong);
                    //xóa dữ liệu trên firebase
//                    refOrder.removeValue();
                    //chuyển sang activity thanh toán
//                    OrderClientActivity.tong0 = tong;


                    HistoryRestaurant.addHistory(dataOrderClient, accountId, numberTable, (long) tong);
                    //xóa dữ liệu brrn trong url trên firebase
                    removeOrder();
//                    Intent intent = new Intent(OrderClientActivity.this, PayTheBillClientActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", URL);
                    bundle.putString("accountId", accountId);
                    bundle.putString("numberTable", numberTable);
                    // Tạo một instance mới của FragmentC
                    OrderClientFragment fragmentC = new OrderClientFragment();

                    // Đặt Arguments cho Fragment
                    fragmentC.setArguments(bundle);

                    // Sử dụng FragmentManager để thay thế Fragment hiện tại bằng FragmentC
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Thay thế và thêm vào back stack
                    fragmentTransaction.replace(R.id.fragment_container, fragmentC);
                    fragmentTransaction.addToBackStack(null);

                    // Commit thao tác
                    fragmentTransaction.commit();

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi
                    System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
                }

            });


        });
        System.out.println("cuối addEvents order");
    }

    /**
     * Hàm đọc dữ liệu từ firebase realtime database (các món ăn đã được chọn)
     */
    void readDataFromFireBase(){
        System.out.println("đầu readDataFromFireBase");
        System.out.println("URL: " + URL);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refOrder = database.getReference(URL);
        refOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lặp qua tất cả child
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Tạo instance mới của ClassTable
                    MenuRestaurant menuOrder = new MenuRestaurant();
                    System.out.println("000000000000000000000000");
                    if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {
                        System.out.println("1111111111111111111111");
                        menuOrder.setId(childSnapshot.child("id").getValue(String.class));
                        System.out.println("2222222222222222222222");
                        menuOrder.setName(childSnapshot.child("name").getValue(String.class));
                        System.out.println("3333333333333333333333");
                        menuOrder.setDescription(childSnapshot.child("describe").getValue(String.class));
                        System.out.println("4444444444444444444444");
                        menuOrder.setImage(childSnapshot.child("image").getValue(String.class));
                        System.out.println("5555555555555555555555");
                        menuOrder.setPrice(childSnapshot.child("price").getValue(Double.class));
                        System.out.println("6666666666666666666666");
                        menuOrder.toString();
                        dataOrderClient.add(menuOrder);

                    }
                }
                orderClientAdapter = new OrderClientAdapter(getActivity(), R.layout.food_show_order_client, dataOrderClient);
                listViewOrderClient.setAdapter(orderClientAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
            }
        });
    }
    public void removeOrder() {
        // Khởi tạo Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Lấy reference đến order
        DatabaseReference orderRef = database.getReference(URL);

        // Xóa tất cả các child bên trong order
        orderRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Xóa thành công
                System.out.println("Xóa order thành công!");

            } else {
                // Xóa thất bại
                Exception e = task.getException();
                System.out.println("Xóa order thất bại: " + e.getMessage());
            }
        });
    }
}