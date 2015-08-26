package com.example.dylan.ourcloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by dylan on 8/24/15.
 */
public class PostDetailView extends AppCompatActivity {

    private Post post;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.post_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(TypeHelper.getTypefaceBold(this));

        post = (Post) getIntent().getSerializableExtra("selectedPost");

        toolbarTitle.setText(post.getPostText().isEmpty() ? "Post Detail" :  post.getPostText().substring(0,10) + "...");

        initHeaderView();
        initPostView();
        //need to get rest of post info such as post text and photo, and comments
            //also show date of post, number of comments, number of views
        //also give option to add a comment to the post
        //when user views a post, OP of post knows which users have viewed their posts, so upload their name to server
    }



    public void initHeaderView() {
        ImageView userHeader = (ImageView) findViewById(R.id.userHeaderPhoto);
        TextView userName = (TextView) findViewById(R.id.userHeaderName);

        //click listener for photo to show full screen photo fragment

        Picasso.with(this).load(post.getUserImageSized(200)).into(userHeader);
        userName.setText(post.getUser());
    }

    public void initPostView() {
        //need to adjust visibility of both textview and imageviews depending on post type

        TextView postText = (TextView) findViewById(R.id.postText);
        ImageView postImage = (ImageView) findViewById(R.id.postImage);

        postText.setVisibility(post.getPostText().isEmpty() ? View.GONE : View.VISIBLE);
        if(postText.getVisibility() == View.VISIBLE) {postText.setText(post.getPostText());}

        postImage.setVisibility(post.getPostImageUrl() == null ? View.GONE : View.VISIBLE);
        if(postImage.getVisibility() == View.VISIBLE) {Picasso.with(this).load(post.getPostImageUrl()).into(postImage);}

        //post image
    }

}
