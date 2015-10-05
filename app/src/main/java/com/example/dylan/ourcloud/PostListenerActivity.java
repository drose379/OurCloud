package com.example.dylan.ourcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.example.dylan.ourcloud.live_zone.LiveUsers;

/**
 * Created by dylan on 10/5/15.
 */
public class PostListenerActivity extends NetworkListenerActivity {

    private BroadcastReceiver newPostReceiver;

    @Override
    public void onCreate( Bundle savedInstance ) {
        super.onCreate( savedInstance );
        registerNewPostListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance( this ).unregisterReceiver( newPostReceiver );
    }

    public void registerNewPostListener() {
        newPostReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive( Context context, Intent data ) {
                newPostReceived();
            }
        };

        LocalBroadcastManager.getInstance( this ).registerReceiver( newPostReceiver, new IntentFilter( LiveUsers.NEW_ZONE_POST ) );
    }

    public void newPostReceived() {

    }

}
