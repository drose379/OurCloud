package com.example.dylan.ourcloud;


import java.util.List;

/**
 * Created by dylan on 8/6/15.
 */
public class Post {

    private String user;
    private String userImageUrl;
    private String postText;
    private String postImageUrl;
    private List<Comment> comments;

    public Post setUser(String user) {
        this.user = user;
        return this;
    }
    public Post setUserImage(String url) {
        userImageUrl = url;
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
    public Post setComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public String getUser() {
        return user;
    }
    public String getUserImage() {
        return userImageUrl;
    }
    public String getPostText() {
        return postText;
    }

    public boolean isCurrentUser() {
        boolean isCurrentUser = user.equals(UserInfo.getInstance().getDisplayName()) ? true : false;
        return isCurrentUser;
    }

}
