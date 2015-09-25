package com.example.dylan.ourcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.example.dylan.ourcloud.live_zone.LiveUsers;
import com.example.dylan.ourcloud.live_zone.User;

import java.util.List;

/**
 * Created by dylan on 9/25/15.
 * This activity listens for User updates from the server along with network updates
 */
public class UserListenerActivity  extends NetworkListenerActivity {

    @Override
    public void onStart() {
        super.onStart();
        initUserListener();
    }

    public void initUserListener() {
        BroadcastReceiver userReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<User> users = intent.getParcelableArrayListExtra("activeUsers");
                userUpdate(users);
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(userReceiver,new IntentFilter(LiveUsers.UPDATE_ACTIVE_USERS));
    }

    public void userUpdate(List<User> users) {
        /**
         * Activities that extend this will implement their own functionality
         */
    }

}
