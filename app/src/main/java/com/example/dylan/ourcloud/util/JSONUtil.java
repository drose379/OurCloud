package com.example.dylan.ourcloud.util;

import android.content.Context;
import android.util.Log;

import com.example.dylan.ourcloud.Comment;
import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.ViewedPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 8/8/15.
 */
public class JSONUtil {

    /**
     * Class for util methods that receive json and parse it into something usable
     * @param items
     * @return
     */

    public static String generateJSONArray(String... items) {
        JSONArray vals = new JSONArray();
        for (String val : items) {
            vals.put(val);
        }
        return vals.toString();
    }

    public static List<Post> toPostList(String postsJson,String currentUser) {
        List<Post> posts = new ArrayList<Post>();

        try {
            JSONArray postsParent = new JSONArray(postsJson);

            for(int i = 0;i<postsParent.length();i++) {
                JSONObject currentObject = postsParent.getJSONObject(i);

                Post currentPost = new Post(currentUser);
                currentPost.setUser(currentObject.getString("user_name"));
                currentPost.setUserImage(currentObject.getString("user_image"));
                currentPost.setPostId(currentObject.getString("ID"));
                currentPost.setPostText(currentObject.getString("postText"));
                currentPost.setPostImage(currentObject.getString("postImage"));
                currentPost.setPostType(currentObject.getString("postType"));
                currentPost.setPostTimeMillis(Long.decode(currentObject.getString("postTime")));

                posts.add(currentPost);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }
        return posts;
    }

    public static List<ViewedPost> toCurrentUserPostList( String postsJSON, Context context ) {
        List<ViewedPost> posts = new ArrayList<ViewedPost>();

        String currentUser = LocalUser.getInstance( context ).getItem(LocalUserDBHelper.nameCol);
        String userImage = LocalUser.getInstance( context ).getItem( LocalUserDBHelper.profile_image_col );

        try {
            JSONArray postsParent = new JSONArray(postsJSON);

            for(int i = 0;i<postsParent.length();i++) {
                JSONObject currentObject = postsParent.getJSONObject(i);

                ViewedPost currentPost = new ViewedPost(currentUser);
                currentPost.setUser(currentUser);
                currentPost.setUserImage( userImage );
                currentPost.setPostId(currentObject.getString("ID"));
                currentPost.setPostText(currentObject.getString("postText"));
                currentPost.setPostImage(currentObject.getString("postImage"));
                currentPost.setPostType(currentObject.getString("postType"));
                currentPost.setPostTimeMillis(Long.decode(currentObject.getString("postTime")));
                currentPost.setViews( currentObject.getString("views") );

                posts.add(currentPost);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }
        return posts;
    }

    public static List<Comment> toCommentList(String commentJson) {
        List<Comment> comments = new ArrayList<Comment>();

        try {

            JSONArray jsonComments = new JSONArray(commentJson);

            for(int i = 0; i < jsonComments.length();i++) {
                JSONObject currentComment = jsonComments.getJSONObject(i);

                Comment comment = new Comment()
                        .setUsersName(currentComment.getString("user_name"))
                        .setUserImage(currentComment.getString("user_image"))
                        .setCommentTime(currentComment.getString("comment_time"))
                        .setCommentText(currentComment.getString("comment"));

                comments.add(comment);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }
        return comments;
    }

}