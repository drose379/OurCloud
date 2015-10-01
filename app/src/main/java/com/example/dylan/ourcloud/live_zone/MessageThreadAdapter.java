package com.example.dylan.ourcloud.live_zone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;

import java.util.List;

/**
 * Created by dylan on 10/1/15.
 */
public class MessageThreadAdapter extends BaseAdapter
{

    private Context context;
    private List<User> users;

    public MessageThreadAdapter( Context context, List<User> users )
    {
        this.context = context;
        this.users = users;
    }

    public int getCount()
    {
        return users.size();
    }

    public User getItem( int item )
    {
        return users.get(item);
    }

    public long getItemId( int item )
    {
        return 0;
    }

    public View getView( int item, View recycledView, ViewGroup parent )
    {
        View v = recycledView == null ? LayoutInflater.from( context ).inflate( R.layout.convo_thread_card, parent,false ) : recycledView;

        User currentUser = users.get( item );

        TextView otherUser = ( TextView ) v.findViewById( R.id.convoOtherUserName );

        otherUser.setText( currentUser.getId() );

        return v;
    }


}
