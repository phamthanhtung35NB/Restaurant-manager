package adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import com.example.restaurantmanager.Client.MenuClientActivity;
import com.example.restaurantmanager.Client.MenuClientFragment;
import com.example.restaurantmanager.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.MenuRestaurant;

public class MenuBillAdapter extends ArrayAdapter<MenuRestaurant> {
    //màn hình sử dụng adapter
    Activity context;
    //layout cho từng dòng muốn hiển thị
    int resource;
    //danh sách nguồn dữ liệu muốn hiển thị lên giao diện
    List<MenuRestaurant> objects;
    public MenuBillAdapter(@NonNull Activity context, int resource, @NonNull List<MenuRestaurant> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        System.out.println("1");
        TextView txtSl = row.findViewById(R.id.txtSl);
        System.out.println("2");
        TextView textViewName = row.findViewById(R.id.textViewNameOrder);
        System.out.println("3");
        TextView textViewPrice = row.findViewById(R.id.textViewPriceOrder);
System.out.println("4");
        MenuRestaurant menuRestaurant = this.objects.get(position);
System.out.println("5");
        txtSl.setText(menuRestaurant.getId() + "");
        System.out.println("6");
        textViewName.setText(menuRestaurant.getName());
System.out.println("7");
        textViewPrice.setText(menuRestaurant.getPrice()+"");
        System.out.println("8");
        return row;
    }


}