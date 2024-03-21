package model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import android.util.Log;
import android.widget.Toast;

public class SqliteAccountHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_ACCOUNT = "account";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADDRESS = "address";
    private final String  DB_PATH_SUFFIX = "/databases/";

    private static final String DATABASE_NAME="menurestaurant.db";
    public SqliteAccountHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + TABLE_NAME_ACCOUNT + "("
                + COLUMN_TYPE + " TEXT PRIMARY KEY,"
                + COLUMN_USERNAME + " TEXT NOT NULL,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_PHONE + " TEXT NOT NULL,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_ADDRESS + " TEXT" + ")";
        db.execSQL(CREATE_ACCOUNT_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade tasks here
    }
    public void addAccountLogin(Account account) {
//        SQLiteDatabase database= null;
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TYPE, account.getType());
        contentValues.put(COLUMN_USERNAME, account.getUsername());
        contentValues.put(COLUMN_PASSWORD, account.getPassword());
        contentValues.put(COLUMN_PHONE, account.getPhone());
        contentValues.put(COLUMN_EMAIL, account.getEmail());
        contentValues.put(COLUMN_ADDRESS, account.getAddress());
        long result = database.insert(TABLE_NAME_ACCOUNT, null, contentValues);
        if (result == -1) {
            Log.d("TAG", "Fail to insert record!");
        } else {
            Log.d("TAG", "Inserted record successfully");
        }

    }

//    public void setAccountLogin(String type,Account account) {
//        SQLiteDatabase database = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_USERNAME, account.getUsername());
//        values.put(COLUMN_PASSWORD, account.getPassword());
//        values.put(COLUMN_PHONE, account.getPhone());
//        values.put(COLUMN_EMAIL, account.getEmail());
//        values.put(COLUMN_ADDRESS, account.getAddress());
//        // Cập nhật dữ liệu
//        database.update(TABLE_NAME_ACCOUNT, values, COLUMN_TYPE + " = ?", new String[]{type});
//        database.close();
//    }
//    public Account getData(String username) {
//        SQLiteDatabase db = getReadableDatabase();
//
//        String query = "SELECT * FROM " + TABLE_NAME_ACCOUNT + " WHERE username = ?";
//
//        Cursor cursor = db.rawQuery(query, new String[]{username});
//
//        Account account = null;
//
//        if (cursor.moveToFirst()) { // Use moveToFirst instead of moveToNext
//            account = new Account();
//            int idColumnIndex = cursor.getColumnIndex(COLUMN_TYPE);
//            if (idColumnIndex >= 0) {
//                account.setType(cursor.getString(idColumnIndex));
//            }
//            int usernameColumnIndex = cursor.getColumnIndex(COLUMN_USERNAME);
//            if (usernameColumnIndex >= 0) {
//                account.setUsername(cursor.getString(usernameColumnIndex));
//            }
//            int passwordColumnIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
//            if (passwordColumnIndex >= 0) {
//                account.setPassword(cursor.getString(passwordColumnIndex));
//            }
//        }
//        cursor.close();
//        db.close();
//
//        return account;
//    }


//    public boolean isTableEmpty(String tableName) {
//        SQLiteDatabase db = getReadableDatabase();
//
//        long count = db.query(tableName, null, null, null, null, null, null).getCount();
//
//        db.close();
//
//        return count == 0;
//    }





    // Truy vấn dữ liệu từ cơ sở dữ liệu
//    public Cursor getData(String type) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_NAME_ACCOUNT + " WHERE " + COLUMN_TYPE + " = '" + type + "'";
//        return db.rawQuery(query, null);
//    }
    // Get single account
    public Account getAccount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_ACCOUNT, new String[]{COLUMN_TYPE, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_ADDRESS},
                null, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            // Handle the case where no accounts are found
            return null; // Or take other appropriate action
        }

        Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return account;
    }
//
//    public Account getAccount(String type) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_NAME_ACCOUNT, new String[]{COLUMN_TYPE,COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_PHONE, COLUMN_EMAIL, COLUMN_ADDRESS}, COLUMN_TYPE + "=?",
//                new String[]{String.valueOf(type)}, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
//        // return account
//        return account;
//    }

    // Update account
    public int updateAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, account.getUsername());
        values.put(COLUMN_PASSWORD, account.getPassword());
        values.put(COLUMN_PHONE, account.getPhone());
        values.put(COLUMN_EMAIL, account.getEmail());
        values.put(COLUMN_ADDRESS, account.getAddress());

        // updating row
        return db.update(TABLE_NAME_ACCOUNT, values, COLUMN_TYPE + " = ?",
                new String[]{String.valueOf(account.getType())});
    }

    // Delete account
    public void deleteAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_ACCOUNT, COLUMN_TYPE + " = ?",
                new String[]{String.valueOf(account.getType())});
        db.close();
    }
}
