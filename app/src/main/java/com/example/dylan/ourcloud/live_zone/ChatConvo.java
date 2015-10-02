package com.example.dylan.ourcloud.live_zone;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.example.dylan.ourcloud.EditText;
import com.example.dylan.ourcloud.NetworkListenerActivity;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.TypeHelper;
import com.example.dylan.ourcloud.UserListenerActivity;
import com.example.dylan.ourcloud.util.ContactUserLookup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 9/16/15.
 */
public class ChatConvo extends UserListenerActivity implements View.OnClickListener {

    public static String convoOtherUserId = "0";

    private User otherUser;
    private MessagesDBHelper messageDBHelper;
    private LocalBroadcastManager broadcastManager;
    private List<Message> messages = new ArrayList<Message>();
    private ChatConvoListAdapter chatConvoListAdapter;

    ListView messageList;
    Button sendButton;
    EditText messageArea;
    TextView toolbarTitle;

    @Override
    public void onCreate( Bundle savedInstance ) {

        super.onCreate(savedInstance);
        setContentView(R.layout.chat_convo);

        otherUser = getIntent().getParcelableExtra("other_user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);

        messageList = (ListView) findViewById(R.id.messageList);
        sendButton = (Button) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        sendButton.setTypeface(TypeHelper.getTypefaceBold(this));
        sendButton.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        messageDBHelper = new MessagesDBHelper(this);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        convoOtherUserId = otherUser.getId();
        toolbarTitle.setText(otherUser.getName());
        getMessages();
        initMessageListener();
        clearNotification();
    }

    @Override
    public void onPause() {
        super.onPause();
        convoOtherUserId = "0";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton :
                sendMessage();
                break;
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        /**
         * If user gets notification, and goes to application manager and closes the app before opening the notification,
         * When they attempt to send a message, the socket in LiveUsers class is null
         * Need to have a LiveUsers.isConnected() method to check if socket is connected.
         * If not, call LiveUsers.connect() method
         *
         */
        super.onNewIntent(intent);
        otherUser = intent.getParcelableExtra("other_user");
    }

    @Override
    public void userUpdate(List<User> users) {
        /**
         * If otherUser is not found in the updated list of Users, show card in messages with no name, saying X user has left, save this to db so it is shown next time
         * Disable the send button when user leaves, re enable if chat activity is still open and otherUser re-joins
         * To show send is disabled, change its button to a darker or dif shade
         */

        boolean userFound = false;

        for (User user : users) {
            if (user.getName().equals(otherUser.getName())) {
                userFound = true;
            }
        }

        if (!userFound) {
            otherUserLeft();
            //getMessages();
        } else {
            otherUserJoined();
            getMessages();
        }


    }

    public void otherUserLeft() {
        sendButton.setText( "Offline" );
        sendButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.send_button_offline));
        sendButton.setTextColor( getResources().getColor(R.color.ColorPrimary) );
        sendButton.setOnClickListener( null );
    }

    public void otherUserJoined() {
        sendButton.setText( "Send" );
        sendButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.send_button_online));
        sendButton.setTextColor( getResources().getColor(R.color.ColorLightPrimary) );
        sendButton.setOnClickListener(this);
    }

    public void clearNotification() {
        NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nManager.cancel(otherUser.getId(),1);
    }

    public void getMessages() {
        messages.clear();
        SQLiteDatabase readable = messageDBHelper.getReadableDatabase();
        Cursor results = readable.rawQuery("SELECT * FROM messages WHERE other_user_id = ?",new String[] {otherUser.getId()},null);

        while(results.moveToNext()) {
            int originCol = results.getColumnIndex("origin");
            int messageCol = results.getColumnIndex("message");
            int otherUserIdCol = results.getColumnIndex("other_user_id");

            messages.add(new Message()
                            .setOrigin(results.getInt(originCol))
                            .setText(results.getString(messageCol))
                            .setOtherUserName(ContactUserLookup.nameLookup( this, results.getString(otherUserIdCol) ))
                            );
        }

        if (chatConvoListAdapter != null) {
            chatConvoListAdapter.updateMessages(messages);
            messageList.smoothScrollToPosition(chatConvoListAdapter.getCount() - 1);
        } else {
            chatConvoListAdapter = new ChatConvoListAdapter(this,messages);
            messageList.setAdapter(chatConvoListAdapter);
            messageList.setSelection(chatConvoListAdapter.getCount() - 1);
        }

    }

    //add getMessages method to only get newest message in the db, no need to grab all messages when only 1 new message comes

    public void sendMessage() {
        if ( !messageArea.isEmpty() ) {
            Intent sendMessage = new Intent(this,SendPrivateMessage.class);
            Bundle messageInfo = new Bundle();
            messageInfo.putString("receiverUserId",otherUser.getId());
            messageInfo.putString("message", messageArea.getText().toString());
            sendMessage.putExtra("messageInfo", messageInfo);
            startService(sendMessage);

            insertMessageToLocal(otherUser.getId(),messageArea.getText().toString(),1);
            getMessages();

            messageArea.clear();
        }
    }

    public void insertMessageToLocal(String otherUserId,String message,int origin) {
        SQLiteDatabase writeable = messageDBHelper.getWritableDatabase();

        ContentValues vals = new ContentValues();
        vals.put("other_user_id",otherUserId);
        vals.put("origin", origin);
        vals.put("message", message);

        writeable.insert("messages", null, vals);
    }



    public void initMessageListener() {
        //Listen for new private message broadcast
        //Only act if the senderId in the broadcast matches up with senderId in this conversation
        IntentFilter newMessageFilter = new IntentFilter(LiveUsers.NEW_PRIVATE_MESSAGE);
        broadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("chatConvo","Received broadcast");
                String otherUserId = intent.getStringExtra("other_user_id");
                if (otherUserId.equals(otherUser.getId())) {
                    getMessages();
                }
            }
        },newMessageFilter);
    }

}
