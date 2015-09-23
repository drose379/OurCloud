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

    public static String user_id_col = "id";
    public static String nameCol = "name";
    public static String profile_image_col = "profile_image";
    public static String wifi_id_col = "wifi_id";
    public static String networks_in_range_col = "networks_in_range";
    public static String zone_name_col = "zone_name";
    public static String zone_id_col = "zone_id";

    public LocalUserDBHelper(Context context) {
        super(context,dbName,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(
                        "CREATE TABLE user (id TEXT,gcm_id TEXT, name TEXT, profile_image TEXT, wifi_id TEXT, networks_in_range TEXT, zone_name TEXT, zone_id TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {}
    @Override
    public void onDowngrade(SQLiteDatabase db,int oldVersion,int newVersion) {}

}
