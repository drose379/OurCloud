package com.example.dylan.ourcloud.post_detail;

import android.content.Context;

import com.example.dylan.ourcloud.Comment;
import com.example.dylan.ourcloud.Post;

import java.util.List;

/**
 * Created by dylan on 8/31/15.
 */
public class CommentController {

    private Callback callback;

    public interface Callback {
        void getComments(List<Comment> comments);
        void commentSubmitted();
    }

    public CommentController(Context context) {
        callback = (Callback) context;
    }

    public void newComment(String comment,Post post) {
        /**
         * NEED:
         * User ID , Post ID ---------      ) Going into post_comments table
         * Time of comment -----------      ) Going into post_comments table
         *
         * Callback with callback.commentSubmitted method
         */
    }

    public void getComments(String postId) {
        //grabs all comments for given postId
    }

}
