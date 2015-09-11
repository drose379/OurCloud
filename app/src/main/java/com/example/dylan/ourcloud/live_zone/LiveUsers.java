package com.example.dylan.ourcloud.live_zone;

import android.content.Context;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

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

            Log.i("isConnected",String.valueOf(socket.connected()));

            socket.emit("connect");
        } catch (URISyntaxException e) {
           throw new RuntimeException(e.getMessage());
        }

    }

    public void updateUsers(String users) {
        //send local broadcast with jsonobject of active users
    }

    public void disconnect() {
        socket.disconnect();
        socket.off("updateUsers",updateUserListener);
    }
}
