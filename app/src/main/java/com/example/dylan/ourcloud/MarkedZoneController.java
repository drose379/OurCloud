package com.example.dylan.ourcloud;

import android.content.Context;
import android.os.Handler;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 10/5/15.
 */
public class MarkedZoneController {

    public interface Callback {
        public void getMarkedZones( List<Zone> zones );
    }

    private Callback callback;

    private OkHttpClient httpClient;
    private Handler handler;

    private Context context;

    public MarkedZoneController( Context context ) {
        this.context = context;
        callback = (Callback) context;
        httpClient = new OkHttpClient();
        handler = new Handler();
    }

    public void getMarkedZones( String userId ) {
        JSONArray items = new JSONArray();
        items.put( userId );

        RequestBody body = RequestBody.create( MediaType.parse("text/plain"), items.toString() );
        Request r = new Request.Builder()
                .post( body )
                .url("http://104.236.15.47/OurCloudAPI/index.php/grabMarkedZones") // untested
                .build();
        Call c = httpClient.newCall( r );
        c.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String jsonResp = response.body().string();

                try {
                    JSONArray resp = new JSONArray( jsonResp );

                    handleMarkedZones( resp );

                } catch ( JSONException e) {
                    throw new RuntimeException( e.getMessage() );
                }
            }
        });
    }


    public void handleMarkedZones( JSONArray zones ) {
        if ( zones.length() == 0 ) {

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    callback.getMarkedZones(new ArrayList<Zone>());
                }
            };

            handler.post( r );

        } else {
            //work with the array to create list of Zone objects
        }
    }

}
