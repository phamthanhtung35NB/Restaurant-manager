package adapter.Client;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.Client.Messages.ListMessagesFragment;
import com.example.restaurantmanager.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Restaurant;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private List<Restaurant> restaurantList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView description;

        public TextView address;
        public CircleImageView logo;
        public LinearLayout restaurant_item_click;

        public ViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            description = view.findViewById(R.id.description);
            address = view.findViewById(R.id.address);
            logo = view.findViewById(R.id.logo);
            restaurant_item_click = view.findViewById(R.id.restaurant_item_click);
        }
    }

    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.username.setText(restaurant.getUsername());
        holder.description.setText(restaurant.getDescription());
        holder.address.setText(restaurant.getAddress());

        if (restaurant.getImage() != null && !restaurant.getImage().isEmpty()) {
            Picasso.get().load(restaurant.getImage()).into(holder.logo);
        } else {
            holder.logo.setImageResource(R.drawable.account);

        }
        holder.restaurant_item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Restaurant: " + restaurant.getUsername(), Toast.LENGTH_SHORT).show();
                showDialogRestaurant(holder,restaurant.getUsername(),restaurant.getIdTableMax(),restaurant.getIdMax(),restaurant.getDescription(),restaurant.getAddress(),restaurant.getImage(),restaurant.getPhone());
            }
        });
    }
    private void showDialogRestaurant(ViewHolder viewHolder,String userName_,int idTableMax_,int idMenuMax_,String description_,String address_,String image_,String phone_){
        // Tạo một Dialog
        Dialog dialog = new Dialog(viewHolder.itemView.getContext());
        // Yêu cầu không hiển thị tiêu đề cho Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt layout cho Dialog từ file XML
        dialog.setContentView(R.layout.dialog_center_show_restaurant);

        // Tìm các thành phần trong layout của Dialog
        CircleImageView logo = dialog.findViewById(R.id.logo);
        TextView idMax = dialog.findViewById(R.id.idMax);
        TextView idTableMax = dialog.findViewById(R.id.idTableMax);
        TextView address = dialog.findViewById(R.id.address);
        TextView phone = dialog.findViewById(R.id.phone);
        TextView description = dialog.findViewById(R.id.description);
        TextView username = dialog.findViewById(R.id.username);
        Button btnDirection = dialog.findViewById(R.id.btnDirection);
                Button btnCall = dialog.findViewById(R.id.btnCall);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        //set data
        if (image_!=null&&image_.length()>0){
            Picasso.get().load(image_).into(logo);
        }else {
            logo.setImageResource(R.drawable.account);
        }
        //set data
        // chuyển idMax_ và idTableMax_ sang String
        idMax.setText(String.valueOf(idMenuMax_));
        idTableMax.setText(String.valueOf(idTableMax_));
        address.setText(address_);
        phone.setText(phone_);
        description.setText(description_);
        username.setText(userName_);
        //button
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Direction", Toast.LENGTH_SHORT).show();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mở cuộc gọi điện
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(android.net.Uri.parse("tel:"+phone_));
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        // Hiển thị Dialog
        dialog.show();
        // Đặt kích thước cho Dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // Đặt màu nền cho Dialog là trong suốt
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Đặt hiệu ứng cho Dialog
        dialog.getWindow().getAttributes().windowAnimations = R.anim.slide_in_notification;
        // Đặt vị trí cho Dialog ở phía dưới
        dialog.getWindow().setGravity(Gravity.TOP);
    }
    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}