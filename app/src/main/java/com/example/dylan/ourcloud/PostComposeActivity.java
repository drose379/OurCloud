package com.example.dylan.ourcloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by dylan on 8/12/15.
 */
public class PostComposeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.post_compose);

        /**
         * Show current users photo and their name in header at top of layout (darker background then rest of layout)
         * Set listeners for image select button and done button
         * Have image select container be 300 dp high and match_parent width, with padding of 20 dp
         * When user selects, show loading dialog, when post is submitted successfully, close this activiy with this.finish()
          */

        TextView userName = (TextView) findViewById(R.id.userName);
        ImageView userImage = (ImageView) findViewById(R.id.userImage);

        userName.setText(UserInfo.getInstance().getDisplayName());
        Picasso.with(this).load(UserInfo.getInstance().getProfileImageSized(100)).into(userImage);

    }

}
