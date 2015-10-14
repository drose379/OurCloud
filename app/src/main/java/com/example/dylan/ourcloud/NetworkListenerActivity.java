package com.example.dylan.ourcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Network;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.home_zone.ThisZone;
import com.example.dylan.ourcloud.live_zone.ExitLiveUser;
import com.example.dylan.ourcloud.live_zone.WifiStateListener;

/**
 * Created by dylan on 9/25/15.
 */
public abstract class NetworkListenerActivity extends AppCompatActivity {

    protected MaterialDialog noWifi;
    private boolean activityActive;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        initDialog();
        initWifiReceiver();
    }

    @Override
    public void onStart() {
        super.onStart();
        activityActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        activityActive = false;
    }

    public void initDialog() {
        /**
         * Need to get rid of the other wifi dialog and use this throughout app
         */
        noWifi = new MaterialDialog.Builder( NetworkListenerActivity.this )
                .title("Not connected!")
                .content("You are not connected to any zone, please choose an option")
                .positiveText("Global Zone")
                .negativeText("Close")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //join global
                        Log.i("noWifi","Global zone clicked");
                        updateZone();
                    }
                    @Override
                    public void onNegative( MaterialDialog dialog ) {
                        NetworkListenerActivity.this.finish();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        noWifi.show();
                    }
                })
                .build();
    }

    public void initWifiReceiver() {
        BroadcastReceiver networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context,Intent intent) {

                switch ( intent.getIntExtra("type", 0) ) {
                    //disconnect
                    case 0:

                        //if ( !noWifi.isShowing() ) { noWifi.show(); }
                        noWifi.show();

                        break;
                    //connect
                    case 1:

                        updateZone();

                        break;
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(networkReceiver,new IntentFilter(WifiStateListener.CONNECTION_UPDATE));

    }

    public void updateZone() {
        if ( noWifi.isShowing() ) {noWifi.hide();}
    }

}
