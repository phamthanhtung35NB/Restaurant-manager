package com.example.restaurantmanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.Messages.MessagesList;
import adapter.Messages.MessagesAdapter;

public class ChatActivity extends AppCompatActivity {

    private List<MessagesList> messagesLists= new ArrayList<>();
    private static final String TAG = "ChatActivity";
    String name,phone,email;
    int unseenMessages=0;
    private String lastMessage;
    private String chatKey;
    private RecyclerView messageRecyclerView;
    private MessagesAdapter messagesAdapter;
    boolean dataSet=false;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        System.out.println("ChatActivity.onCreate");

        messageRecyclerView = findViewById(R.id.messageRecyclerView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        messageRecyclerView.setHasFixedSize(true);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        messageRecyclerView.setAdapter(new MessagesAdapter(messagesLists, ChatActivity.this));
        //set adapter
        messagesAdapter = new MessagesAdapter(messagesLists, ChatActivity.this);
        messageRecyclerView.setAdapter(messagesAdapter);

        //get profile data
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                final String profilePic = snapshot.child("profilePic").getValue().toString();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "onCancelled: " + error.getMessage());
//            }
//        }
        String phone = "123456789";
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesLists.clear();
                unseenMessages = 0;
                lastMessage = "";
                chatKey = "";
                for (DataSnapshot dataSnapshot : snapshot.child("user").getChildren()){

                    final String getPhone = dataSnapshot.getKey();
                    dataSet = false;
                    System.out.println("getPhone: " + getPhone);
                    if (!getPhone.equals(phone)){
                        final String getProfile_pic = dataSnapshot.child("profile_pic").getValue(String.class);
                        String getName = dataSnapshot.child("name").getValue(String.class);
                        lastMessage = dataSnapshot.child("lastMessage").getValue(String.class);
                        System.out.println("name: " + getName);
                        System.out.println("getProfile_pic: " + getProfile_pic);
                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int getChatCount = (int) snapshot.getChildrenCount();
                                System.out.println("getChatCount: " + getChatCount);
                                if (getChatCount > 0){
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        String chatKey = dataSnapshot1.getKey();
                                        if (dataSnapshot1.hasChild("restaurant") && dataSnapshot1.hasChild("client")&& dataSnapshot1.hasChild("messages")) {
                                            String getUserOne = dataSnapshot1.child("restaurant").getValue(String.class);
                                            String getUserTwo = dataSnapshot1.child("client").getValue(String.class);
                                            if ((getUserOne.equals(getPhone) && getUserTwo.equals(phone))||
                                                    (getUserOne.equals(phone) && getUserTwo.equals(getPhone))) {
                                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {
                                                    long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    long getLastMessage = 10;
                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if (getMessageKey>getLastMessage) {
                                                        unseenMessages++;
                                                    }
                                                }
                                            }
                                        }

                                    }
                                    if (!dataSet){
                                        dataSet = true;
                                        MessagesList mesagesList = new MessagesList( getName, getPhone, lastMessage, getProfile_pic, unseenMessages, chatKey);
                                        messagesLists.add(mesagesList);
                                        messagesAdapter.updateList(messagesLists);
                                    }

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        }
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}