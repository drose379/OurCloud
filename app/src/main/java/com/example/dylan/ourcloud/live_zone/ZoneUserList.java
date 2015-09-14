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

    private String users = LiveUsers.currentUsers;

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
        updateUserList(users);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter iFilter = new IntentFilter(LiveUsers.UPDATE_ACTIVE_USERS);
        broadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String usersJson = intent.getStringExtra("activeUsers");
                updateUserList(usersJson);
            }
        }, iFilter);
    }


    public void updateUserList(String userJson) {
        final List<User> users = new ArrayList<User>();
        JSONObject activeUsers;
        try {

            activeUsers = new JSONObject(userJson);
            //Loop over they keys of the object, for each key there is a jsonArray, [Name,Image], add to List<User>
            Iterator<String> keys = activeUsers.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                JSONArray user = new JSONArray(activeUsers.getString(key));
                users.add(new User().setId(key).setName(user.getString(0)).setImage(user.getString(1)));
            }

        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }

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
                    LiveUsers.getInstance(ZoneUserList.this).sendMessage(selectedUser.getId(),"This is a test message!, TO: " + selectedUser.getName());
                }
            }
        });

    }

}
