package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantmanager.Client.OrderClientActivity;
import com.example.restaurantmanager.MenuRestaurant.Order.OderActivity;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.MenuRestaurant;

public class OrderClientAdapter extends ArrayAdapter<MenuRestaurant> {
    //màn hình sử dụng adapter
    Activity context;
    //layout cho từng dòng muốn hiển thị
    int resource;
    //danh sách nguồn dữ liệu muốn hiển thị lên giao diện
    List<MenuRestaurant> objects;
    public OrderClientAdapter(@NonNull Activity context, int resource, @NonNull List<MenuRestaurant> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView textViewId = row.findViewById(R.id.textViewId);
        TextView textViewName = row.findViewById(R.id.textViewNameOrder);
        TextView textViewDescription = row.findViewById(R.id.textViewDescriptionOrder);
        TextView textViewPrice = row.findViewById(R.id.textViewPriceOrder);
        ImageView imageViewFood = row.findViewById(R.id.imageViewFoodOrder);
        ImageButton imageButtonDelFood = row.findViewById(R.id.imageButtonDelFood);
        EditText editTextLoiNhan = row.findViewById(R.id.editTextLoiNhanOrder);

        MenuRestaurant menuRestaurant = this.objects.get(position);

        textViewId.setText(menuRestaurant.getId() + "");
        textViewName.setText(menuRestaurant.getName());
        textViewDescription.setText(menuRestaurant.getDescription());
        textViewPrice.setText(menuRestaurant.getPrice()+"");
        //lấy hình ảnh từ database
        Picasso.get()
                .load(menuRestaurant.getImage())
                .into(imageViewFood);
//        imageViewFood.setImageResource(R.drawable.rice);
//        imageViewFood.setImageBitmap(BitmapFactory.decodeFile(menuRestaurant.getImage()));
        imageButtonDelFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                //xóa trong database

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference ref = database.getReference(OrderClientActivity.URL).child(menuRestaurant.getId());

                // Xóa dữ liệu
                ref.removeValue();
//                làm mới lại listview
                OrderClientActivity.dataOrderClient.remove(position);
                OrderClientActivity.orderClientAdapter.notifyDataSetChanged();


            }
        });
        return row;
    }

}
