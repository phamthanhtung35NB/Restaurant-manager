package adapter.Client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import com.example.restaurantmanager.Client.MenuClientActivity;
import com.example.restaurantmanager.Client.MenuClientFragment;
import com.example.restaurantmanager.MenuRestaurant.Menu.EditFoodActivity;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.MenuRestaurant;

public class MenuClientAdapter extends ArrayAdapter<MenuRestaurant> {
    //màn hình sử dụng adapter
    Activity context;
    //layout cho từng dòng muốn hiển thị
    int resource;
    //danh sách nguồn dữ liệu muốn hiển thị lên giao diện
    List<MenuRestaurant> objects;
    public MenuClientAdapter(@NonNull Activity context, int resource, @NonNull List<MenuRestaurant> objects) {
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
        Button imageButtonAddOrder = row.findViewById(R.id.imageButtonAddOrder);

        MenuRestaurant menuRestaurant = this.objects.get(position);

        textViewId.setText(menuRestaurant.getId() + "");
        textViewName.setText(menuRestaurant.getName());
        textViewDescription.setText(menuRestaurant.getDescription());
        textViewPrice.setText(menuRestaurant.getPrice()+"");
        //lấy hình ảnh từ database
        String imageShow = menuRestaurant.getImage();
        //lấy hình ảnh từ database
        if (imageShow.length()>5){
            Picasso.get()
                    .load(imageShow)
                    .into(imageViewFood);
        }else {
            imageViewFood.setImageResource(R.drawable.food_load);
        }
//        imageViewFood.setImageResource(R.drawable.rice);
//        imageViewFood.setImageBitmap(BitmapFactory.decodeFile(menuRestaurant.getImage()));
        imageButtonAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                //them vao order(menuRestaurant);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef;
                SharedPreferences sharedPreferences = context.getSharedPreferences("dataLogin", context.MODE_PRIVATE);
                String type = sharedPreferences.getString("type", "");
                if (type.equals("restaurant")){
                    String uid = sharedPreferences.getString("uid", "");
                    //lấy id bàn từ SharedPreferences
                    SharedPreferences sharedPreferencesTable = context.getSharedPreferences("dataTable", context.MODE_PRIVATE);
                    String idTable = sharedPreferencesTable.getString("idTable", "");
                    String dp=uid+"/"+idTable+"/order/"+menuRestaurant.getId()+"";
                    myRef = database.getReference(dp);
                }else {
                    String dp=MenuClientFragment.URL+"/"+menuRestaurant.getId()+"";
                    System.out.println("dp: "+dp);
                    myRef = database.getReference(dp);
                }




                myRef.setValue(menuRestaurant, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference ref) {
                        if (error == null) {
                            Toast.makeText(context, "onComplete: success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "onComplete: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Toast.makeText(context, "đã thêm", Toast.LENGTH_SHORT).show();

            }
        });
        return row;
    }


}