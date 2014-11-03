package org.feup.meoarenacustomer.app;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;
import android.content.ContentValues;

import java.util.ArrayList;

public class Storage extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "meoarena.db";
    Storage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE dictionary "
                   + "(key TEXT, value TEXT);";
        db.execSQL(sql);

        String vouchers = "CREATE TABLE vouchers "
                        + "(id TEXT, customerID TEXT, product TEXT, discount TEXT, status TEXT);";
        db.execSQL(vouchers);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String sql = "CREATE TABLE dictionary "
                + "(key TEXT, value TEXT);";
        db.execSQL(sql);

        String vouchers = "CREATE TABLE vouchers "
                + "(id TEXT, customerID TEXT, product TEXT, discount TEXT, status TEXT);";
        db.execSQL(vouchers);
    }

    public boolean saveVoucher(String id, String customerID, String product, String discount, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("customerID", customerID);
        contentValues.put("product", product);
        contentValues.put("discount", discount);
        contentValues.put("status", status);
        db.insert("vouchers", null, contentValues);
        db.close();
        return true;
    }

    public String[][] getVouchers(String customerID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id,product,discount,status FROM vouchers WHERE customerID='" +  customerID + "'" +
                "AND status='unused';", null);
        int count = cursor.getCount();
        if(cursor!=null && count!=0){
            cursor.moveToFirst();
            String[][] vouchers = new String[cursor.getCount()][];
            for (int i=0; i < cursor.getCount(); i++) {
                String[] voucher = new String[3];
                voucher[0] = cursor.getString(1); //voucher id
                voucher[1] = cursor.getString(3); //product
                voucher[2] = cursor.getString(4); //discount
                cursor.moveToNext();
            }
            return vouchers;
        }
        cursor.close();
        db.close();
        return null;

    }

    public boolean put(String key, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", key);
        contentValues.put("value", value);
        db.insert("dictionary", null, contentValues);
        db.close();
        return true;
    }

    public boolean remove(String key){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql;
        sql = "DELETE FROM dictionary "
            + "WHERE key = '" +  key + "';";
        db.execSQL(sql);
        db.close();
        return true;
    }

    public String get(String key){
        String value = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT key,value FROM dictionary WHERE key='" +  key + "';", null);
        int count = cursor.getCount();
        if(cursor!=null && count!=0){
            cursor.moveToFirst();
            value = cursor.getString(1);
        }
        db.close();
        return value;

    }

}