package com.example.dylan.ourcloud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dylan on 9/18/15.
 */
public class LocalUserDBHelper extends SQLiteOpenHelper {

    public static String dbName = "local_user";
    public static String tableName = "user";

    public LocalUserDBHelper(Context context) {
        super(context,dbName,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(/*Create table schema*/);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {}
    @Override
    public void onDowngrade(SQLiteDatabase db,int oldVersion,int newVersion) {}

}
