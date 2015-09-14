package com.example.dylan.ourcloud.live_zone;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.dylan.ourcloud.UserInfo;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by dylan on 9/10/15.
 */
public class LiveUsers {

    public static final String UPDATE_ACTIVE_USERS = "UPDATE_ACTIVE_USERS";
    public static String currentUsers;

    private boolean isConnected = false;

    private Context context;
    private Socket socket;

    private LocalBroadcastManager broadcastManager;

    private Emitter.Listener updateUserListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            updateUsers(args[0].toString());
            currentUsers = args[0].toString();
        }
    };

    private Emitter.Listener privateMessageListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("privateMessage",(String)args[0]);
        }
    };

    private static LiveUsers liveUsers = null;

    public static LiveUsers getInstance(Context context) {
        liveUsers = liveUsers == null ? new LiveUsers(context) : liveUsers;
        return liveUsers;
    }

    public LiveUsers(Context context) {
        this.context = context;
        broadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public void connect() {
        isConnected = true;
        try {
            socket = IO.socket("http://104.236.15.47:3000").connect();
            socket.on("updateUsers",updateUserListener);
            socket.on("privateMessage",privateMessageListener);
            uploadSocketInfo();
        } catch (URISyntaxException e) {
            isConnected = false;
           throw new RuntimeException(e.getMessage());
        }
    }

    public void uploadSocketInfo() {
        JSONArray socketInfo = new JSONArray()
                .put(UserInfo.getInstance().getId())
                .put(UserInfo.getInstance().getZoneName())
                .put(UserInfo.getInstance().getDisplayName())
                .put(UserInfo.getInstance().getProfileImageSized(80));

        socket.emit("socketUserInfo",socketInfo.toString());
    }

    public void sendMessage(String receivingID,String message) {
        JSONArray messageInfo = new JSONArray()
                .put(receivingID)
                .put(message);
        socket.emit("sendPrivateMessage",messageInfo.toString());
    }

    public void updateUsers(String users) {
        Intent i = new Intent(UPDATE_ACTIVE_USERS);
        i.putExtra("activeUsers",users);
        broadcastManager.sendBroadcast(i);
    }

    public void disconnect() {
        socket.disconnect();
        isConnected = false;
        socket.off("updateUsers",updateUserListener);
        socket.off("privateMessage",privateMessageListener);
    }

    public boolean isConnected() {
        return isConnected;
    }
}
