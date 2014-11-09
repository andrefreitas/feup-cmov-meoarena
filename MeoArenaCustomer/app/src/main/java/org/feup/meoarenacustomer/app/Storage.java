package org.feup.meoarenacustomer.app;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;
import android.content.ContentValues;
import android.widget.Toast;

import java.util.ArrayList;

public class Storage extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
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
                        + "(voucherID TEXT, customerID TEXT, product TEXT, discount TEXT, status TEXT);";
        db.execSQL(vouchers);

        String tickets = "CREATE TABLE tickets "
                        + "(ticketID TEXT, customerID TEXT, showID TEXT, name TEXT, seat TEXT, status TEXT, date TEXT);";

        db.execSQL(tickets);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public boolean saveTicket(String id, String customerID, String showID, String seat, String status, String date, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ticketID", id);
        contentValues.put("customerID", customerID);
        contentValues.put("showID", showID);
        contentValues.put("name", name);
        contentValues.put("seat", seat);
        contentValues.put("status", status);
        contentValues.put("date", date);
        db.insert("tickets", null, contentValues);
        db.close();
        return true;
    }

    public String[][] getTickets(String customerID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ticketID, customerID, showID, name, seat, date, status FROM tickets;", null);
        int count = cursor.getCount();
        String[][] tickets = null;
        if(cursor!=null && count!=0){
            cursor.moveToFirst();
            tickets = new String[cursor.getCount()][];
            for (int i=0; i < cursor.getCount(); i++) {
                String[] ticket = new String[7];
                ticket[0] = cursor.getString(3); //name
                ticket[1] = cursor.getString(4); //seat
                ticket[2] = cursor.getString(5); //date
                ticket[3] = cursor.getString(1); //customerID
                ticket[4] = cursor.getString(0); //ticketID
                ticket[5] = cursor.getString(6); //status
                ticket[6] = cursor.getString(2); //showID
                tickets[i] = ticket;
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return tickets;
    }

    public boolean updateTicket(String ticketID, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data=new ContentValues();
        data.put("status",status);
        db.update("tickets", data, "ticketID='" + ticketID + "'", null);
        db.close();
        return true;
    }

    public boolean saveVoucher(String id, String customerID, String product, String discount, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("voucherID", id);
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
        Cursor cursor = db.rawQuery("SELECT voucherID, customerID, product,discount,status FROM vouchers WHERE customerID='" +  customerID + "'" +
                "AND status='unused';", null);
        int count = cursor.getCount();
        String[][] vouchers = null;
        if(cursor!=null && count!=0){
            cursor.moveToFirst();
            vouchers = new String[cursor.getCount()][];
            for (int i=0; i < cursor.getCount(); i++) {
                String[] voucher = new String[3];
                voucher[0] = cursor.getString(0); //voucher id
                voucher[1] = cursor.getString(2); //product
                voucher[2] = cursor.getString(3); //discount
                vouchers[i] = voucher;
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return vouchers;

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