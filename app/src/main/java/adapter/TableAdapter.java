package adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantmanager.DinnerTableActivity;
import com.example.restaurantmanager.HandleQrActivity;
import com.example.restaurantmanager.LoginActivity;
import com.example.restaurantmanager.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.MenuRestaurant;
import model.Table;

public class TableAdapter extends ArrayAdapter<Table> {
    Activity context;
    int resource;
    List<Table> objects;
    public TableAdapter(@NonNull Activity context, int resource, @NonNull List<Table> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
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
        //lấy hình ảnh từ database
        Picasso.get()
                .load(table.getImage())
                .into(imageViewPhoto);
//        imageViewFood.setImageResource(R.drawable.rice);
//        imageViewFood.setImageBitmap(BitmapFactory.decodeFile(menuRestaurant.getImage()));
        linearLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,  table.getName()+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, HandleQrActivity.class);
                String qr = DinnerTableActivity.accountId+"_"+table.getId()+"";
                intent.putExtra("uid", qr);
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
