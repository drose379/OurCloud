package com.example.dylan.ourcloud.live_zone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.R;
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
    public static final String NEW_PRIVATE_MESSAGE = "NEW_PRIVATE_MESSAGE";
    public static ArrayList<User> users = new ArrayList<>();

    private boolean isConnected = false;

    private Context context;
    private Socket socket;
    private LocalUser localUser;

    private PendingIntent openOnNotification;
    private Intent onNotificationIntent;

    private LocalBroadcastManager broadcastManager;
    private MessagesDBHelper messageDBHelper;

    private Emitter.Listener updateUserListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            updateUsers(args[0].toString());
        }
    };

    private Emitter.Listener privateMessageListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String senderId = null;
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

            handleNewMessage(senderId,message);
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
        messageDBHelper = messageDBHelper == null ? new MessagesDBHelper(context) : messageDBHelper;
        localUser = LocalUser.getInstance(context);

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
                .put(localUser.getItem(LocalUserDBHelper.user_id_col))
                .put(localUser.getItem(LocalUserDBHelper.zone_name_col))
                .put(localUser.getItem(LocalUserDBHelper.nameCol))
                .put(localUser.getProfilePhotoSized(80));

        socket.emit("socketUserInfo",socketInfo.toString());
    }

    public void sendMessage(String receivingID,String message) {
        //May want to add senderPhotoURL as an argument, so the user who receives the message has photo of who they are chatting with
        JSONArray messageInfo = new JSONArray()
                .put(receivingID)
                .put(message);

        insertMessageToLocal(receivingID, message, 1);


        socket.emit("sendPrivateMessage", messageInfo.toString());

        /**
         *Also have to save outgoing messages to SQLite DB
         * (sender_id table will be the same, but may need to change name to avoid confusion) may just be chat_with_id
         */

    }

    public void updateUsers(String usersJson) {
        /**
         * Need to just broadcast the List<User> from here
         * Change all use of userString into using List<String>, no need to convert from string to List all around application, just do it here
         * User POJO must be parcelable.
         */

        createUserList(usersJson);

        Intent i = new Intent(UPDATE_ACTIVE_USERS);
        i.putParcelableArrayListExtra("activeUsers", users);
        broadcastManager.sendBroadcast(i);
    }

    public void handleNewMessage(String senderId,String message) {
        /**
         * Need to add message to SQLite messages table
         * AFTER NEW MESSAGE IS ADDED TO DB, THEN DO BELOW (SEND BROADCAST)
         * Need to send broadcast that the chat thread activity and open chat conversation activity will listen for
         * When these activities receive broadcast, they will query the DB and get the new message
         * If app is in background, set notification from these activities (activity will set boolean in onPause and onResume to know when in background)
         */

        insertMessageToLocal(senderId,message,0);
        notifyNewMessage(senderId, message);

        Intent i = new Intent( NEW_PRIVATE_MESSAGE );
        i.putExtra("other_user_id", senderId);
        broadcastManager.sendBroadcast(i);


        //Create a Message Pojo for organization, pass List<Message> to the listadapter for the chat activity, also good for adding new messages to adapter data set

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

    public void insertMessageToLocal(String otherUserId,String message,int origin) {
        String otherUserName = getUserName(otherUserId);
        SQLiteDatabase writeable = messageDBHelper.getWritableDatabase();

        ContentValues vals = new ContentValues();
        vals.put("other_user_id",otherUserId);
        vals.put("other_user_name",otherUserName);
        vals.put("origin", origin);
        vals.put("message", message);

        writeable.insert("messages",null,vals);
    }

    public void notifyNewMessage(String otherUserId, String message) {
        String otherUserName = getUserName(otherUserId);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        onNotificationIntent = new Intent(context,ChatConvo.class);
        onNotificationIntent.putExtra("other_user",new User().setName(otherUserName).setId(otherUserId));

        openOnNotification = PendingIntent.getActivity(context,1,onNotificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification newMessageNoti = new Notification.Builder(context)
                .setContentTitle("New Message")
                .setContentTitle(otherUserName)
                .setContentText(message)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(openOnNotification)
                .setTicker(message)
                .setLights(Color.GREEN,100,100)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .build();

        if (!ChatConvo.convoOtherUserId.equals(otherUserId)) {
            notificationManager.notify(otherUserId,1,newMessageNoti);
        }

    }

    public String getUserName(String userId) {
        String otherUserName = null;
        for(User user : users) {
            if (user.getId().equals(userId)) {
                otherUserName = user.getName();
            }
        }
        return otherUserName;
    }

    public void disconnect() {
        /**
         * Emit event saying that this socket will be disconnecting to server
         * Server will respond with a event with the socket attached userID &|| userName
         * Local broadcast saying that this userID is disconnecting
         * Chat convo actiivty will listen for this broadcast and show "X has left" if the otherUser has left
         */
        socket.disconnect();
        isConnected = false;
        socket.off("updateUsers",updateUserListener);
        socket.off("privateMessage",privateMessageListener);
    }

    public boolean isConnected() {
        return isConnected;
    }
}
