package com.example.dylan.ourcloud.home_zone;

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
 * Created by dylan on 10/5/15.
 */
public class MarkZoneService extends IntentService {

    public MarkZoneService()
    {
        super("MarkZone");
    }

    public void onHandleIntent( Intent intent )
    {
        Log.i("markZone", "Mark zone called");
        String userId = LocalUser.getInstance( this ).getItem( LocalUserDBHelper.user_id_col );
        String zoneId = LocalUser.getInstance( this ).getItem( LocalUserDBHelper.zone_id_col );

        JSONArray items = new JSONArray();
        items.put( userId );
        items.put( zoneId  );

        OkHttpClient http = new OkHttpClient();
        RequestBody body = RequestBody.create( MediaType.parse("text/plain"), items.toString() );
        Request request = new Request.Builder()
                .post( body )
                .url("http://104.236.15.47/OurCloudAPI/index.php/markZone")
                .build();

        try {
            http.newCall( request ).execute();
        } catch ( IOException e ) {

        }

    }

}
