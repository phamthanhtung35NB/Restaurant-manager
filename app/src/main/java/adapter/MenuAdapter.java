package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantmanager.R;

import java.util.List;

import model.Menu;

public class MenuAdapter extends ArrayAdapter<Menu> {
    //màn hình sử dụng adapter
    Activity context;
    //layout cho từng dòng muốn hiển thị
    int resource;
    //danh sách nguồn dữ liệu muốn hiển thị lên giao diện
    List<Menu> objects;
    public MenuAdapter(@NonNull Activity context, int resource, @NonNull List<Menu> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

//    public MenuAdapter(@NonNull Context context, int resource, @NonNull List<Menu> objects) {
//        super(context, resource, objects);
//    }
//    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView txtName = row.findViewById(R.id.txtName);

        //        //lấy dữ liệu từ list
//        Menu menu = getItem(position);
//        //tạo view
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView = inflater.inflate(R.layout.item_menu, null);
//        //ánh xạ view
//        ImageView imgMenu = convertView.findViewById(R.id.imgMenu);
//        TextView txtName = convertView.findViewById(R.id.txtName);
//        TextView txtPrice = convertView.findViewById(R.id.txtPrice);
//        //gán dữ liệu
//        Picasso.get().load(menu.getImage()).into(imgMenu);
//        txtName.setText(menu.getName());
//        txtPrice.setText(menu.getPrice() + " VND");
//        return convertView;
    }

}
