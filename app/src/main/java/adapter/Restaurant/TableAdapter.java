package adapter.Restaurant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.MenuRestaurant;
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
                String qr = ShowTableRestaurantFragment.accountId+"/"+table.getId()+"/order";
                if (table.getStateEmpty().equals("Trống")){
                    try {
                        showQR(qr, table.getId(),ShowTableRestaurantFragment.accountId,""+table.getId());
                    } catch (WriterException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Toast.makeText(context,  table.getName()+"", Toast.LENGTH_SHORT).show();
                    //add id bàn vào SharedPreferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences("dataTable", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("idTable", String.valueOf(table.getId()));
                    editor.commit();
                    Intent intent = new Intent(context, OderActivity.class);

                    intent.putExtra("url", qr);
                    context.startActivity(intent);
                }

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
    void showQR(String url,int id,String accountId,String idTable) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        final int width = 1000;
        final int height = 1000;
        BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_center_show_qr);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == Gravity.BOTTOM) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        TextView username = dialog.findViewById(R.id.username);
        ImageView imageViewQRCode = dialog.findViewById(R.id.imageViewQRCode);
        //set image
        imageViewQRCode.setImageBitmap(bitmap);
//       set username table
        username.setText("Bàn Số: "+id);

        dialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(accountId).child(idTable).child("stateEmpty");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String stateEmpty = dataSnapshot.getValue(String.class);
                if (!stateEmpty.equals("Trống")){
                    //đóng dialog show qr
                    dialog.dismiss();
                    //show dialog thông báo
                    showDialog(id);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }
    public void showDialog(int id){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_notification_dialog);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == Gravity.BOTTOM) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        TextView tvNotification = dialog.findViewById(R.id.tvNotification);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        tvNotification.setText("Bàn "+id+" đã được quét QR thành công!");
        btnClose.setOnClickListener(v -> {
            System.out.println("Đóng dialog");
            dialog.dismiss();
        });

        dialog.show();
    }
}
