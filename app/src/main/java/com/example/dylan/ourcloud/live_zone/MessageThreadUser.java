package com.example.dylan.ourcloud.live_zone;

import android.os.Parcel;

/**
 * Created by dylan on 10/2/15.
 */
public class MessageThreadUser extends User {

    private String lastMessage;

    @Override
    public MessageThreadUser setId( String id )
    {
        this.id = id;
        return this;
    }

    @Override
    public MessageThreadUser setName( String name )
    {
        this.name = name;
        return this;
    }

    @Override
    public MessageThreadUser setImage( String imageUrl )
    {
        photoUrl = imageUrl;
        return this;
    }

    public void setLastMessage( String lastMessage )
    {
        this.lastMessage = lastMessage;
    }


    public String getLastMessage()
    {
        return lastMessage;
    }

}
