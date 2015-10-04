package com.example.dylan.ourcloud.live_zone;

import android.os.Parcel;

/**
 * Created by dylan on 10/2/15.
 */
public class MessageThreadUser extends User {

    private String lastMessage;
    private int origin;

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

    /**
     *
     * @param origin 1 is outgoing, 2 incoming
     */
    public MessageThreadUser setOrigin( int origin )
    {
        this.origin = origin;
        return this;
    }

    public String getLastMessage()
    {
        return lastMessage;
    }

    public int getOrigin() { return origin; }

}
