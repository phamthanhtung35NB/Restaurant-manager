package com.example.restaurantmanager.Client.Messages;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurantmanager.Chat.ChatMessagesActivity;
import com.example.restaurantmanager.FireBase.Notifications.MyFirebaseMessagingService;
import com.example.restaurantmanager.MenuRestaurant.StatisticalFragment;
import com.example.restaurantmanager.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.Chat.ChatAdapter;
import adapter.Chat.ChatList;
import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment {
    ImageView backBtn;
    TextView nameTV,status;
    EditText messageEditText;
    CircleImageView profilePic;
    ImageView sendBtn;
    String myName,myPhone,myChatKey,myProfilePic;
    String opoName,opoPhone,opoChatKey;
    private ValueEventListener seenListener;
    private ValueEventListener messageListener;
    private RecyclerView chattingRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadFirstTime = true;
    final List<ChatList> chatList= new ArrayList<>();
    DatabaseReference databaseReferences= FirebaseDatabase.getInstance().getReference();
    DatabaseReference databaseReferenceSeen= FirebaseDatabase.getInstance().getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        init(view);
        addEvents(view);
//        startChecking();
        return view;
    }
    void init(View view){
        backBtn = view.findViewById(R.id.backBtn);
        nameTV = view.findViewById(R.id.name);
        messageEditText= view.findViewById( R.id.messageEditTxt);
        profilePic = view.findViewById(R.id.profilePic);
        sendBtn = view.findViewById(R.id.sendBtn);
        chattingRecyclerView = view.findViewById(R.id.chattingRecyclerView);
        status = view.findViewById(R.id.status);

        Bundle bundle = getArguments();
        if (bundle!=null){
            opoName = bundle.getString("username");
            opoPhone = bundle.getString("phone");
            opoChatKey = bundle.getString("chatKey");
            System.out.println("11111111111111111");
            System.out.println("nhà hàng" + "username: " + opoName + "phone: " + opoPhone + "opoChatKey: " + opoChatKey);
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
        myName = sharedPreferences.getString("username","");
        myPhone=sharedPreferences.getString("phone","");
        myChatKey = sharedPreferences.getString("uid", "");
        myName = sharedPreferences.getString("username","");
        myProfilePic = sharedPreferences.getString("profilePic", "");

        nameTV.setText(opoName);
        // Kiểm tra xem nhà hàng đã xem tin nhắn chưa
        seenListener=databaseReferenceSeen.child("chat").child(opoChatKey).child(myChatKey)
                .child("restaurantSeen").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            boolean restaurantSeen = dataSnapshot.getValue(Boolean.class);
                            if (restaurantSeen) {
                                status.setText("Đã xem");
                                // set color for status
                                status.setTextColor(getResources().getColor(R.color.green));
                            } else {
                                status.setText("Đã gửi");
                                // set color for status
                                status.setTextColor(getResources().getColor(R.color.gray));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi tại đây nếu có
                    }
                });

        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatAdapter = new ChatAdapter(chatList, getActivity());
        profilePic.setImageResource(R.drawable.account);
        chatList.clear();
        messageListener=databaseReferences.child("chat").child(opoChatKey).child(myChatKey).child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    // Kiểm tra xem có tồn tại key "msg" và "phone" trong dataSnapshot không
                    if (snapshot.hasChild("msg") && snapshot.hasChild("phone")) {
                        System.out.println("có tin nhắn mới");
                        final String messageTimeStamps = snapshot.getKey();
                        final String getMsg = snapshot.child("msg").getValue().toString();
                        final String getPhone = snapshot.child("phone").getValue().toString();

                        SimpleDateFormat inputFormat = new SimpleDateFormat("ddMMyyyyHHmmss", new Locale("vi", "VN"));
                        try {
                            Date date = inputFormat.parse(messageTimeStamps);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("vi", "VN"));
                            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm aa", new Locale("vi", "VN"));
                            String formattedDate = simpleDateFormat.format(date);
                            String formattedTime = simpleTimeFormat.format(date);
                            formattedTime = formattedTime.replace("SA", "AM").replace("CH", "PM");
                            ChatList chatList1 = new ChatList(getPhone, myName, getMsg, formattedDate, formattedTime);
                            chatList.add(chatList1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                databaseReferences.child("chat").child(opoChatKey).child(myChatKey).child("clientSeen").setValue(true);
                chatAdapter.updateChatList(chatList);
                chattingRecyclerView.setAdapter(chatAdapter);
                chattingRecyclerView.scrollToPosition(chatList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error reading data: " + databaseError.getMessage());
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        if (seenListener != null) {
            databaseReferenceSeen.removeEventListener(seenListener);
        }
        if (messageListener != null) {
            databaseReferences.removeEventListener(messageListener);
        }
    }
//    void init(View view){
//
//        backBtn = view.findViewById(R.id.backBtn);
//        nameTV = view.findViewById(R.id.name);
//        messageEditText= view.findViewById( R.id.messageEditTxt);
//        profilePic = view.findViewById(R.id.profilePic);
//        sendBtn = view.findViewById(R.id.sendBtn);
//        chattingRecyclerView = view.findViewById(R.id.chattingRecyclerView);
//
//
////        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
////        phone = sharedPreferences.getString("phone", "");
////        email = sharedPreferences.getString("email", "");
////        uid = sharedPreferences.getString("uid", "");
//        Bundle bundle = getArguments();
//        if (bundle!=null){
//            opoName = bundle.getString("username");
//            opoPhone = bundle.getString("phone");
//            opoChatKey = bundle.getString("chatKey");
//            System.out.println("nhà hàng" + "username: " + opoName + "phone: " + opoPhone + "opoChatKey: " + opoChatKey);
//        }
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
//        myName = sharedPreferences.getString("username","");
//        myPhone=sharedPreferences.getString("phone","");
//        myChatKey = sharedPreferences.getString("uid", "");
//        System.out.println("my" + "username: " + myName + "phone: " + myPhone + "myChatKey: " + myChatKey);
//
//        nameTV.setText(opoName);
//        chattingRecyclerView.setHasFixedSize(true);
//        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        chatAdapter = new ChatAdapter(chatList, getActivity());
////        chattingRecyclerView.setAdapter(chatAdapter);
//        profilePic.setImageResource(R.drawable.account);
////        Picasso.get().load(getProfilePic).into(profilePic);
//        //kiểm tra xem có chat/uid1/uid2 chưa nếu chưa thì tạo mới
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (opoChatKey != null && myChatKey != null) {
//                    if (snapshot.hasChild("chat")){
//                        if (!snapshot.child("chat").hasChild(opoChatKey)){
//                            databaseReference.child("chat").child(opoChatKey).child(myChatKey).child("restaurant").setValue("restaurant");
//                            databaseReference.child("chat").child(opoChatKey).child(myChatKey).child("client").setValue("client");
//                        }
//                    }
//                } else {
//                    // Handle the case where opoChatKey or myChatKey is null
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "onCancelled: " + error.getMessage());
//            }
//        });
//
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                if (chatKey.isEmpty()){
////                    chatKey = "1";
////                    if (snapshot.hasChild("chat")){
////                        databaseReference.child("chat").child(opoChatKey).child(myChatKey).child("restaurant").setValue("restaurant");
////                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount());
////                        System.out.println("chatKey: " + chatKey);
////                    }
////                }
//                System.out.println("ChatMessagesActivity.onCreate.onDataChange");
//                if (snapshot.child("chat").child(opoChatKey).hasChild(myChatKey)){
//                    System.out.println("ChatMessagesActivity.onCreate.onDataChange2");
//                    if (snapshot.child("chat").child(opoChatKey).child(myChatKey).hasChild("message")){
//                        System.out.println("ChatMessagesActivity.onCreate.onDataChange3");
//                        loadFirstTime = false;
//                        chatList.clear();
//                        for (DataSnapshot dataSnapshot : snapshot.child("chat").child(opoChatKey).child(myChatKey).child("message").getChildren()){
//                            System.out.println("ChatMessagesActivity.onCreate.onDataChange4");
//
//                            Log.d(TAG, "onDataChange: " + dataSnapshot.child("msg").getValue());
//                            if (dataSnapshot.hasChild("msg")&&dataSnapshot.hasChild("phone")){
//                                final String messageTimeStamps = dataSnapshot.getKey();
//                                final String getMsg = dataSnapshot.child("msg").getValue().toString();
//                                final String getPhone = dataSnapshot.child("phone").getValue().toString();
//
////                                java.sql.Timestamp timestamp = new java.sql.Timestamp(Long.parseLong(messageTimeStamps));
////                                Date date = new Date(timestamp.getTime());
////                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
////                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
//
//                                // Chuyển đổi chuỗi thời gian từ định dạng "yyyyMMddHHmmss" thành đối tượng Date
//                                SimpleDateFormat inputFormat = new SimpleDateFormat("ddMMyyyyHHmmss", new Locale("vi", "VN"));
//                                try {
//                                    Date date = inputFormat.parse(messageTimeStamps);
//                                    // Tạo các đối tượng SimpleDateFormat để định dạng ngày và thời gian
//                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("vi", "VN"));
//                                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm aa", new Locale("vi", "VN"));
//                                    // Định dạng ngày và thời gian
//                                    String formattedDate = simpleDateFormat.format(date);
//                                    String formattedTime = simpleTimeFormat.format(date);
//                                    // Thay thế "SA" và "CH"
//                                    formattedTime = formattedTime.replace("SA", "AM").replace("CH", "PM");
//                                    // Tạo một đối tượng ChatList với các thông tin đã định dạng
//                                    ChatList chatList1 = new ChatList(getPhone, myName, getMsg, formattedDate, formattedTime);
//                                    System.out.println("chatList1: " + chatList1.getPhone() + " " + chatList1.getName() + " " + chatList1.getMessage() + " " + chatList1.getDate() + " " + chatList1.getTime());
//                                    chatList.add(chatList1);
//
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                if (loadFirstTime==true){
//                                    chatAdapter.updateChatList(chatList);
//                                    chattingRecyclerView.setAdapter(chatAdapter);
//                                    chattingRecyclerView.smoothScrollToPosition(chatList.size()-1);
//                                    loadFirstTime = false;
//                                }
//                            }
//                        }
//
//                    }
//
//                }
////                if (loadFirstTime==true){
////                                        chattingRecyclerView.smoothScrollToPosition(chatList.size()-1);
//                    chatAdapter.updateChatList(chatList);
//                    chattingRecyclerView.setAdapter(chatAdapter);
//                    chattingRecyclerView.setAdapter(chatAdapter);
//                    chattingRecyclerView.smoothScrollToPosition(chatList.size()-1);
//                    loadFirstTime = false;
////                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "onCancelled: " + error.getMessage());
//            }
//        });
//
//        chatAdapter = new ChatAdapter(chatList,getActivity());
//        chattingRecyclerView.setAdapter(chatAdapter);
//        chattingRecyclerView.scrollToPosition(chatList.size() - 1);
//    }
    void addEvents(View view){
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();
                System.out.println("message: " + message + "opoChatKey: " + opoChatKey + "myChatKey: " + myChatKey + "myPhone: " + myPhone);
                if (!message.isEmpty() && opoChatKey != null && myChatKey != null && myPhone != null){
                    //send message
                    System.out.println("ChatMessagesActivity.onCreate.addEvents.onClick");
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
//                    String currentDateTimeString = sdf.format(new Date());
                    Locale vietnam = new Locale("vi", "VN");
                    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss", vietnam);
                    String currentDateTimeString = sdf.format(new Date());
                    System.out.println("currentDateTimeString: " + currentDateTimeString);
//                    String currentDateTimeString = String.valueOf(System.currentTimeMillis()).substring(0,10);
                    databaseReferences.child("chat").child(opoChatKey).child(myChatKey).child("message").child(currentDateTimeString).child("msg").setValue(message);
                    databaseReferences.child("chat").child(opoChatKey).child(myChatKey).child("message").child(currentDateTimeString).child("phone").setValue(myPhone);
                    databaseReferences.child("chat").child(opoChatKey).child(myChatKey).child("restaurant").setValue(opoName);
                    databaseReferences.child("chat").child(opoChatKey).child(myChatKey).child("client").setValue(myName);
                    databaseReferences.child("chat").child(opoChatKey).child(myChatKey).child("lastMessage").setValue(message);
                    databaseReferences.child("chat").child(opoChatKey).child(myChatKey).child("clientSeen").setValue(true);
                    databaseReferences.child("chat").child(opoChatKey).child(myChatKey).child("restaurantSeen").setValue(false);
                    databaseReferences.child("chat").child(opoChatKey).child(myChatKey).child("profilePic").setValue(myProfilePic);

System.out.println("hết");
                    messageEditText.setText("");

                }
//                init(view);
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ChatMessagesActivity.onCreate.addEvents.onClick");
                chatAdapter.updateChatList(chatList);

                chattingRecyclerView.setAdapter(chatAdapter);
                chattingRecyclerView.smoothScrollToPosition(chatList.size()-1);
                loadFirstTime = false;
            }
        });
        backBtn.setOnClickListener(v -> {
            // Tạo một instance mới của FragmentC
            ListMessagesFragment statisticalFragment = new ListMessagesFragment();
            // Đặt Arguments cho Fragment
            // Sử dụng FragmentManager để thay thế Fragment hiện tại bằng FragmentC
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Thay thế và thêm vào back stack
            fragmentTransaction.replace(R.id.fragment_container, statisticalFragment);
            fragmentTransaction.addToBackStack(null);
            // Commit thao tác
            fragmentTransaction.commit();
        });
    }

    private Handler handler;
    private Runnable runnable;

    /**
     * Hàm bắt đầu kiểm tra biến staticBooleanVariable trong OtherClass
     */
    void startChecking() {
        // Khởi tạo Handler và Runnable
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                checkVariable();
                handler.postDelayed(this, 5000);
            }
        };

        // Bắt đầu kiểm tra
        runnable.run();
    }

    /**
     * khi Activity bị hủy
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Dừng kiểm tra khi Activity bị hủy
        handler.removeCallbacks(runnable);
    }

    /**
     * Hàm kiểm tra biến staticBooleanVariable trong OtherClass
     */
    private void checkVariable() {
        chatAdapter.updateChatList(chatList );
        chattingRecyclerView.setAdapter(chatAdapter);
        chattingRecyclerView.scrollToPosition(chatList.size() - 1);
    }

}