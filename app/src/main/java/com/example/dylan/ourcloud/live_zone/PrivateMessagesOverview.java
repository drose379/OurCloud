package com.example.dylan.ourcloud.live_zone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.UserListenerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dylan on 9/29/15.
 */
public class PrivateMessagesOverview extends UserListenerActivity {

    /**
     * ListView where each chat convorsation is a card
     * Query the SQLite DB (MessageDBHelper) and grab each unique "other_user_id"
        * There most likely will be duplicate "other_user_ids", only add it to List<String> of Ids' if its not already in list
     * Once list is made, select the 'other_user_name' corresponding to each id, and whether they are online or not (in LiveUsers.users)
     * If online, show green dot, if not, show RED
     * When card clicked, take to ChatConvo activity
     *
     * Uses userListener to tell if user in chat list has gone offline or online, change "Green or red" dot
     *     *
     */

    private TextView noThreadsText;

    public void onCreate( Bundle savedInstance )
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.convo_thread_overview);

        noThreadsText = (TextView) findViewById(R.id.noConvosText);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        List<String> convos = grabThreads();
        if ( convos.size() == 0 ) {noThreadsText.setVisibility(View.VISIBLE);}
    }

    public List<String> grabThreads()
    {

        List<String> otherUserNames = new ArrayList<String>();

        MessagesDBHelper dbHelper = new MessagesDBHelper(this);
        SQLiteDatabase readable = dbHelper.getReadableDatabase();

        Cursor allSenders = readable.rawQuery("SELECT other_user_id, other_user_name FROM messages",null);

        while (allSenders.moveToNext()) {
            int userNameIndex = allSenders.getColumnIndex("other_user_name");

            String userName = allSenders.getString( userNameIndex );

            if ( !otherUserNames.contains( userName ) ) {
                otherUserNames.add( userName );
            }

        }

        allSenders.close();

        return otherUserNames;
    }


}
