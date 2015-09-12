package com.example.dylan.ourcloud.live_zone;

import android.content.Context;
import android.util.Log;

import com.example.dylan.ourcloud.UserInfo;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;

import java.net.URISyntaxException;

/**
 * Created by dylan on 9/10/15.
 */
public class LiveUsers {

    public static final String UPDATE_ACTIVE_USERS = "UPDATE_ACTIVE_USERS";

    private Context context;
    private Socket socket;

    private Emitter.Listener updateUserListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            updateUsers((String)args[0]);
            Log.i("users", (String) args[0]);
        }
    };

    public LiveUsers(Context context) {
        this.context = context;
    }

    public void connect() {
        try {
            socket = IO.socket("http://104.236.15.47:3000").connect();
            socket.on("updateUsers",updateUserListener);
            updateSocketInfo();
        } catch (URISyntaxException e) {
           throw new RuntimeException(e.getMessage());
        }
    }

    public void updateSocketInfo() {
        JSONArray socketInfo = new JSONArray()
                .put(UserInfo.getInstance().getId())
                .put(UserInfo.getInstance().getZoneName())
                .put(UserInfo.getInstance().getDisplayName())
                .put(UserInfo.getInstance().getProfileImage());

        socket.emit("socketUserInfo",socketInfo.toString());
    }

    public void updateUsers(String users) {
        //send local broadcast with jsonobject of active users
    }

    public void disconnect() {
        socket.disconnect();
        socket.off("updateUsers",updateUserListener);
    }
}
