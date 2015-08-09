package com.example.dylan.ourcloud.hometabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dylan.ourcloud.R;

/**
 * Created by drose379 on 6/13/15.
 */
public class ViewPagerAdapterHome extends FragmentPagerAdapter {

    private String[] titles = {"This Zone","Marked Zones","My Posts"};

    public ViewPagerAdapterHome(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int item) {
        switch(item) {
            case 0 :
                ThisZone thisZone = new ThisZone();
                return thisZone;
            case 1:
                Fragment markedZones = new Fragment();
                return markedZones;
            case 2:
                Fragment myPosts = new Fragment();
                return myPosts;
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    public String getPageTitle(int position) {
        return titles[position];
    }

}
