package com.example.dylan.ourcloud.live_zone;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;

import java.io.IOException;

/**
 * Created by dylan on 9/23/15.
 */
public class NewLiveUser extends IntentService {

    public static String USER_ONLINE = "USER_ONLINE";

    public NewLiveUser() {
        super("NewLiveUser");
    }

    @Override
    public void onHandleIntent( Intent intent ) {
        //Untested, need to make a similar service to this that is called when user closes app, to remove them from live users DB
        //Should get a notificationf from gcm after this request finishes with updated list of users
        LocalUser localUser = LocalUser.getInstance(this);
        OkHttpClient http = new OkHttpClient();
        JSONArray items = new JSONArray()
                .put(localUser.getItem(LocalUserDBHelper.gcm_id_col))
                .put(localUser.getItem(LocalUserDBHelper.user_id_col))
                .put(localUser.getItem(LocalUserDBHelper.zone_name_col))
                .put(localUser.getItem(LocalUserDBHelper.nameCol))
                .put(localUser.getItem(LocalUserDBHelper.profile_image_col));

        RequestBody rBody = RequestBody.create(MediaType.parse("text/plain"), items.toString());
        Request request = new Request.Builder()
                .post(rBody)
                .url("http://104.236.15.47/OurCloudAPI/index.php/live/newUserEnter")
                .build();

        try {
            Response httpResponse = http.newCall(request).execute();
        } catch (IOException e) {throw new RuntimeException(e.getMessage());}

    }

}
