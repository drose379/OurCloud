package com.example.dylan.ourcloud.live_zone;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;

import java.io.IOException;

/**
 * Created by dylan on 9/23/15.
 */
public class ExitLiveUser extends IntentService {

    public ExitLiveUser() {
        super("ExitLiveUser");
    }

    @Override
    public void onHandleIntent(Intent intent) {

        LiveUsers.appActive = false;
        OkHttpClient http = new OkHttpClient();
        LocalUser user = LocalUser.getInstance(this);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),
                new JSONArray().put(user.getItem(LocalUserDBHelper.user_id_col)).put(user.getItem(LocalUserDBHelper.zone_id_col)).toString());
        Request r = new Request.Builder()
                .post(body)
                .url("http://104.236.15.47/OurCloudAPI/index.php/live/userExit")
                .build();
        try {
            http.newCall(r).execute();
        } catch (IOException e) {e.printStackTrace();}
    }

}
