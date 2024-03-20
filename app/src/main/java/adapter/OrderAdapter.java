//package adapter;
//
////import static com.example.restaurantmanager.MainActivity.oderAdapter;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.example.restaurantmanager.MainActivity;
//import com.example.restaurantmanager.R;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//import model.MenuRestaurant;
//
//public class OrderAdapter extends ArrayAdapter<MenuRestaurant> {
//    //màn hình sử dụng adapter
//    Activity context;
//    //layout cho từng dòng muốn hiển thị
//    int resource;
//    //danh sách nguồn dữ liệu muốn hiển thị lên giao diện
//    List<MenuRestaurant> objects;
//    public OrderAdapter(@NonNull Activity context, int resource, @NonNull List<MenuRestaurant> objects) {
//        super(context, resource, objects);
//        this.context = context;
//        this.resource = resource;
//        this.objects = objects;
//    }
//
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = this.context.getLayoutInflater();
//        View row = inflater.inflate(this.resource, null);
//        TextView textViewId = row.findViewById(R.id.textViewId);
//        TextView textViewName = row.findViewById(R.id.textViewName);
//        TextView textViewDescription = row.findViewById(R.id.textViewDescription);
//        TextView textViewPrice = row.findViewById(R.id.textViewPrice);
//        ImageView imageViewFood = row.findViewById(R.id.imageViewFood);
//        ImageButton imageButtonAdd = row.findViewById(R.id.imageButtonAdd);
//
//        MenuRestaurant menuRestaurant = this.objects.get(position);
//
//        textViewId.setText(menuRestaurant.getId() + "");
//        textViewName.setText(menuRestaurant.getName());
//        textViewDescription.setText(menuRestaurant.getDescription());
//        textViewPrice.setText(menuRestaurant.getPrice()+"");
//        //lấy hình ảnh từ database
//        Picasso.get()
//                .load(menuRestaurant.getImage())
//                .into(imageViewFood);
////        imageViewFood.setImageResource(R.drawable.rice);
////        imageViewFood.setImageBitmap(BitmapFactory.decodeFile(menuRestaurant.getImage()));
//        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.dataOrder.add(menuRestaurant);
////                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Thêm thành công món: "+ menuRestaurant.getName()+"", Toast.LENGTH_SHORT).show();
////                OderActivity2.listViewOrder.setAdapter(oderAdapter);
////                for (int i = 0; i < MainActivity.dataOrder.size(); i++) {
////                    Toast.makeText(context, MainActivity.dataOrder.get(i).getName()+MainActivity.dataOrder.get(i).getPrice(), Toast.LENGTH_SHORT).show();
////                }
//            }
//        });
//        return row;
//    }
//
//}
