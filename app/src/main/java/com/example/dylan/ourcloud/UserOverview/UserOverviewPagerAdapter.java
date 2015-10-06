package com.example.dylan.ourcloud.UserOverview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by dylan on 10/6/15.
 */
public class UserOverviewPagerAdapter extends FragmentPagerAdapter {

    private String[] items = { "Posts","Comments","Zones" };

    public UserOverviewPagerAdapter( FragmentManager manager ) {
        super( manager );
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Fragment getItem( int item ) {
        return new UserPostsFragment();
    }

    public String getTabTitle( int item ) {
        return items[item];
    }

}
