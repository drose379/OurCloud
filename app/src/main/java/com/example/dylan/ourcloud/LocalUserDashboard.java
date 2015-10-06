package com.example.dylan.ourcloud;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.dylan.ourcloud.util.GPhotoUrlCut;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dylan on 10/6/15.
 */
public class LocalUserDashboard extends NetworkListenerActivity {



    public void onCreate( Bundle savedInstance ) {
        super.onCreate(savedInstance);
        setContentView( R.layout.local_user_dash );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        TextView toolbarTitle = (TextView) findViewById( R.id.toolbarTitle );

        TextView userNameHead = (TextView) findViewById( R.id.userName );
        CircleImageView userImageHead = (CircleImageView) findViewById( R.id.userImage );

        userNameHead.setText( LocalUser.getInstance( this ).getItem( LocalUserDBHelper.nameCol ) );
        toolbarTitle.setText( LocalUser.getInstance( this ).getItem( LocalUserDBHelper.nameCol ) );
        Picasso.with(this).load(GPhotoUrlCut.getImageSized(LocalUser.getInstance(this).getItem(LocalUserDBHelper.profile_image_col), 80)).into(userImageHead);

    }

}
