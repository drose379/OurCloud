package com.example.dylan.ourcloud.live_zone;

import android.os.Bundle;

import com.example.dylan.ourcloud.UserListenerActivity;

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

    public void onCreate( Bundle savedInstance )
    {
        super.onCreate( savedInstance );
    }


}
