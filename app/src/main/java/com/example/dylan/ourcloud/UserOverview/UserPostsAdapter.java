package com.example.dylan.ourcloud.UserOverview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.ViewedPost;
import com.example.dylan.ourcloud.util.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dylan on 10/7/15.
 */
public class UserPostsAdapter extends BaseAdapter {

    private Context context;
    private List<ViewedPost> posts;

    public UserPostsAdapter( List<ViewedPost> posts, Context context ) {
        this.context = context;
        this.posts = posts;
    }

    public int getCount() {
        return posts.size();
    }

    public long getItemId( int item ) {
        return 0;
    }

    public ViewedPost getItem( int i ) {
        return posts.get( i );
    }

    public View getView( int item, View recycledView, ViewGroup parent ) {

        View v = recycledView == null ? LayoutInflater.from(context).inflate( R.layout.user_overview_post_card, parent, false ) : recycledView;

        ViewedPost currentPost = posts.get( item );

        TextView postText = (TextView) v.findViewById( R.id.postText );
        TextView viewCount = (TextView) v.findViewById( R.id.viewCount );
        TextView postTime = (TextView) v.findViewById( R.id.dateText );
        ImageView postImage = (ImageView) v.findViewById( R.id.postImage );

        switch ( currentPost.getPostType() ) {

            case "1" :
                postImage.setVisibility( View.GONE );
                postText.setText(currentPost.getPostText());
                break;
            case "2" :
                postText.setText( currentPost.getPostText() );
                Picasso.with( context ).load( currentPost.getPostImageUrl() ).into( postImage );
                break;
            case "3" :
                postText.setVisibility( View.GONE );
                Picasso.with( context ).load( currentPost.getPostImageUrl() ).into( postImage );
                break;

        }


        viewCount.setText( String.valueOf( "Views: " + currentPost.getViews().size() ) );
        postTime.setText(DateUtil.currentDate(currentPost.getPostTimeMillis()) );

        return v;
    }

}
