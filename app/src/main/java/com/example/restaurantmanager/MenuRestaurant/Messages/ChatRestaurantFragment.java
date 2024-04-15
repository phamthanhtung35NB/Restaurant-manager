package com.example.restaurantmanager.MenuRestaurant.Messages;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurantmanager.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.Chat.ChatAdapter;
import adapter.Chat.ChatList;
import de.hdodenhof.circleimageview.CircleImageView;


public class ChatRestaurantFragment extends Fragment {
    ImageView backBtn;
    TextView nameTV ;
    EditText messageEditText;
    CircleImageView profilePic;
    ImageView sendBtn;
    String myName,myPhone,myChatKey;
    String opoName,opoPhone,opoChatKey;

    private RecyclerView chattingRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadFirstTime = true;
    final List<ChatList> chatList= new ArrayList<>();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
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

        Bundle bundle = getArguments();
        if (bundle!=null){
            opoName = bundle.getString("username");
            opoPhone = bundle.getString("phone");
            opoChatKey = bundle.getString("chatKey");
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", getActivity().MODE_PRIVATE);
        myName = sharedPreferences.getString("username","");
        myPhone=sharedPreferences.getString("phone","");
        myChatKey = sharedPreferences.getString("uid", "");

        nameTV.setText(opoName);
        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatAdapter = new ChatAdapter(chatList, getActivity());
        profilePic.setImageResource(R.drawable.account);
        chatList.clear();
        databaseReference.child("chat").child(myChatKey).child(opoChatKey).child("message").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        chatList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            if (snapshot.hasChild("msg") && snapshot.hasChild("phone")) {
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
//                    String currentDateTimeString = String.valueOf(System.currentTimeMillis()).substring(0,10);
                    databaseReference.child("chat").child(myChatKey).child(opoChatKey).child("restaurant").setValue("restaurant");
                    databaseReference.child("chat").child(myChatKey).child(opoChatKey).child("client").setValue("client");
                    databaseReference.child("chat").child(myChatKey).child(opoChatKey).child("message").child(currentDateTimeString).child("msg").setValue(message);
                    databaseReference.child("chat").child(myChatKey).child(opoChatKey).child("message").child(currentDateTimeString).child("phone").setValue(myPhone);
                    messageEditText.setText("");

                }
                init(view);
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
            //finish();
        });
    }
}