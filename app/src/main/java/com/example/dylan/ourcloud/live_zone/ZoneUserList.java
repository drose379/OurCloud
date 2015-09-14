package com.example.dylan.ourcloud.live_zone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.TypeHelper;
import com.example.dylan.ourcloud.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dylan on 9/10/15.
 */
public class ZoneUserList extends AppCompatActivity {

    private List<User> currentUsers = LiveUsers.users;

    ListView userList;
    LiveUserListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.live_zone_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);

        toolbarTitle.setTypeface(TypeHelper.getTypefaceBold(this));
        toolbarTitle.setText(UserInfo.getInstance().getZoneName());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        userList = (ListView) findViewById(R.id.activeUserList);

    }

    @Override
    public void onStart() {
        super.onStart();
        updateUserList(currentUsers);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter iFilter = new IntentFilter(LiveUsers.UPDATE_ACTIVE_USERS);
        broadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<User> usersList = intent.getParcelableArrayListExtra("activeUsers");
                updateUserList(usersList);
            }
        }, iFilter);
    }


    public void updateUserList(final List<User> users) {

        if (listAdapter != null) {
            listAdapter.updateUsers(users);
        } else {
            listAdapter = new LiveUserListAdapter(this,users);
            userList.setAdapter(listAdapter);
        }

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView,View view,int item,long id) {
                User selectedUser = users.get(item);
                if (!selectedUser.getName().equals(UserInfo.getInstance().getDisplayName())) {
                    //For testing the private messaging feature
                    LiveUsers.getInstance(ZoneUserList.this).sendMessage(selectedUser.getId(),"This is a test message");
                }
            }
        });

    }

}
