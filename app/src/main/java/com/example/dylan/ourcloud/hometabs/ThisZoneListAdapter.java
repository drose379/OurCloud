package com.example.dylan.ourcloud.hometabs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.squareup.picasso.Picasso;

import java.awt.font.TextAttribute;
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

        Picasso.with(context).load(currentPost.getUserImage()).into(userImage);

        userName.setText(currentPost.isCurrentUser() ? "Me" : currentPost.getUser());
        userName.setTextColor(currentPost.isCurrentUser() ? context.getResources().getColor(R.color.ColorStart) : Color.parseColor("#000000"));

        postText.setText(currentPost.getPostText());

            //need to show post image (add functionality)
            //need to give comment option
            //create detail view to view comments on card click
            //show date in card bottom section

        return v;
    }
}
