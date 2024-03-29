package adapter;

//import static com.example.restaurantmanager.MenuRestaurant.Menu.MainActivity.oderAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantmanager.MenuRestaurant.Menu.EditFoodActivity;
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
        TextView textViewName = row.findViewById(R.id.textViewNameOrder);
        TextView textViewDescription = row.findViewById(R.id.textViewDescriptionOrder);
        TextView textViewPrice = row.findViewById(R.id.textViewPriceOrder);
        ImageView imageViewFood = row.findViewById(R.id.imageViewFoodOrder);
        ImageButton imageButtonSet = row.findViewById(R.id.imageButtonDelFood);

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
        imageButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //add vào sqlite
//                MainActivity.dataOrder.add(menuRestaurant);
////                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Thêm thành công món: "+ menuRestaurant.getName()+"", Toast.LENGTH_SHORT).show();
////                OderActivity2.listViewOrder.setAdapter(oderAdapter);
////                for (int i = 0; i < MainActivity.dataOrder.size(); i++) {
////                    Toast.makeText(context, MainActivity.dataOrder.get(i).getName()+MainActivity.dataOrder.get(i).getPrice(), Toast.LENGTH_SHORT).show();
////                }
            //xửa món ăn
                Intent intent = new Intent(context, EditFoodActivity.class);
                intent.putExtra("id", menuRestaurant.getId());
                intent.putExtra("name", menuRestaurant.getName());
                intent.putExtra("description", menuRestaurant.getDescription());
                intent.putExtra("price", menuRestaurant.getPrice());
                intent.putExtra("image", menuRestaurant.getImage());
                context.startActivity(intent);


            }
        });
        return row;
    }

}
