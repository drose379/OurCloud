package com.example.dylan.ourcloud.hometabs;

import android.os.Handler;
import android.util.Log;

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

import org.json.JSONArray;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;


/**
 * Created by dylan on 8/6/15.
 */
public class ThisZoneController {

    public interface Callback {
        void getZoneId(String zoneId);
        void getZonePosts(List<Post> posts);
        void postSubmitted();
    }

    private OkHttpClient httpClient;
    private Handler handler;
    private Callback callback;

    public ThisZoneController(ThisZone frag) {
        callback = frag;
        handler = new Handler();
        httpClient = new OkHttpClient();
    }

    public void grabZonePosts() {
        String currentZone = UserInfo.getInstance().getZoneId();
        //grab items that user is OP off, but change the author name to "Me"

        RequestBody rBody = RequestBody.create(MediaType.parse("text/plain"), JSONUtil.generateJSONArray(currentZone));
        Request request = new Request.Builder()
                .post(rBody)
                .url("http://104.236.15.47/OurCloudAPI/index.php/getZonePosts")
                .build();
        Call newCall = httpClient.newCall(request);
        newCall.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                throw new RuntimeException(e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final List<Post> posts = JSONUtil.toPostList(response.body().string());
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        callback.getZonePosts(posts);
                    }
                };
                handler.post(r);
            }
        });

    }

    public void getZoneId(String ssid,JSONArray networksInRange) {
        String jsonItems = JSONUtil.generateJSONArray(ssid, networksInRange.toString());
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), jsonItems);
        Request request = new Request.Builder()
                .post(body)
                .url("http://104.236.15.47/OurCloudAPI/index.php/getZoneId")
                .build();
        httpClient.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.getZoneId(response.body().string().trim());
            }
        });
    }

    public void newPost(String postText,long expirationDate) {

        //need to construct a json array of just the values that are in range, add that as an item in jsonItems below. remember to json_decode it in the API
        UserInfo currentUser = UserInfo.getInstance();

        String jsonItems = JSONUtil.generateJSONArray(
                currentUser.getId(),
                currentUser.getZoneId(),
                postText.trim(),
                String.valueOf(getCurrentTimeMillis()),
                String.valueOf(expirationDate));

        RequestBody rBody = RequestBody.create(MediaType.parse("text/plain"), jsonItems);
        Request request = new Request.Builder()
                .url("http://104.236.15.47/OurCloudAPI/index.php/newPost")
                .post(rBody)
                .build();
        Call newCall = httpClient.newCall(request);
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

    public void newPostWithImage(String postText,String postImageUrl,long expirationDate) {

        UserInfo currentUser = UserInfo.getInstance();

        String jsonItems = JSONUtil.generateJSONArray(currentUser.getId(),
                currentUser.getZoneId(),
                postText.trim(),
                postImageUrl,
                String.valueOf(getCurrentTimeMillis()),
                String.valueOf(expirationDate));

        RequestBody rBody = RequestBody.create(MediaType.parse("text/plain"), jsonItems);
        Request request = new Request.Builder()
                .url("http://104.236.15.47/OurCloudAPI/index.php/newPostWithImage")
                .post(rBody)
                .build();
        Call newCall = httpClient.newCall(request);
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

    public long getCurrentTimeMillis() {
        Calendar currentCal = Calendar.getInstance();
        return currentCal.getTimeInMillis();
    }

}
