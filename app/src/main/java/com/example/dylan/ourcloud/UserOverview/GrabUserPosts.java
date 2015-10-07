package com.example.dylan.ourcloud.UserOverview;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.ViewedPost;
import com.example.dylan.ourcloud.util.JSONUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

/**
 * Created by dylan on 10/6/15.
 */
public class GrabUserPosts extends AsyncTask<String,Integer,List<ViewedPost>> {

    public interface Callback {
        void getPosts( List<ViewedPost> posts );
    }

    private Context context;
    private Callback callback;
    private OkHttpClient client;

    public GrabUserPosts( Fragment context ) {
        this.context = context.getActivity();
        callback = (Callback) context;
        client = new OkHttpClient();
    }

    public List<ViewedPost> doInBackground( String... params ) {
        List<ViewedPost> userPosts;

        JSONArray items = new JSONArray();
        items.put( params[0] );

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), items.toString());
        Request request = new Request.Builder()
                .post( body )
                .url("http://104.236.15.47/OurCloudAPI/index.php/grabUserPosts")
                .build();

        try {
            Response response = client.newCall( request ).execute();
            userPosts = JSONUtil.toCurrentUserPostList(response.body().string(), context );
        } catch( IOException e ) {
            throw new RuntimeException( e.getMessage() );
        }


        return userPosts;
    }

    public void onPostExecute( List<ViewedPost> posts ) {
        callback.getPosts( posts );
    }

}
