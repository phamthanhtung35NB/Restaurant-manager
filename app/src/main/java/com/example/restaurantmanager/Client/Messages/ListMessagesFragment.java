package com.example.restaurantmanager.Client.Messages;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurantmanager.ChatActivity;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.Messages.MessagesAdapter;
import adapter.Messages.MessagesList;


public class ListMessagesFragment extends Fragment {

    static private List<MessagesList> messagesLists= new ArrayList<>();
    private static final String TAG = "ChatActivity";
    static String name,phone,email,uid;
    static int unseenMessages=0;
    static private String lastMessage;
    static private String chatKey;
    private RecyclerView messageRecyclerView;
    static private MessagesAdapter messagesAdapter;
    static boolean dataSet=false;

    static DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_messages, container, false);
        init(view);
        addEvents(view);
        getProfileData();
        return view;
    }
    void init(View view){
        messageRecyclerView = view.findViewById(R.id.messageRecyclerView);
        messageRecyclerView.setHasFixedSize(true);

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //set adapter
        messagesAdapter = new MessagesAdapter(messagesLists, getActivity());
        messageRecyclerView.setAdapter(messagesAdapter);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", "");
        email = sharedPreferences.getString("email", "");
        uid = sharedPreferences.getString("uid", "");
//        bỏ hết dấu cách ở đầu và cuối của uid
        uid = uid.trim();
    }
    void addEvents(View view){

    }
    public static void getProfileData(){
//        messagesLists.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesLists.clear();
                unseenMessages = 0;
                lastMessage = "";
                chatKey = "";
                for (DataSnapshot dataSnapshot : snapshot.child("user").getChildren()){

                    final String chatKey = dataSnapshot.getKey().trim();
                    dataSet = false;
                    System.out.println("getUid: " + chatKey);  ;
                    System.out.println("uid: " + uid);
                    if (!chatKey.equals(uid)){
                        final String getProfile_pic = dataSnapshot.child("profile_pic").getValue(String.class);
                        String getName = dataSnapshot.child("username").getValue(String.class);
                        lastMessage = dataSnapshot.child("lastMessage").getValue(String.class);
                        System.out.println("name: " + getName);
                        System.out.println("-----------khác");
                        System.out.println("getProfile_pic: " + getProfile_pic);
                        //ađ chat

                        System.out.println("dataSet: " + dataSet);
                        if (!dataSet){
                            dataSet = true;
                            MessagesList mesagesList = new MessagesList( getName, "getUid", lastMessage, getProfile_pic, unseenMessages, chatKey);
                            messagesLists.add(mesagesList);
                            messagesAdapter.updateList(messagesLists);
                        }
                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String getChatCount = String.valueOf(snapshot.getChildrenCount());
                                System.out.println("getChatCount: " + getChatCount);
                                if (!getChatCount.equals("")){

                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        String chatKey = dataSnapshot1.getKey();
                                        if (dataSnapshot1.hasChild("restaurant") && dataSnapshot1.hasChild("client")&& dataSnapshot1.hasChild("messages")) {
                                            String getUserOne = dataSnapshot1.child("restaurant").getValue(String.class);
                                            String getUserTwo = dataSnapshot1.child("client").getValue(String.class);
                                            if ((getUserOne.equals("getUid") && getUserTwo.equals(uid))||
                                                    (getUserOne.equals(uid) && getUserTwo.equals("getUid"))) {
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


                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }else {
                        System.out.println("-----------giống");



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}