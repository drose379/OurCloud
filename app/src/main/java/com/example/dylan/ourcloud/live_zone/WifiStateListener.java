package com.example.dylan.ourcloud.live_zone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Created by dylan on 9/24/15.
 */
public class WifiStateListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context,Intent intent) {

        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conManager.getActiveNetworkInfo().getTypeName().equals("mobile")) {
            LiveUsers.appActive = false;
            Intent exitUser = new Intent(context,ExitLiveUser.class);
            context.startService(exitUser);
        }

        /**
         * Need to implement whether the network is connected or disconnected, and act accordingly.
         */

    }

}
