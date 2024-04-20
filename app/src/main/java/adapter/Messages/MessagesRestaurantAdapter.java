package adapter.Messages;



import android.content.Context;
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

import com.example.restaurantmanager.Client.Messages.ChatFragment;
import com.example.restaurantmanager.MenuRestaurant.Messages.ChatRestaurantFragment;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesRestaurantAdapter extends RecyclerView.Adapter<MessagesRestaurantAdapter.MyViewHolder> {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<MessagesList> messagesList;
    private final Context context;

    public MessagesRestaurantAdapter(List<MessagesList> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
        System.out.println("MessagesAdapter.MessagesAdapter");
    }

    @NonNull
    @Override
    public MessagesRestaurantAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesRestaurantAdapter.MyViewHolder holder, int position) {
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
                // Tạo một instance mới của ChatFragment
                ChatRestaurantFragment chatFragment = new ChatRestaurantFragment();

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
