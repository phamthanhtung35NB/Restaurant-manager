package model;
import static android.content.ContentValues.TAG;

import static com.example.restaurantmanager.MainActivity.dataRestaurant;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.restaurantmanager.LoginActivity;
import com.example.restaurantmanager.MainActivity;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import adapter.MenuAdapter;

public class FirestoreHelper {
    private static FirebaseFirestore db;

    public FirestoreHelper() {

    }
    public static boolean checkDocumentExistence(String document, String documentId) {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(document).document(documentId);
        Task<DocumentSnapshot> task = docRef.get();
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return task.isSuccessful();
    }
    public static void addAccount(String type,Account account) {
        db = FirebaseFirestore.getInstance();
        Map<String, Object> newData = new HashMap<>();
        newData.put("username", account.getUsername());
        newData.put("password", account.getPassword());
        newData.put("phone", account.getPhone());
        newData.put("email", account.getEmail());
        newData.put("address", account.getAddress());
        Map<String, MenuRestaurant> newData1 = new HashMap<>();
        MenuRestaurant enuRestaurant = new MenuRestaurant("0", "0", "0", 0, "0");
        newData1.put("0", enuRestaurant);
        newData.put("menuRestaurant", newData1);
        // Định nghĩa ID cho tài liệu
        String accountId = account.getUsername(); // Thay "your_custom_id" bằng ID muốn định nghĩa

        db.collection(type).document(accountId) // Sử dụng document() thay vì add()
                .set(newData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + accountId); // Log ID đã được định nghĩa
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public interface AccountCallback {
        void onAccountReceived(Account account);
        void onFailure(Exception e);
    }

//    public static void getAccount(String type, String accountId, AccountCallback callback) {
//        db = FirebaseFirestore.getInstance();
//
//        db.collection(type).document(accountId)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        String username = documentSnapshot.getString("username");
//                        String password = documentSnapshot.getString("password");
//                        String phone = documentSnapshot.getString("phone");
//                        String email = documentSnapshot.getString("email");
//                        String address = documentSnapshot.getString("address");
//                        System.out.println("-----------------------");
//                        System.out.println("username: "+username);
//                        Map<String, Object> menuRestaurantData = (Map<String, Object>) documentSnapshot.get("menuRestaurant");
//
//                        Account account = new Account(type, username, password, phone, email, address);
//                        callback.onAccountReceived(account);
//                    } else {
//                        Log.d(TAG, "No such document");
//                        callback.onAccountReceived(null);
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.w(TAG, "Error getting document", e);
//                    callback.onFailure(e);
//                });
//    }



    public static String type = "restaurant";
    public static String accountId = "tung";
    public static void getMenuRestaurant() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(type).document(accountId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> accountData = documentSnapshot.getData();

                            if (accountData.containsKey("menuRestaurant")) {
                                Map<String, Object> menuRestaurantData = (Map<String, Object>) accountData.get("menuRestaurant");

                                // Lặp qua các mục của menuRestaurantData và tạo các đối tượng MenuRestaurant tương ứng
                                for (Map.Entry<String, Object> entry : menuRestaurantData.entrySet()) {
                                    String menuId = entry.getKey();
                                    Map<String, Object> menuData = (Map<String, Object>) entry.getValue();

                                    // Lấy dữ liệu từ menuData và tạo đối tượng MenuRestaurant
                                    String name = (String) menuData.get("name");
                                    String description = (String) menuData.get("description");
                                    double price = (double) menuData.get("price");
                                    String image = (String) menuData.get("image");

                                    // Tạo đối tượng MenuRestaurant từ dữ liệu thu được
                                    MenuRestaurant menuRestaurant_ = new MenuRestaurant(menuId, name, description, price, image);
                                    dataRestaurant.add(menuRestaurant_);
//                                    MainActivity.dataRestaurant.add(menuRestaurant_);
                                    // Sử dụng đối tượng menuRestaurant ở đây theo ý của bạn (ví dụ: thêm vào danh sách)
                                    //print data
//                                    System.out.println("-------++++++++++++++++++++++++++++++----------------------");
//                                    System.out.println(MainActivity.dataRestaurant.size());
//                                    for (MenuRestaurant menuRestaurant : MainActivity.dataRestaurant) {
//                                        System.out.println(menuRestaurant.getId()+" "+menuRestaurant.getName()+" "+menuRestaurant.getDescription()+" "+menuRestaurant.getPrice()+" "+menuRestaurant.getImage());
//                                    }
                                }

                            } else {
                                Log.d(TAG, "No menuRestaurant found for account: " + accountId);
                            }
                        } else {
                            Log.d(TAG, "No such account exists with ID: " + accountId);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting account data for ID: " + accountId, e);
                    }
                });
    }

    public static void addMenuRestaurant(String accountId, MenuRestaurant menuRestaurant) {
        // Lấy tham chiếu đến Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo đối tượng Map chứa dữ liệu của MenuRestaurant mới
        Map<String, Object> newMenuData = new HashMap<>();
        newMenuData.put("id", menuRestaurant.getId());
        newMenuData.put("name", menuRestaurant.getName());
        newMenuData.put("description", menuRestaurant.getDescription());
        newMenuData.put("price", menuRestaurant.getPrice());
        newMenuData.put("image", menuRestaurant.getImage());

        // Thực hiện truy vấn để thêm MenuRestaurant mới vào collection "menuRestaurant"
        db.collection("restaurant").document(accountId)
                .update("menuRestaurant." + menuRestaurant.getId(), newMenuData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "MenuRestaurant added successfully for account: " + accountId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding MenuRestaurant for account: " + accountId, e);
                    }
                });
    }
    public static void updateMenuRestaurant(String accountId, String menuId, MenuRestaurant updatedMenu) {
        // Lấy tham chiếu đến Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo đối tượng Map chứa dữ liệu cập nhật của MenuRestaurant
        Map<String, Object> updatedMenuData = new HashMap<>();
        updatedMenuData.put("id", updatedMenu.getId());
        updatedMenuData.put("name", updatedMenu.getName());
        updatedMenuData.put("description", updatedMenu.getDescription());
        updatedMenuData.put("price", updatedMenu.getPrice());
        updatedMenuData.put("image", updatedMenu.getImage());

        // Thực hiện truy vấn để cập nhật MenuRestaurant trong collection "account"
        db.collection("account").document(accountId)
                .update("menuRestaurant." + menuId, updatedMenuData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "MenuRestaurant updated successfully for account: " + accountId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating MenuRestaurant for account: " + accountId, e);
                    }
                });
    }
    public void addClient(Account account) {
        db.collection("client")
                .document(account.getUsername())
                .set(account)
                .addOnSuccessListener(aVoid -> {
                    // Đã thêm thành công
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi thất bại
                });
    }
    public static void removeMenuRestaurant(String accountId, String menuIdToRemove) {
        // Lấy tham chiếu đến Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo một đối tượng Map chứa giá trị null cho mục bạn muốn xóa
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("menuRestaurant." + menuIdToRemove, FieldValue.delete());

        // Thực hiện truy vấn để xóa MenuRestaurant
        db.collection("account").document(accountId)
                .update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "MenuRestaurant removed successfully for account: " + accountId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error removing MenuRestaurant for account: " + accountId, e);
                    }
                });
    }


    public static void checkCredentials(String username, String password, OnCheckCompleteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("restaurant").document(username).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // check
                            String storedPassword = documentSnapshot.getString("password");
                            if (storedPassword != null && storedPassword.equals(password)) {
                                //khớp
                                listener.onCheckComplete(true);
                            } else {
                                listener.onCheckComplete(false);
                            }
                        } else {
                            // Không tìm thấy
                            listener.onCheckComplete(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi
                        listener.onCheckComplete(false);
                    }
                });
    }

    // Interface để trả về kết quả
    public interface OnCheckCompleteListener {
        void onCheckComplete(boolean result);
    }

}
