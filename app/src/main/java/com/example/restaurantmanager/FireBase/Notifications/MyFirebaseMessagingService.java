package com.example.restaurantmanager.FireBase.Notifications;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.restaurantmanager.FireBase.FireBase;
import com.example.restaurantmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService";
    private static final int PERMISSION_REQUEST_CODE = 123;
    public static Boolean isNotification = false;
    public static String title = "";
    public static String message = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            // Handle the data payload here
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("có thông báo");
            System.out.println("Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
            isNotification = true;
//            showDialog(this, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
//            showDialog();
        }
    }
    public void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_notification_dialog);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == Gravity.BOTTOM) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        dialog.show();
    }
    public static void showDialog(Context context, String title, String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public void showNotificationDialog(String title, String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyFirebaseMessagingService.this);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
        @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Send the new Instance ID token to your server
            System.out.println("Token: "+token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationToServer: trong lưu token " + token);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Save the user's token to Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            db.collection("chat").document(user.getUid()).set(data, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Token successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing token", e);
                        }
                    });
        }
    }


    /**
     * Gửi token của thiết bị lên firestore
     * Đường dẫn collection: type(restaurant hoặc client) / userId
     * @param context context của activity
     * @param type loại token (restaurant, client)
     */
    public static void fetchTokenAndSendToServer(final Context context,String type) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();

                // Log and toast
                String msg = context.getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                // Send the Instance ID token to your app server.
                FireBase.sendRegistrationToServer(token,type);
            }
        });
    }

}
