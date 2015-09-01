package com.example.dylan.ourcloud.post_detail;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.dylan.ourcloud.Comment;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.UserInfo;
import com.example.dylan.ourcloud.util.JSONUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * Created by dylan on 8/31/15.
 */
public class CommentController {

    private Callback callback;

    private OkHttpClient httpClient;

    public interface Callback {
        void getComments(List<Comment> comments);
        void commentSubmitted();
    }

    public CommentController(Fragment context) {
        callback = (Callback) context;
        httpClient = new OkHttpClient();
    }

    public void newComment(String comment,Post post) {
        /**
         * NEED:
         * User ID , Post ID ---------      ) Going into post_comments table
         * Time of comment -----------      ) Going into post_comments table
         *
         * Callback with callback.commentSubmitted method
         */

        //Add time of comment
        String json = JSONUtil.generateJSONArray(UserInfo.getInstance().getId(),post.getId(),comment);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), json);
        Request request = new Request.Builder()
                .post(body)
                //.url()
                .build();
        Call newCall = httpClient.newCall(request);
        newCall.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.commentSubmitted();
            }
        });

    }

    public void getComments(String postId) {
        //grabs all comments for given postId
    }

}
