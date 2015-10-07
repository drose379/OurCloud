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

    public void setPostId(String id) {
        postId = id;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public void setUserImage(String url) {
        userImageUrl = url;
    }
    public void setPostType(String type) {
        postType = type;
    }
    public void setPostText(String postText) {
        this.postText = postText;
    }
    public void setPostImage(String url) {
        postImageUrl = url;
    }
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
    public void setPostTimeMillis(long millis)  {
        postTimeMillis = millis;
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
