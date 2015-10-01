package com.example.dylan.ourcloud.live_zone;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;

import java.io.IOException;

/**
 * Created by dylan on 9/24/15.
 */
public class SendPrivateMessage extends IntentService {

    private MessagesDBHelper messageDBHelper;

    public SendPrivateMessage() {
        super("SendPrivateMessage");
        messageDBHelper = new MessagesDBHelper(this);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Bundle messageInfo = intent.getBundleExtra("messageInfo");
        String receiverId = messageInfo.getString("receiverUserId");
        String message = messageInfo.getString("message");

        sendMessage(receiverId, message);
    }



    public void sendMessage(String receiverId, String message) {
        OkHttpClient http = new OkHttpClient();

        RequestBody rBody = RequestBody.create(MediaType.parse("text/plain"), new JSONArray()
                .put(LocalUser.getInstance(this).getItem(LocalUserDBHelper.user_id_col))
                .put(receiverId)
                .put(message).toString());

        Request r = new Request.Builder()
                .post(rBody)
                .url("http://104.236.15.47/OurCloudAPI/index.php/live/privateMessage")
                .build();
        try {
            http.newCall(r).execute();
        } catch (IOException e) {throw new RuntimeException(e.getMessage());}

    }


}
