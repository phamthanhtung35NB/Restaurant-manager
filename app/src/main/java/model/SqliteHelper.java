//package model;
//import static android.content.Context.MODE_PRIVATE;
//import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.Serializable;
//import java.util.ArrayList;
//
////public class MenuRestaurant implements Serializable {
////private String id;
////private String name;
//////miêu tả món ăn
////private String description;
//////giá tiền
////private double price;
//////hình ảnh
////private String image;
//public class SqliteHelper extends AppCompatActivity{
//
//
//
//    private final String  DB_PATH_SUFFIX = "/databases/";
//    SQLiteDatabase database=null;
//    private final String DATABASE_NAME="menurestaurant.db";
//    //Khai báo ListView
//    ArrayList<String> mylist;
//    ArrayAdapter<String> myadapter;
//
//
//
//    public boolean checkIfAccountExists() {
//        SQLiteDatabase database = getReadableDatabase();
//        Cursor cursor = database.rawQuery("SELECT * FROM account", null);
//        boolean exists = cursor.moveToFirst();
//        cursor.close();
//        return exists;
//    }
//
//    public void addOrUpdateAccount(String type, String username, String password, String phone, String email, String address) {
//        if (checkIfAccountExists()) {
//            // Do something if account exists, you can return Account object or perform other actions
//            // In this case, let's assume we are returning Account object
//            Account existingAccount = getAccountByUsername(username);
//            // You can handle the existing account here, for example:
//            // updateAccount(existingAccount, type, password, phone, email, address);
//            // or display a message that account already exists
//        } else {
//            addData(type, username, password, phone, email, address);
//        }
//    }
//
//    public Account getAccountByUsername(String username) {
//        SQLiteDatabase database = getReadableDatabase();
//        Cursor cursor = database.rawQuery("SELECT * FROM account WHERE username = ?", new String[]{username});
//        Account account = null;
//        if (cursor.moveToFirst()) {
//            String password = cursor.getString(cursor.getColumnIndex("password"));
//            String phone = cursor.getString(cursor.getColumnIndex("phone"));
//            String email = cursor.getString(cursor.getColumnIndex("email"));
//            String address = cursor.getString(cursor.getColumnIndex("address"));
//            account = new Account(username, password, phone, email, address);
//        }
//        cursor.close();
//        return account;
//    }
//
//
//    //add menu
//    void addData(String id, String name, String description, double price, String image) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("id", id);
//        contentValues.put("name", name);
//        contentValues.put("description", description);
//        contentValues.put("price", price);
//        contentValues.put("image", image);
//
//        long result = database.insert("menurestaurant", null, contentValues);
//        if (result == -1) {
//            Toast.makeText(this, "Fail to insert record!", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Record inserted successfully!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Hàm xóa dữ liệu khỏi bảng
//    void deleteData(String id) {
//        int result = database.delete("menurestaurant", "id=?", new String[]{id});
//        if (result > 0) {
//            Toast.makeText(this, "Record deleted successfully!", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Fail to delete record!", Toast.LENGTH_SHORT).show();
//        }
//    }
//    void deleteAllData() {
//        database.delete("menurestaurant", null, null);
//        Toast.makeText(this, "All data deleted successfully!", Toast.LENGTH_SHORT).show();
//    }
////    add món ăn vào odre
//    void insertData(String id, String name, String description, double price, String image){
//        database = openOrCreateDatabase("menurestaurant.db", MODE_PRIVATE, null);
//        String sql = "INSERT INTO menurestaurant VALUES('"+id+"','"+name+"','"+description+"','"+price+"','"+image+"')";
//        database.execSQL(sql);
//        ContentValues myvalue = new ContentValues();
//        myvalue.put("id",id);
//        myvalue.put("name",name);
//        myvalue.put("description",description); myvalue.put("price",price);
//        myvalue.put("image",image);
//        String msg = "";
//        if (database.insert("tbllop",null,myvalue) == -1)
//        {
//            msg = "Fail to Insert Record!";
//        }
//        else {
//            msg ="Insert record Sucessfully";
//        }
//    }
//    void init() {
//        processCopy();
////Mở CSDL lên để dùng
//        database = openOrCreateDatabase("menurestaurant.db", MODE_PRIVATE, null);
//// Tạo ListView
//// Truy vấn CSDL và cập nhật hiển thị lên Listview
//        Cursor c = database.query("menurestaurant", null, null, null, null, null, null);
//
//        c.moveToFirst();
//        String data = "";
//        while (c.isAfterLast() == false) {
//            data = c.getString(0) + "-" + c.getString(1) + "-" + c.getString(2)+"-"+c.getString(3)+"-"+c.getString(4);
//            System.out.println(data);
//            c.moveToNext();
//        }
//        c.close();
//    }
//    private void processCopy() {
////private app
//        File dbFile = getDatabasePath(DATABASE_NAME);
//        if (!dbFile.exists()) {
//            try {
//                CopyDataBaseFromAsset();
////                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
////                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private String getDatabasePath() {
//        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
//    }
//
//    public void CopyDataBaseFromAsset() {
//// TODO Auto-generated method stub
//        try {
//            InputStream myInput;
//            myInput = getAssets().open(DATABASE_NAME);
//// Path to the just created empty db
//            String outFileName = getDatabasePath();
//// if the path doesn't exist first, create it
//            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
//            if (!f.exists())
//                f.mkdir();
//// Open the empty db as the output stream
//            OutputStream myOutput = new FileOutputStream(outFileName);
//// transfer bytes from the inputfile to the outputfile
//// Truyền bytes dữ liệu từ input đến output
//            int size = myInput.available();
//            byte[] buffer = new byte[size];
//            myInput.read(buffer);
//            myOutput.write(buffer);
//// Close the streams
//            myOutput.flush();
//            myOutput.close();
//            myInput.close();
//        } catch (IOException e) {
//// TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }}