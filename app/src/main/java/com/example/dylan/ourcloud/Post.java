package com.example.dylan.ourcloud;

import android.content.res.Resources;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 8/6/15.
 */
public class Post implements Serializable {

    private String currentUser;

    private String postId;
    private String user;
    private String userImageUrl;
    private String postType;
    private String postText;
    private String postImageUrl;
    private long postTimeMillis;
    private ArrayList<Comment> comments;

    public Post(String currentUser) {
        this.currentUser = currentUser;
    }

    public Post setPostId(String id) {
        postId = id;
        return this;
    }

    public Post setUser(String user) {
        this.user = user;
        return this;
    }
    public Post setUserImage(String url) {
        userImageUrl = url;
        return this;
    }
    public Post setPostType(String type) {
        postType = type;
        return this;
    }
    public Post setPostText(String postText) {
        this.postText = postText;
        return this;
    }
    public Post setPostImage(String url) {
        postImageUrl = url;
        return this;
    }
    public Post setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        return this;
    }
    public Post setPostTimeMillis(long millis)  {
        postTimeMillis = millis;
        return this;
    }

    public String getId() {return postId;}
    public String getUser() {
        return user;
    }
    public String getUserImage() {
        return userImageUrl;
    }
    public String getUserImageSized(int size) {
        Resources r = Resources.getSystem();
        String fullUrl = userImageUrl;
        String[] splitUrl = fullUrl.split("\\=");

        return splitUrl[0] + "=" + String.valueOf( size * Math.round(r.getDisplayMetrics().density) );
    }
    public String getPostText() {
        return postText;
    }
    public String getPostImageUrl() {
        return postImageUrl;
    }
    public String getType() {return postType;}
    public long getPostTimeMillis() {
        return postTimeMillis;
    }

    public boolean isCurrentUser() {
        return user.equals(currentUser);
    }

}
