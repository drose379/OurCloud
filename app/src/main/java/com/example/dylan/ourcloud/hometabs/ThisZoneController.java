package com.example.dylan.ourcloud.hometabs;

import android.content.Context;
import android.util.Log;

import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.UserInfo;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dylan on 8/6/15.
 */
public class ThisZoneController {

    public interface Callback {
        void getZonePosts(List<Post> posts);
        void postSubmitted();
    }

    private OkHttpClient httpClient;

    private Callback callback;

    public ThisZoneController(ThisZone frag) {
        callback = frag;

        httpClient = new OkHttpClient();
    }

    public String generateJSONArray(String... value) {
        JSONArray vals = new JSONArray();
        for (String val : value) {
            vals.put(val);
        }
        return vals.toString();
    }

    public void getZonePosts() {
        String currentZone = UserInfo.getWifiId();
        //send to api which will query where zone = zone
        //grab items that user is OP off, change the author name to "Me"
    }

    public void newPost(String postText) {
        //remove all whitespace from postText
        String jsonVals = generateJSONArray(UserInfo.getPerson().getDisplayName(),UserInfo.getWifiId(),postText.trim());
        Log.i("jsonVals", jsonVals);
        RequestBody rBody = RequestBody.create(MediaType.parse("text/plain"), jsonVals);
        Request request = new Request.Builder()
                .url("http://104.236.15.47/OurCloudAPI/index.php/newPost")
                .post(rBody)
                .build();
        Call newCall = httpClient.newCall(request);;
        newCall.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.postSubmitted();
            }
        });
    }

}
