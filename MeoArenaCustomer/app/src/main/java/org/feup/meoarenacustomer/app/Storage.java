package org.feup.meoarenacustomer.app;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursor;
import android.content.ContentValues;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

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