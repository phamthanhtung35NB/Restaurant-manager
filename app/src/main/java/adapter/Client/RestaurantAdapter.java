package adapter.Client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanager.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Restaurant;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private List<Restaurant> restaurantList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView phone;
        public TextView username;
        public TextView description;
        public TextView idTableMax;
        public TextView idMax;
        public TextView address;
        public ImageView logo;
        public LinearLayout restaurant_item_click;

        public ViewHolder(View view) {
            super(view);
            phone = view.findViewById(R.id.phone);
            username = view.findViewById(R.id.username);
            description = view.findViewById(R.id.description);
            idTableMax = view.findViewById(R.id.idTableMax);
            idMax = view.findViewById(R.id.idMax);
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
        holder.phone.setText(restaurant.getPhone());
        holder.username.setText(restaurant.getUsername());
        holder.description.setText(restaurant.getDescription());
        holder.idTableMax.setText(String.valueOf(restaurant.getIdTableMax()));
        holder.idMax.setText(String.valueOf(restaurant.getIdMax()));
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}