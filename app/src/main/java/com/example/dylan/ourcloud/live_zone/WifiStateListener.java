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
import com.example.dylan.ourcloud.WifiController;

import java.util.Set;

/**
 * Created by dylan on 9/24/15.
 */
public class WifiStateListener extends BroadcastReceiver {

    public static String EXIT_WIFI = "EXIT_WIFI";

    private String previousType;

    @Override
    public void onReceive(Context context,Intent intent) {

        String conType;

        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetwork = conManager.getActiveNetworkInfo();
        if ( activeNetwork != null ) {
            conType = conManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI ? "WIFI" : "OTHER";


            if (!conType.equals(previousType)) {
                Log.i("conType", conType + " Previous Type: " + previousType);
            }

            previousType = conType;
            Log.i("conType", "Assigned previous type to " + previousType);
        }
        //Getting 2 WIFI broadcasts when connected to wrii, make sure to use previousType pattern to only use 1



        //use previousType idea to make sure not receiving OTHER OTHER twice, same for wifi
/**

        int networkType = (int) intent.getExtras().get("networkType");
        /**
         * Need to add a check if network "mobile" is active, if yes, call all code below
         * If it is Wifi, this means the user entered a new wifi zone, update all info, remove them from current zone... etc



        if ( previousType != networkType && networkType == 0 ) {

            Log.i("networkType", String.valueOf( networkType ) );

            Intent exitUser = new Intent(context,ExitLiveUser.class);
            context.startService(exitUser);

            Intent networkChange = new Intent(EXIT_WIFI);
            LocalBroadcastManager.getInstance(context).sendBroadcast(networkChange);

        } else if ( previousType != networkType && networkType == 1 ) {

            Log.i("networkType", String.valueOf( networkType ) );

            /**
             * Connected to new network, reload all app data pertaining to the current network and load new data for newly connected network
             * Bring back to ThisZone activity


        //send broadcast ENTER_WIFI to be received by NetworkListener activity, make sure only sent when completely connected and not just enabled
        } else {
            Log.i("networkType", String.valueOf( networkType ) + " Outside of conditionals" );
            Log.i("networkTypePrevious","Previous Type:" + previousType);
        }


        /**
         * Need to implement whether the network is connected or disconnected, and act accordingly.
         * WHEN WIFI DISCONNECTED, send out a broadcast, THE THISZONE activity will listen for it (if it is open) and show a dialog if it receives the broadcast


*/

    }



}
