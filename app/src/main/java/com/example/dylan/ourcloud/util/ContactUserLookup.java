package com.example.dylan.ourcloud.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dylan.ourcloud.live_zone.ContactDBHelper;

/**
 * Created by dylan on 10/1/15.
 */
public class ContactUserLookup {

    public static String nameLookup( Context context, String id )
    {
        SQLiteDatabase readable = new ContactDBHelper( context ).getReadableDatabase();
        Cursor nameRes = readable.rawQuery("SELECT user_name FROM contacts WHERE user_id = ?", new String[]{id});
        nameRes.moveToFirst();

        return nameRes.getString( nameRes.getColumnIndex("user_name") );
    }

    public static String photoLookup( Context context, String id )
    {
        SQLiteDatabase readable = new ContactDBHelper( context ).getReadableDatabase();
        Cursor imageRes = readable.rawQuery( "SELECT user_image FROM contacts WHERE user_id = ?", new String[] {id} );
        imageRes.moveToFirst();

        return imageRes.getString( imageRes.getColumnIndex( "user_image" ) );
    }

}
