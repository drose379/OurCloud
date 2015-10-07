package com.example.dylan.ourcloud.post_detail;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;

import java.io.IOException;

/**
 * Created by dylan on 10/6/15.
 */
public class PostViewRequest extends IntentService {

    public PostViewRequest() {
        super("PostView");
    }

    public void onHandleIntent( Intent intent ) {
        OkHttpClient httpClient = new OkHttpClient();

        JSONArray items = new JSONArray();
        items.put( intent.getStringExtra("postID") );
        items.put( intent.getStringExtra("userID") );

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), items.toString() );
        Request r = new Request.Builder()
                .url("http://104.236.15.47/OurCloudAPI/index.php/newPostView")
                .post( body )
                .build();
        try {
            httpClient.newCall( r ).execute();
        } catch ( IOException e) {
            throw new RuntimeException( e.getMessage() );
        }

    }

}
