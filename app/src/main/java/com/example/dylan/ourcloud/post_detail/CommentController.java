package com.example.dylan.ourcloud.post_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import com.example.dylan.ourcloud.Comment;
import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.Post;

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

    public static String NEW_COMMENT_SUBMIT = "NEW_COMMENT_SUBMIT";

    private Context context;
    private Handler handler;
    private OkHttpClient httpClient;

    public interface Callback {
        void getComments( List<Comment> comments );
    }

    private static CommentController commentController = null;

    public static CommentController getInstance( Context context ) {
        commentController = commentController == null ? new CommentController(context) : commentController;
        return commentController;
    }

    public CommentController(Context context) {
        this.context = context;
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
        String json = JSONUtil.generateJSONArray(LocalUser.getInstance(context).getItem(LocalUserDBHelper.user_id_col),post.getId(),String.valueOf(TimeUtil.getCurrentTimeMillis()),comment);
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
                /**
                 * Send local broadcast that there is new comment, any act. interested will grab it
                 */
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(NEW_COMMENT_SUBMIT));
            }
        });

    }

    public void grabComments(String postId,final Fragment context) {
        final Callback callback = (Callback) context;

        String json = JSONUtil.generateJSONArray(postId);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), json);
        Request request = new Request.Builder()
                .post(body)
                .url("http://104.236.15.47/OurCloudAPI/index.php/getPostComments")
                .build();
        Call newCall = httpClient.newCall(request);
        newCall.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String commentsJson = response.body().string();
                final List<Comment> comments = JSONUtil.toCommentList(commentsJson);

                Runnable r = new Runnable() {@Override public void run() {callback.getComments(comments);}};
                handler.post(r);
            }
        });

    }

}
