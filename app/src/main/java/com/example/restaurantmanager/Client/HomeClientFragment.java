package com.example.restaurantmanager.Client;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.restaurantmanager.FireBase.FireBase;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.Client.RestaurantAdapter;
import model.Restaurant;


public class HomeClientFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageButton imgBtnMap;
    private RestaurantAdapter adapter;
    public static List<Restaurant> restaurantList;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_client, container, false);
        init(view);
        addEvents(view);
        return view;
    }
    private TextView textView;
    // server key từ firebase console project setting cloud messaging
    public static final String SERVER_KEY = "AAAAl-xT4ko:APA91bGASnqgklF4OfVR6ls42PxiSI1Lzj2Aj8qYqdlCgk4LKApgGGpE1oH_GzLgBqjheSfQqHc3_qrdcsT4cwOGAbGCwgdUNpLmLx-tdGLo_NtbC-rZrqiDBtcP5qI6xI_YrefHOAtX";

    void init(View view) {
        textView = view.findViewById(R.id.textView);
        imgBtnMap = view.findViewById(R.id.imgBtnMap);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        String a = sharedPreferences.getString("url", "");
        if (a.length()>2){
            // Tạo một instance mới của MenuClientFragment
            MenuClientFragment menuClientFragment = new MenuClientFragment();
            MainActivity.isCheckQR = true;

            // Sử dụng FragmentManager để thay thế Fragment hiện tại bằng MenuClientFragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Thay thế và thêm vào back stack
            fragmentTransaction.replace(R.id.fragment_container, menuClientFragment);
            fragmentTransaction.addToBackStack(null);

            // Commit thao tác
            fragmentTransaction.commit();
        }
        recyclerView = view.findViewById(R.id.recycler_view);
        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(restaurantList);
        getRestaurant();
    }
    void addEvents(View view) {
        imgBtnMap.setOnClickListener(v -> {
            // Tạo một instance mới của MapFragment
            MapFragment mapFragment = new MapFragment();

            // Sử dụng FragmentManager để thay thế Fragment hiện tại bằng MapFragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Thay thế và thêm vào back stack
            fragmentTransaction.replace(R.id.fragment_container, mapFragment);
            fragmentTransaction.addToBackStack(null);

            // Commit thao tác
            fragmentTransaction.commit();
        });
    }

    /**
     * Hàm lấy thông tin nhà hàng từ firebase
     *
     */
    void getRestaurant() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //lấy thông tin nhà hàng từ firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("restaurant").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
//                    Restaurant restaurant = document.toObject(Restaurant.class);
                    String phone = document.getString("phone");
                    String username = document.getString("username");
                    String address = document.getString("address");
                    String location = document.getString("location");
                    String image = document.getString("profilePic");
                    String description = document.getString("description");
                    //tách chuỗi location
                    if (location != null) {
                        String[] arr = location.split("_");
                        double latitude = Double.parseDouble(arr[0]);
                        double longitude = Double.parseDouble(arr[1]);
                    }else {
                        location = "0_0";
                    }

//                    String description = document.getString("description");
                    //tableMax type is number
                    Long tableMax = document.getLong("idTableMax");
                    Long idMax = document.getLong("idMax");

                    System.out.println("restaurant________" + tableMax + idMax + phone + username + address + location);
                    if (tableMax != null && idMax != null) {
                        Restaurant restaurant = new Restaurant(tableMax.intValue(), idMax.intValue(), phone, username,description, address, location, image);
                        restaurantList.add(restaurant);
                    }else {
                        Restaurant restaurant = new Restaurant(0, 0, phone, username);
                        restaurantList.add(restaurant);
                    }
                }
                adapter.notifyDataSetChanged();
                } else {
                    Log.d("RestaurantActivity", "Error getting documents: ", task.getException());
                }
        });

    }

    /**
     * Hàm này sẽ được gọi sau khi quét mã QR xong
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // Xử lý kết quả quét mã QR
            String content = result.getContents();
            if (content != null) {

                //tách chuỗi
                String[] arr = content.split("/");
                String userId = arr[0];
                String numberTable = arr[1];
//                tạo sharedPreferences lưu mã QR
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("url", content);
                editor.putString("accountId", userId);
                editor.putString("numberTable", numberTable);
                editor.apply();

                //lấy token của nhà hàng từ firebase
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("restaurant").document(userId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Map<String, Object> accountData = documentSnapshot.getData();
                                    String token = accountData.get("token").toString();
                                    //gửi thông báo đến nhà hàng
                                    sendNotification(token);
                                } else {
                                    Log.d(ContentValues.TAG, "No such document");
                                }
                            }
                        });
                // Tạo một instance mới của MenuClientFragment
                MenuClientFragment menuClientFragment = new MenuClientFragment();
//                menuClientFragment.setArguments(bundle);

                // Sử dụng FragmentManager để thay thế Fragment hiện tại bằng MenuClientFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Thay thế và thêm vào back stack
                fragmentTransaction.replace(R.id.fragment_container, menuClientFragment);
                fragmentTransaction.addToBackStack(null);

                // Commit thao tác
                fragmentTransaction.commit();
                //đóng activity
//                getActivity().finish();

            } else {
                textView.setText("Không tìm thấy mã QR");
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Hàm gửi thông báo đến Restaurant
     * @param content Token của nhà hàng
     */
    public static void sendNotification(String content) {
        //lấy token từ uri của user
        String token = FireBase.tokenRtn;
        System.out.println("token: " + token);
        //gửi thông báo đến token
        sendMessageToToken(content,"Thông báo","Có khách hàng mới đến quét mã QR");
        sendMessageFromUser1ToUser2(content,"Thông báo","Có khách hàng mới đến quét mã QR");
    }

    //gửi thông báo in app cho user
    public static void sendMessageFromUser1ToUser2(String user2Token,String title,String body) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create URL instance.
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    // Create connection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // Set method as POST
                    connection.setRequestMethod("POST");
                    // Set headers
                    connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);
                    connection.setRequestProperty("Content-Type", "application/json");
                    // Enable input and output streams
                    connection.setDoOutput(true);
                    // Create the message content
                    String jsonInputString = "{\"to\": \"" + user2Token + "\", \"notification\": {\"title\": \""+title+"\", \"body\": \""+body+"\"}}";
                    // Write the output
                    try(OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                    // Get the response
                    int responseCode = connection.getResponseCode();
                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Log.d(TAG, "Response: " + response.toString());
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, "Error sending FCM message", e);
                }
            }
        });
        thread.start();
    }

    //gửi thông báo đến token
    public static void sendMessageToToken(String token,String title,String body) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create URL instance.
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");

                    // Create connection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set method as POST
                    connection.setRequestMethod("POST");

                    // Set headers
                    connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);
                    connection.setRequestProperty("Content-Type", "application/json");

                    // Enable input and output streams
                    connection.setDoOutput(true);

                    // Create the message content
                    String jsonInputString = "{\"to\": \"" + token + "\", \"notification\": {\"title\": \""+title+"\", \"body\": \""+body+"\"}}";

                    // Write the output
                    try(OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                    // Get the response
                    int responseCode = connection.getResponseCode();
                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Log.d(TAG, "Response: " + response.toString());
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, "Error sending FCM message", e);
                }
            }
        });

        thread.start();
    }
}