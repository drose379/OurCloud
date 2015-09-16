package com.example.dylan.ourcloud.live_zone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;

/**
 * Created by dylan on 9/16/15.
 */
public class ChatConvo extends AppCompatActivity {

    private User sender;

    @Override
    public void onCreate( Bundle savedInstance ) {
        super.onCreate(savedInstance);
        setContentView(R.layout.chat_convo);

        sender = getIntent().getParcelableExtra("sender");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(sender.getName());



    }

    @Override
    public void onStart() {
        super.onStart();
        getMessages();
        //Need to listen for local broadcasts of new private message, if broadcast is for the userID in this convo, query db for new message
    }

    public void getMessages() {
        //called to query db for new messages
    }

}
