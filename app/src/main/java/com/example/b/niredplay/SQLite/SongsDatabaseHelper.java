package com.example.b.niredplay.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SongsDatabaseHelper extends SQLiteOpenHelper {

    public static  final String CREATE_SONG = "create table Song (id integer primary key autoincrement,artist text,name text,songUri text)";

    private Context mContext;

    public SongsDatabaseHelper( Context context,  String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SONG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static boolean haveData(SQLiteDatabase db,String tableName,String item,String data)
    {
        Cursor cursor;
        cursor = db.query(tableName,null,null,
                null,null,null,null);
        if(cursor.moveToFirst())
        {
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if(name.equals(data))
                {
                    return true;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }
}
