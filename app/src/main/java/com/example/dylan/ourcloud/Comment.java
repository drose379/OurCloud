package com.example.dylan.ourcloud;

/**
 * Created by dylan on 8/9/15.
 */
public class Comment {

    private String usersName;
    private String userImageUrl;
    private String commentText;

    private long commentTime;

    public Comment setUsersName(String userName) {
        usersName = userName;
        return this;
    }
    public Comment setUserImage(String userImageUrl) {
        this.userImageUrl = userImageUrl;
        return this;
    }
    public Comment setCommentTime(String milliseconds) {
        commentTime = Long.decode(milliseconds);
        return this;
    }
    public Comment setCommentText(String comment) {
        commentText = comment;
        return this;
    }

    public String getCommentText() {
        return commentText;
    }
    public String getUserName() {
        return usersName;
    }
    public String getUserImage() {
        return userImageUrl;
    }

}
