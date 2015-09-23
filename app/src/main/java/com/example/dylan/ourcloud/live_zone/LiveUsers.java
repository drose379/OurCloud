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

import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.R;
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
    public static ArrayList<User> users = new ArrayList<>();

    private boolean isConnected = false;

    private Context context;
    private Socket socket;
    private LocalUser localUser;


    @Override
    public void onMessageReceived(String from, Bundle data) {
        /**
         * Check message type, either new live chat for client, or updated list of users in this zone, use a switch to get the 'type' from the received data
         */
    }

}
