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

    public static String CONNECTION_UPDATE = "CONNECTION_UPDATE";
    public static String previousType = "null";


    @Override
    public void onReceive(Context context,Intent intent) {

        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();



        if ( netInfo != null ) {

            Log.i("newType",netInfo.getTypeName());

           // String networkType = netInfo.getType() == ConnectivityManager.TYPE_WIFI ? "WIFI" : "OTHER";
/**
            if ( netInfo.getType() == ConnectivityManager.TYPE_WIFI ) {
                networkType = "WIFI";
            } else if ( netInfo.getType() == ConnectivityManager.TYPE_MOBILE ) {
                networkType = "MOBILE";
            }
*/

            String networkType = netInfo.getTypeName();

            if ( !networkType.equals( previousType ) ) {

                /**
                 * This is working, if type is WIFI, reload all data, make sure to exit user from current zone, and open ThisZone with new zone info
                 * If type is other, Exit user from zone and show disconnectd popup
                 *
                 * That logic will be done in NetworkListenerActivity, this receiver just needs to send a broadcast with either Connected for WIFI or Disconnected for OTHER
                 */

                switch ( networkType ) {
                    case "WIFI" :

                        Intent enterWifiBroadcast = new Intent( CONNECTION_UPDATE );
                        enterWifiBroadcast.putExtra( "type", 1 );
                        LocalBroadcastManager.getInstance( context ).sendBroadcast( enterWifiBroadcast );

                        break;
                    case "MOBILE" :
                        /**
                         * 0 : Disconnect from wifi
                         * 1:  (Re)connected to wifi
                         */
                        Intent exitWifiBroadcast = new Intent( CONNECTION_UPDATE );
                        exitWifiBroadcast.putExtra( "type", 0 );
                        LocalBroadcastManager.getInstance( context ).sendBroadcast( exitWifiBroadcast );

                        break;
                }

            }

            previousType = networkType;

        }

        /**
         * Logging 1 every time, this means that a entire new Object is being created each time a broadcast is received
         *
         * This means need to store the previousType EITHER in a static field or elsewhere, maybe LocalUser, or in SQLITE table, find best implementation
         *
         * See github history for correct receiver code
         *
         * After testing
         *
         * STATIC FIELD WORKS, MOST LIKELY BEST SOLUTION, IMPLEMENT CORRECT RECEIVER CODE WITH STATIC FIELD FOR PREVIOUSTYPE
         *
         */

    }



}
