package com.example.dylan.ourcloud.live_zone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dylan on 9/15/15.
 */
public class MessagesDBHelper extends SQLiteOpenHelper {

    private static String databaseName = "chat_message";
    public String messageTableName = "messages";

    public String colSenderId = "other_user_id";
    public String colMessage = "message";

    public MessagesDBHelper( Context context ) {
        super( context, databaseName, null, 1  );
    }

    @Override
    public void onCreate( SQLiteDatabase database ) {
        database.execSQL(
                "CREATE TABLE messages (other_user_id TEXT, origin INTEGER, message TEXT);"
        );
    }

    @Override
    public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion ) {}

    @Override
    public void onDowngrade( SQLiteDatabase database, int oldVersion, int newVersion ) {}


}
