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
    private List<MessageThreadUser> users;

    public MessageThreadAdapter( Context context, List<MessageThreadUser> users )
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
        boolean currentUserOnline = false;


        for (User user : LiveUsers.users) {
            String id = user.getId();
            if ( id.equals(currentUser.getId()) ) {currentUserOnline = true;}
        }

        TextView otherUser = ( TextView ) v.findViewById( R.id.convoOtherUserName );
        otherUser.setText(currentUser.getName() + " " + String.valueOf(currentUserOnline) );

        /**
         * Now have access to image, name. Now need to gain access to last message sent, and if user is online or not
         *
         */

        return v;
    }


}
