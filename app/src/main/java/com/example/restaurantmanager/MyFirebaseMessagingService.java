package com.example.restaurantmanager;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG ="MyFirebaseMessagingService" ;

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
            // Handle the notification payload here
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
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
                sendRegistrationToServer(token,type);
            }
        });
    }"ftoBTzOuSRVmwZiCd-PuFD:APA91bH7c4vWD99tqqqFmWXD6u830ewhFssQQRT3Nh0rupSR8s_YIzXUMBFyY2Sl2wfdmWNMoPYwAhuhHvelerN8smDSU60NRbbFDZpZJKUbCKRiQvrg59xIv0yHwMbSN7JoFnqDSZUr"


    private static void sendRegistrationToServer(String token, String type) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationToServer: sending token " + token);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Save the user's token to Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            db.collection(type).document(user.getUid()).update(data)//, SetOptions.merge())
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
}
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // ...
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        String TAG="MyFirebaseInstanceIDService";
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
////        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//    }
//    @Override
//    public void onTokenRefresh() {
//        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(refreshedToken);
//    }
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        if (notification != null) {
//            String title = notification.getTitle();
//            String body = notification.getBody();
//            // Hiển thị thông báo
//            sendNotification(title, body);
////            NotificationHelper.displayNotification(getApplicationContext(), title, body);
//        }
//    }
//
//    private void sendNotification(String title, String body) {
//        Intent intent = new Intent(this, LoginActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        // Hiển thị thông báo
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, MyApplication.CHANNEL)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setSmallIcon(R.drawable.avata)
//                .setContentIntent(pendingIntent);
//        Notification notification = notificationBuilder.build();
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (notificationManager != null) {
//            notificationManager.notify(0, notification);
//        }
//    }

//}
