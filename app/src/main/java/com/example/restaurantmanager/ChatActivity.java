package com.example.restaurantmanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.Messages.MessagesList;
import adapter.Messages.MessagesAdapter;

public class ChatActivity extends AppCompatActivity {

    private List<MessagesList> messagesLists= new ArrayList<>();
    private static final String TAG = "ChatActivity";
    String name,phone;
    private String chatKey;
    private RecyclerView messageRecyclerView;
    private MessagesAdapter messagesAdapter;
//    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("messages");
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
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    name = dataSnapshot.child("name").getValue().toString();
//                    email = dataSnapshot.child("email").getValue().toString();
//                    phone = dataSnapshot.child("phone").getValue().toString();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "onCancelled: " + error.getMessage());
//            }
//        }

        phone = "phone";
        name = "name";
        MessagesList mesagesList = new MessagesList( name, "phone","lastMessage", "profilePic", 0, "chatKey");
        messagesLists.add(mesagesList);
        MessagesList mesagesList1 = new MessagesList("name1", "phone1", "lastMessage1", "profilePic1", 0, "chatKey1");
        messagesLists.add(mesagesList1);
        MessagesList mesagesList2 = new MessagesList("name2", "phone2", "lastMessage2", "profilePic2", 0, "chatKey2");
        messagesLists.add(mesagesList2);
        System.out.println("messagesLists: " + messagesLists.size());
        messagesAdapter.updateList(messagesLists);
//        messageRecyclerView.setAdapter(new MessagesAdapter(messagesLists, ChatActivity.this));
    }
}