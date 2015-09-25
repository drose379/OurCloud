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

import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.NetworkListenerActivity;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.TypeHelper;
import com.example.dylan.ourcloud.UserListenerActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dylan on 9/10/15.
 */
public class ZoneUserList extends UserListenerActivity {

    private LocalUser localUser;

    private List<User> currentUsers = LiveUsers.users;

    ListView userList;
    LiveUserListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.live_zone_list);
        localUser = LocalUser.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);

        toolbarTitle.setTypeface(TypeHelper.getTypefaceBold(this));
        toolbarTitle.setText(localUser.getItem(LocalUserDBHelper.zone_name_col));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        userList = (ListView) findViewById(R.id.activeUserList);

    }

    @Override
    public void onStart() {
        super.onStart();

        userUpdate(currentUsers);

    }

    @Override
    public void userUpdate(final List<User> users) {
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
                if (!selectedUser.getName().equals(localUser.getItem(LocalUserDBHelper.nameCol))) {
                    /**
                     * Open the chat convorsation activity, pass the User object along
                     * Activity will use the User object to query the DB for previous messages
                     * Will be able to use LiveUsers.sendMessage() from convorsation, and also listen for new message broadcast
                     */
                    Intent i = new Intent(ZoneUserList.this,ChatConvo.class);
                    i.putExtra("other_user",selectedUser);
                    startActivity(i);
                }
            }
        });

    }

}
