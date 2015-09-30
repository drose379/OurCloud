package com.example.dylan.ourcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.dylan.ourcloud.live_zone.LiveUsers;
import com.example.dylan.ourcloud.live_zone.User;

import java.util.List;

/**
 * Created by dylan on 9/25/15.
 * This activity listens for User updates from the server along with network updates
 */
public class UserListenerActivity extends NetworkListenerActivity {

    private BroadcastReceiver userUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<User> users = intent.getParcelableArrayListExtra("activeUsers");
            userUpdate(users);
        }
    };

    @Override
    public void onCreate( Bundle savedInstance ) {
        super.onCreate( savedInstance );
        initUserListener();
    }

    public void initUserListener() {
        LocalBroadcastManager.getInstance(this).registerReceiver(userUpdateReceiver, new IntentFilter(LiveUsers.UPDATE_ACTIVE_USERS));
    }

    @Override
    public void finish() {
        super.finish();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userUpdateReceiver); //untested
    }

    public void userUpdate(List<User> users) {
        /**
         * Activities that extend this will implement their own functionality
         */
    }


}
