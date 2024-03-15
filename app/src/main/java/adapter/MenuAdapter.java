package adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantmanager.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.MenuRestaurant;

public class MenuAdapter extends ArrayAdapter<MenuRestaurant> {
    //màn hình sử dụng adapter
    Activity context;
    //layout cho từng dòng muốn hiển thị
    int resource;
    //danh sách nguồn dữ liệu muốn hiển thị lên giao diện
    List<MenuRestaurant> objects;
    public MenuAdapter(@NonNull Activity context, int resource, @NonNull List<MenuRestaurant> objects) {
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
        TextView textViewName = row.findViewById(R.id.textViewName);
        TextView textViewDescription = row.findViewById(R.id.textViewDescription);
        TextView textViewPrice = row.findViewById(R.id.textViewPrice);
        ImageView imageViewFood = row.findViewById(R.id.imageViewFood);
        ImageButton imageButtonAdd = row.findViewById(R.id.imageButtonAdd);


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
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //xử lý sự kiện khi click vào nút thêm
            }
        });
        return row;
    }

}
