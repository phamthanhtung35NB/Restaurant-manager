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
    static String profilePic;
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
        name = sharedPreferences.getString("username", "");
        profilePic = sharedPreferences.getString("profilePic", "");
        System.out.println("uid: "+uid);
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
                messagesLists.clear(); // Xóa danh sách tin nhắn
                unseenMessages = 0; // Đặt lại số lượng tin nhắn chưa xem
                lastMessage = ""; // Đặt lại tin nhắn cuối cùng
                chatKey = ""; // Đặt lại khóa chat
                // Duyệt qua tất cả các nút con của nút "user"
                for (DataSnapshot dataSnapshot : snapshot.child("user").getChildren()){
                    final String chatKey = dataSnapshot.getKey().trim(); // Lấy khóa của nút con hiện tại
                    dataSet = false; // Đặt lại cờ dataSet
                    // Kiểm tra xem khóa của nút con hiện tại có khác với uid của người dùng và loại là "restaurant" hay không
                    if (!chatKey.equals(uid) && dataSnapshot.child("type").getValue(String.class).trim().equals("restaurant")) {
                        final String getProfile_pic = dataSnapshot.child("profile_pic").getValue(String.class); // Lấy hình ảnh đại diện
                        String getName = dataSnapshot.child("username").getValue(String.class); // Lấy tên người dùng
                        System.out.println("getUid: " + chatKey + " uid: " + uid);
                        // Lấy dữ liệu chat từ Firebase
                        databaseReference.child("chat").child(chatKey).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                if (dataSnapshot1.child("lastMessage").getValue(String.class) != null) {
                                    lastMessage = dataSnapshot1.child("lastMessage").getValue(String.class);
                                } else {
                                    lastMessage = "";
                                }
                                if (dataSnapshot1.child("sdtRestaurant").getValue(String.class) != null) {
                                    phone = dataSnapshot1.child("sdtRestaurant").getValue(String.class);
                                } else {
                                    phone = "0";
                                }

                                // Kiểm tra xem cờ dataSet có phải là false hay không
                                if (!dataSet){
                                    dataSet = true; // Đặt cờ dataSet thành true
                                    // Tạo một đối tượng MessagesList mới và thêm nó vào danh sách
                                    MessagesList mesagesList = new MessagesList( getName, phone, lastMessage, getProfile_pic, unseenMessages, chatKey);
                                    messagesLists.add(mesagesList);
                                    messagesAdapter.updateList(messagesLists); // Cập nhật adapter với danh sách mới
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Xử lý lỗi
                            }
                        });

                    } else {
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