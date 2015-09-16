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

    public String colSenderId = "sender_id";
    public String colSenderName = "sender_name";
    public String colMessage = "message";

    public MessagesDBHelper( Context context ) {
        super( context, databaseName, null, 1  );
    }

    @Override
    public void onCreate( SQLiteDatabase database ) {
        database.execSQL(
                "CREATE TABLE messages (sender_id TEXT, sender_name TEXT, message TEXT);"
        );
    }

    @Override
    public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion ) {}

    @Override
    public void onDowngrade( SQLiteDatabase database, int oldVersion, int newVersion ) {}


}
