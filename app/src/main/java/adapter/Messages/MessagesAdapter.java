package adapter.Messages;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.Chat.ChatMessagesActivity;
import com.example.restaurantmanager.Client.MainActivity;
import com.example.restaurantmanager.Client.Messages.ChatFragment;
import com.example.restaurantmanager.MenuRestaurant.StatisticalFragment;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<MessagesList> messagesList;
    private final Context context;

    public MessagesAdapter(List<MessagesList> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
        System.out.println("MessagesAdapter.MessagesAdapter");
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        System.out.println("MessagesAdapter.onBindViewHolder");
        System.out.println("--------------------");
        System.out.println("MessagesAdapter.onBindViewHolder");
        MessagesList list2 = messagesList.get(position);
        String profilePic = list2.getProfilePic();
        if (profilePic == null || profilePic.equals("")) {
            holder.profilePic.setImageResource(R.drawable.account);
        } else {
            Picasso.get().load(profilePic).into(holder.profilePic);
        }
        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessage());
        if (list2.getUnseenMessages() == 0) {
            holder.unseenMessages.setVisibility(View.GONE);
        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
        }
        System.out.println("name: "+list2.getName() + " lastMessage: "+list2.getLastMessage() + " unseenMessages: "+ list2.getUnseenMessages());
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    int getChatCount = (int) snapshot.getChildrenCount();
//                    System.out.println("getChatCount: " + getChatCount);
//                    if (getChatCount > 0){
//
//                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//                            String chatKey = dataSnapshot1.getKey();
//                            if (dataSnapshot1.hasChild("restaurant") && dataSnapshot1.hasChild("client")&& dataSnapshot1.hasChild("messages")) {
//                                String getUserOne = dataSnapshot1.child("restaurant").getValue(String.class);
//                                String getUserTwo = dataSnapshot1.child("client").getValue(String.class);
//                                if ((getUserOne.equals(getUid) && getUserTwo.equals(uid))||
//                                        (getUserOne.equals(uid) && getUserTwo.equals(getUid))) {
//                                    for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {
//                                        long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
//                                        long getLastMessage = 10;
//                                        lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
//                                        if (getMessageKey>getLastMessage) {
//                                            unseenMessages++;
//                                        }
//                                    }
//                                }
//                            }
//
//                        }
//
//
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
                // Tạo một instance mới của ChatFragment
                ChatFragment chatFragment = new ChatFragment();

                // Tạo một Bundle để chứa dữ liệu
                Bundle bundle = new Bundle();
                bundle.putString("username", list2.getName());
                bundle.putString("phone", list2.getPhone());
                bundle.putString("profilePic", list2.getProfilePic());
                bundle.putString("chatKey", list2.getChatKey());

                // Đặt Arguments cho Fragment
                chatFragment.setArguments(bundle);

                // Kiểm tra xem context có phải là một instance của FragmentActivity hay không
                if (context instanceof FragmentActivity) {
                    // Sử dụng FragmentManager để thay thế Fragment hiện tại bằng ChatFragment
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Thay thế và thêm vào back stack
                    fragmentTransaction.replace(R.id.fragment_container, chatFragment);
                    fragmentTransaction.addToBackStack(null);

                    // Commit thao tác
                    fragmentTransaction.commit();
                }
            }
        });
    }

    public void updateList(List<MessagesList> list){
        this.messagesList=list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return messagesList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profilePic;
        private TextView name;
        private TextView lastMessage;
        private TextView unseenMessages;
        private LinearLayout rootLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}
