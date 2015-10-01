package com.example.dylan.ourcloud.live_zone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.UserListenerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dylan on 9/29/15.
 */
public class PrivateMessagesOverview extends UserListenerActivity implements ListView.OnItemClickListener {

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
    private List<User> convos;
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
        convos = grabThreads();
        if ( convos.size() == 0 ) {noThreadsText.setVisibility(View.VISIBLE);}
        else
        {
            /**
             * Set adapter to show each convo and last message sent to user
             * Need to create basic messsage thread card
             * Need to have access to users photo and last message sent for the card
              */

            ListView convoList = (ListView) findViewById( R.id.convoThreadsList );
            MessageThreadAdapter convoAdapter = new MessageThreadAdapter( this, convos );

            convoList.setAdapter( convoAdapter );
            convoList.setOnItemClickListener(this);


        }
    }

    public List<User> grabThreads()
    {

        List<String> otherUserNames = new ArrayList<String>();
        List<User> convoUsers = new ArrayList<User>();

        MessagesDBHelper dbHelper = new MessagesDBHelper(this);
        SQLiteDatabase readable = dbHelper.getReadableDatabase();

        Cursor allSenders = readable.rawQuery("SELECT other_user_id, other_user_name, other_user_image FROM messages",null);

        while (allSenders.moveToNext()) {
            int userNameIndex = allSenders.getColumnIndex("other_user_name");
            int userIdIndex = allSenders.getColumnIndex("other_user_id");
            int userImageIndex = allSenders.getColumnIndex("other_user_image");

            String userId = allSenders.getString( userIdIndex );
            String userName = allSenders.getString( userNameIndex ); //Really shouldnt be any, but remove any null usernames
            String userImage = allSenders.getString(userImageIndex);

            //need to fix this loop to make sure only unique Users are added to list, may need a name list along with a user list, check name, if not in, add to user, add to name

            if ( !otherUserNames.contains( userName ) ) {
                otherUserNames.add( userName );
                convoUsers.add( new User().setName(userName).setImage(userImage).setId(userId) );
            }

        }

        allSenders.close();

        return convoUsers;
    }

    @Override
    public void onItemClick( AdapterView parent, View view, int position, long id )
    {
        /**
         * This activity needs to be adjusted to have access to everything that can be added to a `User` object
            * Name
            * Id
            * Photo
         * MessagesDBHelper must be changed to store a userPhoto for each message sent
         * OR, have a table that stores info about each user the Local user comes into contact with, stores name, id, photo, and this activity can pull from there
         * Where does "People Here" activity get its photo from
         * Open ChatConvo activity, needs to be passed a User object through intent
         */

    }


}
