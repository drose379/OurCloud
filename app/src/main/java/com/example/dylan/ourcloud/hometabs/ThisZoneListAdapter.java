package com.example.dylan.ourcloud.hometabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.awt.font.TextAttribute;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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


        Picasso.with(context).load(currentPost.getUserImage()).into(userImage);

        userName.setText(currentPost.isCurrentUser() ? "Me" : currentPost.getUser());
        userName.setTextColor(currentPost.isCurrentUser() ? context.getResources().getColor(R.color.ColorStart) : Color.parseColor("#000000"));

        postText.setText(currentPost.getPostText());

        if (!currentPost.getPostImageUrl().equals("null")) {
            Log.i("postBody","post" + currentPost.getPostText() + " " + currentPost.getPostImageUrl());
            postImage.setVisibility(View.VISIBLE);
            Picasso.with(context).load(currentPost.getPostImageUrl()).into(postImage);
        } else {
            postImage.setVisibility(View.GONE);
        }


            //need to show post image (add functionality)
            //need to give comment option
            //create detail view to view comments on card click
            //show date in card bottom section

        return v;
    }
}
