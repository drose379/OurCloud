package com.example.dylan.ourcloud.post_detail;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.example.dylan.ourcloud.Comment;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.UserInfo;
import com.example.dylan.ourcloud.util.JSONUtil;
import com.example.dylan.ourcloud.util.TimeUtil;
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
    private Handler handler;
    private OkHttpClient httpClient;

    public interface Callback {
        void getComments(List<Comment> comments);
        void commentSubmitted();
    }

    public CommentController(Fragment context) {
        callback = (Callback) context;
        httpClient = new OkHttpClient();
        handler = new Handler();
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
        String json = JSONUtil.generateJSONArray(UserInfo.getInstance().getId(),post.getId(),String.valueOf(TimeUtil.getCurrentTimeMillis()),comment);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), json);
        Request request = new Request.Builder()
                .post(body)
                .url("http://104.236.15.47/OurCloudAPI/index.php/newComment")
                .build();
        Call newCall = httpClient.newCall(request);
        newCall.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                Runnable r = new Runnable() {@Override public void run() {callback.commentSubmitted();}};
                handler.post(r);
            }
        });

    }

    public void getComments(String postId) {
        //grabs all comments for given postId
    }

}
