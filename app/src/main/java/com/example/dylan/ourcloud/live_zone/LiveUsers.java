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
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dylan on 9/10/15.
 */
public class LiveUsers {

    public static final String UPDATE_ACTIVE_USERS = "UPDATE_ACTIVE_USERS";
    //public static String currentUsers;
    public static ArrayList<User> users = new ArrayList<>();

    private boolean isConnected = false;

    private Context context;
    private Socket socket;

    private LocalBroadcastManager broadcastManager;

    private Emitter.Listener updateUserListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            updateUsers(args[0].toString());
            //currentUsers = args[0].toString();
        }
    };

    private Emitter.Listener privateMessageListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String senderId = null;
            String senderName = null;
            String message = null;
            /**
             * args[0] contians json string of [senderID,message]
             * Use users List<User> to look up the senderId and get a name and photo corresponding
             * Make the List<User> in ZoneUserList activity public
             */
            try {
                JSONArray messageInfo = new JSONArray(args[0].toString());
                senderId = messageInfo.getString(0);
                message = messageInfo.getString(1);
            } catch (JSONException e) {e.getMessage();}

            //loop over users List<User> and grab the id matching the senderId, use that object to get the name of the sender, assign to senderName field
            for(User user : users) {
                if (user.getId().equals(senderId)) {
                    senderName = user.getName();
                }
            }

            //This event listener needs to broadcast new message info to correct message thread
            //Test notifications on message receive, make sure activity is staying alive when app is in background

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

    public void updateUsers(String usersJson) {
        /**
         * Need to just broadcast the List<User> from here
         * Change all use of userString into using List<String>, no need to convert from string to List all around application, just do it here
         * User POJO must be parcelable.
         */

        createUserList(usersJson);

        Intent i = new Intent(UPDATE_ACTIVE_USERS);
        i.putParcelableArrayListExtra("activeUsers",users);
        broadcastManager.sendBroadcast(i);
    }

    public void createUserList(String usersJson) {
        users.clear();
        JSONObject activeUsers;
        try {
            activeUsers = new JSONObject(usersJson);
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
