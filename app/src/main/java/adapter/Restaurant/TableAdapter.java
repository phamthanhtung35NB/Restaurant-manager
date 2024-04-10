package adapter.Restaurant;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import com.example.restaurantmanager.MenuRestaurant.Table.ShowTableActivity;
import com.example.restaurantmanager.MenuRestaurant.Order.OderActivity;
import com.example.restaurantmanager.MenuRestaurant.Table.ShowTableRestaurantFragment;
import com.example.restaurantmanager.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Table;

public class TableAdapter extends ArrayAdapter<Table> {
    Activity context;
    int resource;
    List<Table> objects;

    /**
     * Constructor của TableAdapter
     * @param context Activity hiện tại
     * @param resource Layout cho từng item
     * @param objects Danh sách bàn
     */
    public TableAdapter(@NonNull Activity context, int resource, @NonNull List<Table> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    /**
     *  Được gọi khi ListView cần một view từ Adapter. getView() sẽ cung cấp một View cho mỗi item trong danh sách.
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        ImageView imageViewPhoto = row.findViewById(R.id.imageViewPhoto);
        TextView textViewNameTable = row.findViewById(R.id.textViewNameTable);
        TextView textViewStatus = row.findViewById(R.id.textViewStatus);
        TextView textViewDescribe = row.findViewById(R.id.textViewDescribe);
        LinearLayout linearLayoutButton = row.findViewById(R.id.linearLayoutButton);

        Table table = this.objects.get(position);
        textViewNameTable.setText(table.getName());
        textViewStatus.setText(table.getStateEmpty());
        textViewDescribe.setText(table.getDescribe()+"");
        String imageShow = table.getImage();
        //lấy hình ảnh từ database
        if (imageShow.length()>5){
            Picasso.get()
                    .load(imageShow)
                    .into(imageViewPhoto);
        }else {
            imageViewPhoto.setImageResource(R.drawable.chair);
        }
        /**
         * Xử lý sự kiện khi click vào 1 bàn (item trong GridView)
         */
        linearLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,  table.getName()+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, OderActivity.class);
                String qr = ShowTableRestaurantFragment.accountId+"/"+table.getId()+"/order";
                intent.putExtra("url", qr);
                context.startActivity(intent);
//                //add vào sqlite
//                MainActivity.dataOrder.add(menuRestaurant);
////                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Thêm thành công món: "+ menuRestaurant.getName()+"", Toast.LENGTH_SHORT).show();
////                OderActivity2.listViewOrder.setAdapter(oderAdapter);
////                for (int i = 0; i < MainActivity.dataOrder.size(); i++) {
////                    Toast.makeText(context, MainActivity.dataOrder.get(i).getName()+MainActivity.dataOrder.get(i).getPrice(), Toast.LENGTH_SHORT).show();
////                }
            }
        });
        return row;

    }

}
