package adapter;

//import static com.example.restaurantmanager.MenuRestaurant.Menu.MainActivity.oderAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    /**
     * Hàm này sẽ được gọi khi những dòng dữ liệu muốn hiển thị lên giao diện
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
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
        String imageShow = menuRestaurant.getImage();
        //lấy hình ảnh từ database
        if (imageShow.length()>5){
            Picasso.get()
                    .load(imageShow)
                    .into(imageViewFood);
        }else {
            imageViewFood.setImageResource(R.drawable.food_load);
        }


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
