package com.example.dylan.ourcloud.hometabs;

import android.content.Context;

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

/**
 * Created by dylan on 8/6/15.
 */
public class ThisZoneController {

    public interface Callback {
        public void postSubmitted();
    }

    private Context context;

    private OkHttpClient httpClient;

    private Callback callback;

    public ThisZoneController(Context context) {
        this.context = context;
        callback = (Callback) context;

        httpClient = new OkHttpClient();
    }

    public String generateJSONArray(String... value) {
        JSONArray vals = new JSONArray();
        for (String val : value) {
            vals.put(val);
        }
        return vals.toString();
    }

    public void newPost(String postText) {
        String jsonVals = generateJSONArray(UserInfo.getPerson().getDisplayName(),UserInfo.getWifiId(),postText);
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
