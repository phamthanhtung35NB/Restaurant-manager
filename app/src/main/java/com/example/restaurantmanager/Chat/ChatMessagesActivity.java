package com.example.restaurantmanager.Chat;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.Chat.ChatAdapter;
import adapter.Chat.ChatList;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessagesActivity extends AppCompatActivity {

    ImageView backBtn;
    TextView nameTV ;
    EditText messageEditText;
    CircleImageView profilePic;
    ImageView sendBtn;
    String name,phone;
    String chatKey;

    String getPhone;
//    String userMobeile = "1234";
    String getUserMobeile = "1234";
    private RecyclerView chattingRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadFirstTime = true;
    final List<ChatList> chatList= new ArrayList<>();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_messages);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

//        create chat
        createChat();

        addEvents();
    }
    void createChat(){
        //create chat

    }
    void init(){
        backBtn = findViewById(R.id.backBtn);
        nameTV = findViewById(R.id.name);
        messageEditText= findViewById( R.id.messageEditTxt);
        profilePic = findViewById(R.id.profilePic);
        sendBtn = findViewById(R.id.sendBtn);
        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);



        String getName = getIntent().getStringExtra("name");
        String getProfilePic = getIntent().getStringExtra("profilePic");
        phone = getIntent().getStringExtra("phone");
        chatKey = getIntent().getStringExtra("chatKey");

        getUserMobeile= 123456789+"";
        nameTV.setText(getName);
        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(chatList,ChatMessagesActivity.this);
        chattingRecyclerView.setAdapter(chatAdapter);
        Picasso.get().load(getProfilePic).into(profilePic);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (chatKey.isEmpty()){
                    chatKey = "1";
                    if (snapshot.hasChild("chat")){
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount());
                        System.out.println("chatKey: " + chatKey);
                    }
                }
                System.out.println("ChatMessagesActivity.onCreate.onDataChange");
                if (snapshot.hasChild("chat")){
                    System.out.println("ChatMessagesActivity.onCreate.onDataChange2");
                    if (snapshot.child("chat").child(chatKey).hasChild("message")){
                        System.out.println("ChatMessagesActivity.onCreate.onDataChange3");
                            loadFirstTime = false;
                            chatList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.child("chat").child(chatKey).child("message").getChildren()){
                            System.out.println("ChatMessagesActivity.onCreate.onDataChange4");

                            Log.d(TAG, "onDataChange: " + dataSnapshot.child("msg").getValue());
                            if (dataSnapshot.hasChild("msg")&&dataSnapshot.hasChild("phone")){
                                final String messageTimeStamps = dataSnapshot.getKey();
                                final String getMsg = dataSnapshot.child("msg").getValue().toString();
                                final String getPhone = dataSnapshot.child("phone").getValue().toString();

                                java.sql.Timestamp timestamp = new java.sql.Timestamp(Long.parseLong(messageTimeStamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                ChatList chatList1 = new ChatList(getPhone,getName,getMsg,simpleDateFormat.format(date),simpleTimeFormat.format(date));
                                chatList.add(chatList1);
                                if (loadFirstTime==true){
//                                        chattingRecyclerView.smoothScrollToPosition(chatList.size()-1);
                                    chatAdapter.updateChatList(chatList );

                                    chattingRecyclerView.setAdapter(chatAdapter);
                                    chattingRecyclerView.smoothScrollToPosition(chatList.size()-1);
                                    loadFirstTime = false;
                                }
                            }
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });

        chatAdapter = new ChatAdapter(chatList,ChatMessagesActivity.this);
        chattingRecyclerView.setAdapter(chatAdapter);
    }
    void addEvents(){
        sendBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String message = messageEditText.getText().toString();
                   if (!message.isEmpty()){
                          //send message
                       System.out.println("ChatMessagesActivity.onCreate.addEvents.onClick");
                            String currentDateTimeString = String.valueOf(System.currentTimeMillis()).substring(0,10);
                            databaseReference.child("chat").child(chatKey).child("restaurant").setValue("restaurant");
                            databaseReference.child("chat").child(chatKey).child("client").setValue("client");
                            databaseReference.child("chat").child(chatKey).child("message").child(currentDateTimeString).child("msg").setValue(message);
                            databaseReference.child("chat").child(chatKey).child("message").child(currentDateTimeString).child("phone").setValue(getUserMobeile);
                          messageEditText.setText("");

                    }
           }
        });
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}