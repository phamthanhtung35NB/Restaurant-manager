package com.example.restaurantmanager.MenuRestaurant.Table;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.restaurantmanager.MenuRestaurant.HomeRestaurantActivity;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapter.Restaurant.TableAdapter;
import model.Table;


public class ShowTableRestaurantFragment extends Fragment {
    GridView gvTable;
    ImageButton imageButtonExit,imageButtonAddTable;
    TextView textViewSummary;
    public static ArrayList<Table> arrTableData;
    TableAdapter adapterTable;
    public static String accountId = "";
    public static int idTableMax = 0;
    String type = "restaurant";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_table_restaurant, container, false);
        init(view);
        addEvents(view);
        return view;
    }
    public void init(View view){
        System.out.println("đầu tiên");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        accountId = sharedPreferences.getString("uid", "");
        System.out.println(accountId);

        gvTable = view.findViewById(R.id.gvTable);
        imageButtonExit = view.findViewById(R.id.imageButtonExit);
        textViewSummary = view.findViewById(R.id.textViewSummary);
        imageButtonAddTable = view.findViewById(R.id.imageButtonAddTable);
        arrTableData = new ArrayList<>();
        System.out.println("------------------------------------------------------");
        System.out.println("accountId: " + accountId);
        readDataFromFireBase(accountId);
    }
    public void addEvents(View view){
        imageButtonExit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HomeRestaurantActivity.class);
            startActivity(intent);
//            finish();
        });
        System.out.println("hết event-----------------------------");
        imageButtonAddTable.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTableActivity.class);
            intent.putExtra("uid",accountId);
            intent.putExtra("idMax",idTableMax);
            startActivity(intent);

        });
    }

    /**
     * Đọc dữ liệu table từ Firebase
     * @param accountId uid của node cha trong realtime database của restaurant
     *           uid này được truyền từ HomeRestaurantActivity khi click vào button Table của restaurant
     */
    public void readDataFromFireBase(String accountId){
        System.out.println("đầu readDataFromFireBase");
        System.out.println("accountId: " + accountId);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(accountId);
        // Lắng nghe giá trị của tất cả child
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // lặp qua tất cả child
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // tạo instance mới của ClassTable
                    Table table = new Table();
                    if (childSnapshot.hasChild("id") && childSnapshot.child("id").getValue() != null) {

                        table.setId(childSnapshot.child("id").getValue(Integer.class));
                        table.setName(childSnapshot.child("name").getValue(String.class));
                        table.setDescribe(childSnapshot.child("describe").getValue(String.class));
                        table.setStateEmpty(childSnapshot.child("stateEmpty").getValue(String.class));
                        table.setImage(childSnapshot.child("image").getValue(String.class));
                        System.out.println("id: "+table.getId()+" name: "+table.getName()+" describe: "+table.getDescribe()+" stateEmpty: "+table.getStateEmpty()+" image: "+table.getImage());
                        // thêm từng bàn vào mảng arrTableData
                        arrTableData.add(table);
                    }
                }

                adapterTable = new TableAdapter(getActivity(), R.layout.table, arrTableData);
                gvTable.setAdapter(adapterTable);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                System.out.println("Lỗi đọc dữ liệu: " + databaseError.getMessage());
            }
        });
    }
}