package com.example.dylan.ourcloud;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.dylan.ourcloud.UserOverview.SlidingTabLayout;
import com.example.dylan.ourcloud.UserOverview.UserOverviewPagerAdapter;
import com.example.dylan.ourcloud.util.GPhotoUrlCut;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dylan on 10/6/15.
 */
public class LocalUserDashboard extends NetworkListenerActivity implements View.OnClickListener {



    public void onCreate( Bundle savedInstance ) {
        super.onCreate(savedInstance);
        setContentView( R.layout.local_user_dash );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        TextView toolbarTitle = (TextView) findViewById( R.id.toolbarTitle );
        ImageView toolbarBackButton = (ImageView) findViewById( R.id.toolbarBackButton );

        TextView userNameHead = (TextView) findViewById( R.id.userName );
        CircleImageView userImageHead = (CircleImageView) findViewById( R.id.userImage );

        ViewPager pager = (ViewPager) findViewById( R.id.localUserPager );
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById( R.id.tabLayout );

        toolbarBackButton.setOnClickListener( this );

        FragmentPagerAdapter adapter = new UserOverviewPagerAdapter( getSupportFragmentManager() );
        pager.setAdapter( adapter );
        tabs.setViewPager(pager);

        SlidingTabLayout.TabColorizer tabColors = new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor( R.color.ColorPrimary );
            }

            @Override
            public int getDividerColor(int position) {
                return 0;
            }
        };

        tabs.setCustomTabColorizer( tabColors );

        userNameHead.setText( LocalUser.getInstance( this ).getItem( LocalUserDBHelper.nameCol ) );
        toolbarTitle.setText( LocalUser.getInstance( this ).getItem( LocalUserDBHelper.nameCol ) );
        Picasso.with(this).load(GPhotoUrlCut.getImageSized(LocalUser.getInstance(this).getItem(LocalUserDBHelper.profile_image_col), 80)).into(userImageHead);

    }

    @Override
    public void onClick( View v ) {

        switch ( v.getId() ) {
            case R.id.toolbarBackButton :
                this.finish();
        }

    }

}
