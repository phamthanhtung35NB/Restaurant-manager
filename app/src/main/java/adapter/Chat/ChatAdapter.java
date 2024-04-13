package adapter.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;

import java.util.List;

import adapter.Messages.MessagesAdapter;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatList> chatList;
    private Context context;

    public ChatAdapter(List<ChatList> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatList list2 = chatList.get(position);
        if (list2.getPhone().equals("userMobeile")) {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.opoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getTime());
        } else {
            holder.myLayout.setVisibility(View.GONE);
            holder.opoLayout.setVisibility(View.VISIBLE);

            holder.opoMessage.setText(list2.getMessage());
            holder.opoTime.setText(list2.getTime());
        }

    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }
    public void updateList(List<ChatList> list){
        this.chatList = list;
        notifyDataSetChanged();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout opoLayout,myLayout;
        private TextView opoMessage,myMessage;
        private TextView opoTime,myTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            opoLayout = itemView.findViewById(R.id.opoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            opoMessage = itemView.findViewById(R.id.opoMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            opoTime = itemView.findViewById(R.id.opoTime);
            myTime = itemView.findViewById(R.id.myTime);
        }

    }
}
