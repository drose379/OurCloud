package com.example.dylan.ourcloud.live_zone;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

        /**
         * Need to show indicator of user online or offline ( Cloud icon and cloud with line through it icon )
         * Need to show incicator of whether recent message is incoming <- OR outgoing -> ( Arrow icons )
         */

        View v = recycledView == null ? LayoutInflater.from( context ).inflate( R.layout.convo_thread_card, parent,false ) : recycledView;
        CardView thisCard = (CardView) v.findViewById( R.id.card );

        MessageThreadUser currentUser = users.get( item );
        boolean currentUserOnline = false;


        for (User user : LiveUsers.users) {
            String id = user.getId();
            if ( id.equals(currentUser.getId()) ) {currentUserOnline = true;}
        }

        CircleImageView otherUserImage = ( CircleImageView ) v.findViewById( R.id.otherUserImage );
        TextView otherUserName = (TextView) v.findViewById( R.id.convoOtherUserName );
        TextView recentMessageText = (TextView) v.findViewById( R.id.recentMessage );
        ImageView otherUserStatus = ( ImageView ) v.findViewById( R.id.otherUserStatus );
        ImageView origin = (ImageView) v.findViewById( R.id.origin );
        thisCard.setCardBackgroundColor( Color.parseColor( "#E0E0E0" ) );

        Picasso.with( context ).load(GPhotoUrlCut.getImageSized(currentUser.getPhotoUrl(), 65) ).into(otherUserImage);
        otherUserName.setText(currentUser.getName());
        recentMessageText.setText(currentUser.getLastMessage().length() > 60 ? currentUser.getLastMessage().substring(0,60) + "..." : currentUser.getLastMessage());

        otherUserStatus.setImageDrawable( context.getResources().getDrawable(currentUserOnline ?
                R.drawable.ic_cloud_queue_black_24dp : R.drawable.ic_cloud_off_black_24dp ) );

        origin.setImageDrawable( context.getResources().getDrawable( currentUser.getOrigin() == 1 ? R.drawable.ic_arrow_forward_black_24dp :
                R.drawable.ic_arrow_back_black_24dp ) );




        return v;
    }


}
