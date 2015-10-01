package com.example.dylan.ourcloud.live_zone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dylan on 10/1/15.
 */
public class ContactDBHelper extends SQLiteOpenHelper {

    public static String dbName = "contact_dictionary";
    public static String tableName = "contacts";
    public static String idCol = "user_id";
    public static String nameCol = "user_name";
    public static String imageCol = "user_image";

    public ContactDBHelper( Context context )
    {
        super( context, dbName, null, 1 );
    }

    @Override
    public void onCreate( SQLiteDatabase database )
    {
        String stmt = "CREATE TABLE contacts (user_id TEXT PRIMARY KEY, user_name TEXT, user_image TEXT)";
        database.execSQL(stmt);
    }

    @Override
    public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion )
    {

    }

    @Override
    public void onDowngrade( SQLiteDatabase database, int oldVersion, int newVersion )
    {

    }

}
