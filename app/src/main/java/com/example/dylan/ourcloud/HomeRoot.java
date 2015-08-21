package com.example.dylan.ourcloud;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.hometabs.SlidingTabLayout;
import com.example.dylan.ourcloud.hometabs.ViewPagerAdapterHome;

import java.util.List;

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
        TextView toolbarText = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarText.setText("Home");
        toolbarText.setTypeface(TypeHelper.getTypefaceBold(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onStart() {

        /**
         * If user logs in without wifi connection, gets dialog and then connects, their bssid is never being set, need to have a config method here and update UserInfo
         */
        super.onStart();
        initHomeView();


    }

    public void initHomeView() {


        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.viewPager);

        pager.setAdapter(new ViewPagerAdapterHome(getSupportFragmentManager()));
        tabs.setViewPager(pager);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.indicator);
            }

            @Override
            public int getDividerColor(int position) {
                return Color.parseColor("#607D8B");
            }
        });

    }

}
