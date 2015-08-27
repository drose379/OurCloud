package com.example.dylan.ourcloud;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * need to get rest of post info such as post text and photo, and comments
 * also show date of post, number of comments, number of views
 * also give option to add a comment to the post
 * when user views a post, OP of post knows which users have viewed their posts, so upload their name to server
*/


public class PostDetailView extends AppCompatActivity implements View.OnClickListener {

    private Post post;

    FloatingActionButton menuButton;
    DrawerLayout navDrawer;
    ListView navDrawerItems;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.post_detail);

        post = (Post) getIntent().getSerializableExtra("selectedPost");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        menuButton = (FloatingActionButton) findViewById(R.id.menuButton);
        navDrawer = (DrawerLayout) findViewById(R.id.postInfoLayout);
        navDrawerItems = (ListView) findViewById(R.id.navDrawerItems);

        toolbarTitle.setTypeface(TypeHelper.getTypefaceBold(this));
        toolbarTitle.setText(post.getPostText().isEmpty() ? "Post Detail" : post.getPostText().substring(0, 10) + "...");

        menuButton.setOnClickListener(this);

        initHeaderView();
        initNavDrawer();
        //initPostView();

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.menuButton :
                //check if nav drawer is open or closed, if open, close it, if closed, open it.

                if (navDrawer.isDrawerOpen(Gravity.LEFT)) {
                    navDrawer.closeDrawer(Gravity.LEFT);
                } else {
                    navDrawer.openDrawer(Gravity.LEFT);
                }

                break;
        }
    }



    public void initHeaderView() {
        CircleImageView userHeader = (CircleImageView) findViewById(R.id.userHeaderPhoto);
        TextView userName = (TextView) findViewById(R.id.userHeaderName);

        //click listener for photo to show full screen photo fragment

        Picasso.with(this).load(post.getUserImageSized(150)).into(userHeader);
        userName.setText(post.getUser());
    }

    public void initNavDrawer() {
        navDrawerItems.setAdapter(new ArrayAdapter<String>(this,R.layout.nav_item,new String[]{"Text","Photo","Comments","More"}));
        navDrawerItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                navDrawer.closeDrawer(Gravity.LEFT);
                FragmentManager fragMan = getSupportFragmentManager();
                //need to remove current fragment, (if its not null, and assign the new one to currentFragment and add it with fragMan.beginTrans().add()
            }
        });
    }

    public void initPostView() {
        //need to adjust visibility of both textview and imageviews depending on post type



        //post image
    }

}
