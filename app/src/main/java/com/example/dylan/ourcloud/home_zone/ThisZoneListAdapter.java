package com.example.dylan.ourcloud.home_zone;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.util.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dylan on 8/6/15.
 */
public class ThisZoneListAdapter extends BaseAdapter {

    private List<Post> posts;

    Context context;
    LayoutInflater inflater;

    public ThisZoneListAdapter(Context context,List<Post> posts) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public long getItemId(int item) {
        return 0;
    }

    @Override
    public Post getItem(int item) {
        return posts.get(item);
    }

    @Override
    public View getView(int position,View recycledView,ViewGroup parent) {
        View v = recycledView;
        Post currentPost = posts.get(position);

        if(v == null) {
            v = inflater.inflate(R.layout.post_card, parent, false);
        }

        TextView userName = (TextView) v.findViewById(R.id.userName);
        ImageView userImage = (ImageView) v.findViewById(R.id.userImage);

        TextView postText = (TextView) v.findViewById(R.id.postText);
        final ImageView postImage = (ImageView) v.findViewById(R.id.postImage);

        TextView dateText = (TextView) v.findViewById(R.id.postDate);


        Picasso.with(context).load(currentPost.getUserImage()).into(userImage);

        userName.setText(currentPost.isCurrentUser() ? "Me" : currentPost.getUser());
        userName.setTextColor(currentPost.isCurrentUser() ? context.getResources().getColor(R.color.ColorStart) : Color.parseColor("#000000"));

        if(currentPost.getPostText().equals("")) {
            postText.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) postImage.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
            params.setMargins(0,(int)22.5,0,(int)22.5);
            postImage.setLayoutParams(params);
        } else {
            postText.setVisibility(View.VISIBLE);
            postText.setText(currentPost.getPostText());
        }

        postText.setText(currentPost.getPostText());

        if (!currentPost.getPostImageUrl().equals("null")) {
            postImage.setVisibility(View.VISIBLE);
            Picasso.with(context).load(currentPost.getPostImageUrl()).into(postImage);
        } else {
            postImage.setVisibility(View.GONE);
        }

        /**
         * Use DateUtil.currentDate to go from millis (come with currentPost) to string date, set the string time to the postDate textview
         * Need to add setPostTimeMillis and getPostTimeMillis method to Post class
         */
        dateText.setText(DateUtil.currentDate(currentPost.getPostTimeMillis()));

        Log.i("postType",currentPost.getType());

            //need to show post image (add functionality)
            //need to give comment option
            //create detail view to view comments on card click
            //show date in card bottom section, also show buttons for comment and favorite

        return v;
    }
}
