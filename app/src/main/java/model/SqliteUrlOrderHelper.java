package model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SqliteUrlOrderHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
//    private static final String TABLE_NAME_ACCOUNT = "account";
    private static final String TABLE_NAME_ORDER = "order_menu";
    private static final String COLUMN_URL_ORDER = "urlOrder_menu"; // Primary key for order table

    private final String  DB_PATH_SUFFIX = "/databases/";

    private static final String DATABASE_NAME="menurestaurant.db";
    public SqliteUrlOrderHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Check if tables exist before creating them
       if (!hasTable(db, TABLE_NAME_ORDER)) {
        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_NAME_ORDER + " ( "
                + COLUMN_URL_ORDER + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_ORDER_TABLE);
    }
    }


    private boolean hasTable(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'", null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade tasks here
    }

    public void addUrl(String url) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_URL_ORDER, url); // Add url to order table
        long result = database.insert(TABLE_NAME_ORDER, null, contentValues);

        if (result == -1) {
            Log.d("TAG", "Fail to insert order URL!");
        } else {
            Log.d("TAG", "Inserted order URL successfully");
        }
    }



    // Get all order URLs
    public String getAllUrls() {
        String urls ="";
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {COLUMN_URL_ORDER}; // Specify only the URL_ORDER column
        Cursor cursor = db.query(TABLE_NAME_ORDER, projection, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            // Handle the case where no accounts are found
            return null; // Or take other appropriate action
        }

        urls=cursor.getString(0);
        cursor.close();
        db.close();
        return urls;
    }

    // Delete an order URL
    public void deleteUrl(String url) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_ORDER, COLUMN_URL_ORDER + " = ?", new String[]{url});
        db.close();
    }

}

