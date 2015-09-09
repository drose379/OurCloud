package com.example.dylan.ourcloud.live_zone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.TypeHelper;
import com.example.dylan.ourcloud.UserInfo;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by dylan on 9/8/15.
 */
public class ZoneList extends AppCompatActivity {

    private Socket socket;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.live_zone_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);

        toolbarTitle.setTypeface(TypeHelper.getTypefaceBold(this));
        toolbarTitle.setText(UserInfo.getInstance().getZoneName());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initSocketConnection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    public void initSocketConnection() {
        try {
            socket = IO.socket("http://104.236.15.47:3000");
            socket.connect();

            socket.emit("roomName",UserInfo.getInstance().getZoneName());

        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
