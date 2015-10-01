package com.example.dylan.ourcloud.live_zone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.util.ContactUserLookup;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.gcm.GcmListenerService;

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
public class LiveUsers extends GcmListenerService {

    public static final String UPDATE_ACTIVE_USERS = "UPDATE_ACTIVE_USERS";
    public static final String NEW_PRIVATE_MESSAGE = "NEW_PRIVATE_MESSAGE";
    public static boolean appActive = true;

    public static ArrayList<User> users = new ArrayList<>();

    private boolean isConnected = false;

    private LocalUser localUser;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        if (appActive) {

            String messageType = data.getString("type");

            switch (messageType) {
                case "1" :

                    //live users update

                    LiveUsers.users.clear();
                    try {

                        JSONArray users = new JSONArray(data.getString("message"));
                        for (int i = 0; i < users.length(); i++) {
                            JSONObject currentUser = new JSONObject(users.getString(i));
                            LiveUsers.users.add(new User()
                                    .setId(currentUser.getString("user_id"))
                                    .setName(currentUser.getString("user_name"))
                                    .setImage(currentUser.getString("user_photo")));
                        }

                    } catch (JSONException e) {e.getMessage();}

                    updateUsersBroadcast();
                    updateUserDictionary();

                    break;

                case "2"  :

                    //private message

                    SQLiteDatabase writeable = new MessagesDBHelper(this).getWritableDatabase();

                    String senderId = data.getString("senderId");
                    String message = data.getString("message");
                    int origin = 2;

                    ContentValues vals = new ContentValues();
                    vals.put("other_user_id",senderId);
                    vals.put("origin", origin);
                    vals.put("message", message);

                    writeable.insert("messages", null, vals);

                    Intent newMessage = new Intent(NEW_PRIVATE_MESSAGE);
                    newMessage.putExtra("other_user_id",senderId);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(newMessage);

                    notifyNewMessage( senderId, message );

                    break;
            }
        }

        /**
         * Check message type, either new live chat for client, or updated list of users in this zone, use a switch to get the 'type' from the received data
         * Send local broadcasts from here, just like old activity did
         */
    }

    public void updateUsersBroadcast() {
        Intent updateUsers = new Intent(UPDATE_ACTIVE_USERS);
        updateUsers.putParcelableArrayListExtra("activeUsers",users);
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateUsers);
    }

    public void updateUserDictionary()
    {
        SQLiteDatabase writeable = new ContactDBHelper( this ).getWritableDatabase();
        for ( User user : users ){
            ContentValues vals = new ContentValues();
            vals.put( ContactDBHelper.idCol, user.getId() );
            vals.put( ContactDBHelper.nameCol, user.getName() );
            vals.put( ContactDBHelper.imageCol, user.getPhotoUrl() );


            writeable.insertWithOnConflict( ContactDBHelper.tableName,null,vals,SQLiteDatabase.CONFLICT_REPLACE );
        }

    }

    public void notifyNewMessage(String otherUserId, String message) {

        String senderName = ContactUserLookup.nameLookup( this, otherUserId );

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent onNotificationIntent = new Intent(this,ChatConvo.class);
        onNotificationIntent.putExtra("other_user",new User().setName(senderName).setId(otherUserId));

        //PendingIntent openOnNotification = PendingIntent.getActivity(this,1,onNotificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent openOnNotification = PendingIntent.getActivity(this,2,onNotificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification newMessageNoti = new Notification.Builder(this)
                .setContentTitle("New Message")
                .setContentTitle(senderName)
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

    public static String getUserName(String userId) {
        String otherUserName = null;
        for(User user : LiveUsers.users) {
            if (user.getId().equals(userId)) {
                otherUserName = user.getName();
            }
        }
        return otherUserName;
    }

}
