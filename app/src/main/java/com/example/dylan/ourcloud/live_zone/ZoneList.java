package com.example.dylan.ourcloud.live_zone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.TypeHelper;
import com.example.dylan.ourcloud.UserInfo;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Iterator;

/**
 * Created by dylan on 9/8/15.
 */
public class ZoneList extends AppCompatActivity {

    private Socket socket;

    private Emitter.Listener updateUserListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("args",(String)args[0]);
        }
    };

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

        initSocketConnection();
    }

    @Override
    public void onStart() {
        super.onStart();

        JSONArray socketInfo = new JSONArray();

        socketInfo.put(UserInfo.getInstance().getId());
        socketInfo.put(UserInfo.getInstance().getZoneName());
        socketInfo.put(UserInfo.getInstance().getDisplayName());
        socketInfo.put(UserInfo.getInstance().getProfileImage());

        updateSocketInfo(socketInfo);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off("updateUsers",updateUserListener);
    }

    public void initSocketConnection() {
        try {
            socket = IO.socket("http://104.236.15.47:3000");
            socket.connect();

            socket.on("updateUsers",updateUserListener);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateSocketInfo(JSONArray info) {
        socket.emit("userInfo",info.toString());
    }

    public void updateActiveUsers(JSONObject activeUsers) {
        //update the active users listview with this object
        //Get keys of object, loop over keys, get info about each user and make a card for each user, can click and start chat.
        Log.i("rooms", activeUsers.names().toString());
    }

}
