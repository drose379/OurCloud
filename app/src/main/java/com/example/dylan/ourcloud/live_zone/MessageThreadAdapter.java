package com.example.dylan.ourcloud.live_zone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.util.GPhotoUrlCut;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

    public void updateDataSet( List<MessageThreadUser> users )
    {
        this.users = users;
    }

    public View getView( int item, View recycledView, ViewGroup parent )
    {
        View v = recycledView == null ? LayoutInflater.from( context ).inflate( R.layout.convo_thread_card, parent,false ) : recycledView;

        MessageThreadUser currentUser = users.get( item );
        boolean currentUserOnline = false;


        for (User user : LiveUsers.users) {
            String id = user.getId();
            if ( id.equals(currentUser.getId()) ) {currentUserOnline = true;}
        }

        CircleImageView otherUserImage = ( CircleImageView ) v.findViewById( R.id.otherUserImage );
        TextView otherUserName = (TextView) v.findViewById( R.id.convoOtherUserName );
        TextView recentMessageText = (TextView) v.findViewById( R.id.recentMessage );

        Picasso.with( context ).load(GPhotoUrlCut.getImageSized(currentUser.getPhotoUrl(), 65) ).into( otherUserImage );
        otherUserName.setText(currentUser.getName() );
        recentMessageText.setText( currentUser.getLastMessage() );

        /**
         * Now have access to image, name. Now need to gain access to last message sent, and if user is online or not
         *
         */

        return v;
    }


}
