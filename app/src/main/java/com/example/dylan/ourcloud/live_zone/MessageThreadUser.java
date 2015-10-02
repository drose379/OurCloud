package com.example.dylan.ourcloud.live_zone;

import android.os.Parcel;

/**
 * Created by dylan on 10/2/15.
 */
public class MessageThreadUser extends User {

    private String lastMessage;

    public void setLastMessage( String lastMessage )
    {
        this.lastMessage = lastMessage;
    }


    public String getLastMessage()
    {
        return lastMessage;
    }

}
