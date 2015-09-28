package com.example.dylan.ourcloud.live_zone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by dylan on 9/24/15.
 */
public class WifiStateListener extends BroadcastReceiver {

    public static String EXIT_WIFI = "EXIT_WIFI";

    @Override
    public void onReceive(Context context,Intent intent) {

        ConnectivityManager conManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        /**
         * Need to add a check if network "mobile" is active, if yes, call all code below
         * If it is Wifi, this means the user entered a new wifi zone, update all info, remove them from current zone... etc
         */

        Log.i("wifiState","wifi state intent sent");

        LiveUsers.appActive = false;

        Intent networkChange = new Intent(EXIT_WIFI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkChange);

        Intent exitUser = new Intent(context,ExitLiveUser.class);
        context.startService(exitUser);

        /**
         * Need to implement whether the network is connected or disconnected, and act accordingly.
         * WHEN WIFI DISCONNECTED, send out a broadcast, THE THISZONE activity will listen for it (if it is open) and show a dialog if it receives the broadcast
         */

    }



}
