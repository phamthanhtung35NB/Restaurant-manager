package com.example.restaurantmanager.FireBase;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FireBase {
    public static String tokenRtn = "";
    // get token from uri của user
    public static String getUserToken(String type,String userId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(type).document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> accountData = documentSnapshot.getData();
                            String token = accountData.get("token").toString();
                            Log.d(ContentValues.TAG, "Summmmmmmmmm: " + token);
                            tokenRtn = token;
                        } else {
                            Log.d(ContentValues.TAG, "No such document");
                        }
                    }
                });
        return tokenRtn;
    }


// send token to server firestore
    public static void sendRegistrationToServer(String token, String type) {
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
