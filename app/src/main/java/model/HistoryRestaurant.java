package model;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.restaurantmanager.MenuRestaurant.Menu.AddFoodActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HistoryRestaurant {
    public static void addHistory(ArrayList<MenuRestaurant> dataOrderClient, String accountId, String table, long bill) {

        Map<String, Object> menuRestaurantData = new HashMap<>();
        // Convert dataOrderClient to a map structure suitable for Firestore
        for (MenuRestaurant menuRestaurant : dataOrderClient) {
            Map<String, Object> dishData = new HashMap<>();
            dishData.put("id", menuRestaurant.getId());
            dishData.put("name", menuRestaurant.getName());
            dishData.put("description", menuRestaurant.getDescription());
            dishData.put("price", menuRestaurant.getPrice());
            dishData.put("image", menuRestaurant.getImage());
            menuRestaurantData.put(menuRestaurant.getId(), dishData);
            System.out.println("id: " + menuRestaurant.getId()+ "name: " + menuRestaurant.getName() + "description: " + menuRestaurant.getDescription() + "price: " + menuRestaurant.getPrice() + "image: " + menuRestaurant.getImage());
            System.out.println("vào aaaaaaaaaaaaaaaaaaaa");
        }
//        add bill
        menuRestaurantData.put("bill", bill);
        readSumFromFireBase(menuRestaurantData,  accountId,  table, bill);
//        addDataToFireBase(menuRestaurantData,  accountId,  table, bill);

    }
    public static void checkSumDayAndInitialization (String accountId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("history").document(accountId).collection(getDay())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (querySnapshot.isEmpty()) {
                            Log.d(TAG, "Tài liệu không tồn tại,tạo nó");
                            Map<String, Object> dataBillAndSum = new HashMap<>();
                            dataBillAndSum.put(getDay()+"sumDay", 0);
                            db.collection("history").document(accountId)
                                    .set(dataBillAndSum)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Tài liệu được tạo thành công!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Lỗi tạo tài liệu: ", e);
                                        }
                                    });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Lỗi khi lấy tài liệu: ", e);
                    }
                });
    }
    public static void checkSumAndInitialization (String accountId, String table){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("history").document(accountId).collection(getDay()).document(table)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                        } else {
                            Log.d(TAG, "Tài liệu không tồn tại,tạo nó");
                            Map<String, Object> dataBillAndSum = new HashMap<>();
                            dataBillAndSum.put("sum", 0);
                            db.collection("history").document(accountId).collection(getDay()).document(table)
                                    .set(dataBillAndSum)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Tài liệu được tạo thành công!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Lỗi tạo tài liệu: ", e);
                                        }
                                    });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Lỗi khi lấy tài liệu: ", e);
                    }
                });

    }
    private static void addDataToFireBase(Map<String, Object> menuRestaurantData, String accountId, String table, long bill) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new map to hold the "menuRestaurant" field and your data
        Map<String, Object> updateData = new HashMap<>();
        updateData.put(getTime(), menuRestaurantData);
        String day = getDay();
        db.collection("history").document(accountId).collection(day).document(table)
                .update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data added successfully for account: " + accountId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding data for account: " + accountId, e);
                    }
                });
    }

    private static void updateSumMax(long sum,Map<String, Object> menuRestaurantData, String accountId, String table,long bill) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("sum", sum + bill);


        db.collection("history").document(accountId).collection(getDay()).document(table)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "ID Max updated successfully!");
                        addDataToFireBase( menuRestaurantData, accountId, table, bill);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating ID Max", e);
                    }
                });
    }

    // Đọc bill and sum từ Firebase
    private static void readSumFromFireBase(Map<String, Object> menuRestaurantData, String accountId, String table,long bill) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("history").document(accountId).collection(getDay()).document(table)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> accountData = documentSnapshot.getData();
                            Long sum = (Long) accountData.get("sum");
                            Log.d(TAG, "Summmmmmmmmm: " + sum);
                            updateSumMax(sum , menuRestaurantData, accountId, table, bill);
                        } else {
                            Log.d(TAG, "No such document");

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error reading data", e);
                    }
                });
    }
    public static void readSumDayFromFireBase(String accountId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("history").document(accountId).collection(getDay())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        long totalSum = 0;
                        for (DocumentSnapshot documentSnapshot : querySnapshot) {
                            if (documentSnapshot.exists()) {
                                Map<String, Object> accountData = documentSnapshot.getData();
                                Long sum = (Long) documentSnapshot.getData().get("sum");

                                // Cộng dồn vào biến tổng
                                if (sum != null) {
                                    totalSum += sum;
                                }
                                Log.d(TAG, "Summmmmmmmmm: " + sum);
                            } else {
                                Log.d(TAG, "Tài liệu không tồn tại");
                            }
                            Log.d(TAG, "Total sum: " + totalSum);
                        }
                        //lưu tổng bill của ngày hôm đó
                        db.collection("history").document(accountId)
                                .update(getDay()+"sumDay", totalSum)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Data added successfully for account: " + accountId);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Error adding data for account: " + accountId, e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                    // ... (Mã xử lý lỗi hiện có của bạn)
                });
    }

    private static String getDay() {
        // Lấy thời gian hiện tại
        long currentTime = System.currentTimeMillis();
        // Định dạng thời gian
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault());
        String timestamp = sdf.format(new Date(currentTime));
        return timestamp;
    }
    //lấy giời phút giây
    private static String getTime() {
        // Lấy thời gian hiện tại
        long currentTime = System.currentTimeMillis();
        // Định dạng thời gian
        SimpleDateFormat sdf = new SimpleDateFormat("HH_mm", Locale.getDefault());
        String timestamp = sdf.format(new Date(currentTime));
        return timestamp;
    }
}