package com.example.dylan.ourcloud.live_zone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.TypeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 9/16/15.
 */
public class ChatConvo extends AppCompatActivity implements View.OnClickListener {

    private User otherUser;
    private MessagesDBHelper messageDBHelper;
    private LocalBroadcastManager broadcastManager;
    private List<Message> messages = new ArrayList<Message>();
    private ChatConvoListAdapter chatConvoListAdapter;

    ListView messageList;
    Button sendButton;
    EditText messageArea;

    @Override
    public void onCreate( Bundle savedInstance ) {
        super.onCreate(savedInstance);
        setContentView(R.layout.chat_convo);

        otherUser = getIntent().getParcelableExtra("other_user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(otherUser.getName());

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
        getMessages();
        initBroadcastListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton :
                sendMessage();
                break;
        }
    }

    public void getMessages() {
        messages.clear();
        SQLiteDatabase readable = messageDBHelper.getReadableDatabase();
        Cursor results = readable.rawQuery("SELECT * FROM messages WHERE other_user_id = ?",new String[] {otherUser.getId()},null);

        while(results.moveToNext()) {
            int originCol = results.getColumnIndex("origin");
            int messageCol = results.getColumnIndex("message");
            int otherUserNameCol = results.getColumnIndex("other_user_name");

            messages.add(new Message()
                            .setOrigin(results.getInt(originCol))
                            .setText(results.getString(messageCol))
                            .setOtherUserName(results.getString(otherUserNameCol))
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

    public void sendMessage() {
        if ( !messageArea.isEmpty() ) {
            LiveUsers.getInstance(this).sendMessage(otherUser.getId(),messageArea.getText().toString());
            messageArea.clear();
            getMessages();
        }
    }

    public void initBroadcastListener() {
        //Listen for new private message broadcast
        //Only act if the senderId in the broadcast matches up with senderId in this conversation
        IntentFilter newMessageFilter = new IntentFilter(LiveUsers.NEW_PRIVATE_MESSAGE);
        broadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String otherUserId = intent.getStringExtra("other_user_id");
                if (otherUserId.equals(otherUser.getId())) {
                    getMessages();
                }
            }
        },newMessageFilter);
    }

}
