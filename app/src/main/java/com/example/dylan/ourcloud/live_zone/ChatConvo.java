package com.example.dylan.ourcloud.live_zone;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.dylan.ourcloud.EditText;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.TypeHelper;

/**
 * Created by dylan on 9/16/15.
 */
public class ChatConvo extends AppCompatActivity implements View.OnClickListener {

    private User sender;

    Button sendButton;
    EditText messageArea;

    @Override
    public void onCreate( Bundle savedInstance ) {
        super.onCreate(savedInstance);
        setContentView(R.layout.chat_convo);

        sender = getIntent().getParcelableExtra("sender");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(sender.getName());

        sendButton = (Button) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        sendButton.setTypeface(TypeHelper.getTypefaceBold(this));
        sendButton.setOnClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();
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
        //called to query db for new messages
    }

    public void sendMessage() {

        if ( !messageArea.isEmpty() ) {
            LiveUsers.getInstance(this).sendMessage(sender.getId(),messageArea.getText().toString());
            messageArea.clear();
        }

        //test convorsation
        Cursor c = new MessagesDBHelper(this).getReadableDatabase().rawQuery("SELECT * FROM messages",null,null);
        while (c.moveToNext()) {
            int messageCol = c.getColumnIndex("message");
            Log.i("message",c.getString(messageCol));
        }

    }

    public void initBroadcastListener() {
        //Listen for new private message broadcast
        //Only act if the senderId in the broadcast matches up with senderId in this conversation
    }

}
