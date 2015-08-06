package com.example.dylan.ourcloud;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.dylan.ourcloud.hometabs.SlidingTabLayout;
import com.example.dylan.ourcloud.hometabs.ViewPagerAdapterHome;

/**
 * Created by dylan on 8/6/15.
 */
public class HomeRoot extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.home_root);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);

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
