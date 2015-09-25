package com.example.dylan.ourcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.example.dylan.ourcloud.live_zone.WifiStateListener;

/**
 * Created by dylan on 9/25/15.
 */
public abstract class NetworkListenerActivity extends AppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();
        initWifiReceiver();
    }

    public void initWifiReceiver() {
        BroadcastReceiver networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context,Intent intent) {
                NetworkListenerActivity.this.finish();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(networkReceiver,new IntentFilter(WifiStateListener.EXIT_WIFI));

    }
}
