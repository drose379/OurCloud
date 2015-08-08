package com.example.dylan.ourcloud;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.hometabs.SlidingTabLayout;
import com.example.dylan.ourcloud.hometabs.ViewPagerAdapterHome;

/**
 * Created by dylan on 8/6/15.
 */
public class HomeRoot extends AppCompatActivity {

    WifiController wifiController;

    SlidingTabLayout tabs;
    ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.home_root);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
    }

    @Override
    public void onStart() {

        /**
         * If user logs in without wifi connection, gets dialog and then connects, their bssid is never being set, need to have a config method here and update UserInfo
         */
        super.onStart();
        wifiController = WifiController.getInstance(getApplicationContext());
        initWifiConnection();
    }

    public void initWifiConnection() {
        if (wifiController.isConnected()) {
            UserInfo.getInstance().setWifiId(wifiController.getWifiId());
            initHomeView();
        } else {
            showWifiConnectDialog();
        }
    }

    public void showWifiConnectDialog() {

        if (WifiController.getInstance(getApplicationContext()).isConnected()) {
            initHomeView();
        } else {
            MaterialDialog enableWifi = new MaterialDialog.Builder(this)
                    .title("Enable Wifi")
                    .content("Please enable wifi to post in a zone, press reload when connected. If you want to check your marked zones, click skip")
                    .positiveText("Reload")
                    .negativeText("Skip")
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            initWifiConnection();
                        }
                    })
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            //wants to just view marked zones, account for this
                        }
                    })
                    .build();

            enableWifi.show();
        }

    }

    public void initHomeView() {

        Log.i("userData","BSSID" + UserInfo.getInstance().getWifiId());

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.viewPager);

        pager.setAdapter(new ViewPagerAdapterHome(getSupportFragmentManager()));
        tabs.setViewPager(pager);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.ColorPrimaryDark);
            }

            @Override
            public int getDividerColor(int position) {
                return Color.parseColor("#607D8B");
            }
        });

    }

}
