package com.example.dylan.ourcloud;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by dylan on 9/22/15.
 */
public class GcmTokenGrab extends IntentService {

    public static String RECEIVE_GCM_TOKEN = "RECEIVE_GCM_TOKEN";

    public GcmTokenGrab() {
        super("RegisterGcm");
    }


    @Override
    public void onHandleIntent( Intent intent ) {
        String gcmId;
        try {
            gcmId = InstanceID.getInstance(this).getToken("937815926312", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {throw new RuntimeException(e.getMessage());}

        Intent token = new Intent(RECEIVE_GCM_TOKEN);
        token.putExtra("gcmId",gcmId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(token);

    }

}
