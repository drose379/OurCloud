package com.example.dylan.ourcloud.post_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.TypeHelper;
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
    private Bundle postBundle;

    FloatingActionButton menuButton;
    DrawerLayout navDrawer;
    ListView navDrawerItems;

    Fragment currentDetailFrag;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.post_detail);

        post = (Post) getIntent().getSerializableExtra("selectedPost");
        postBundle = new Bundle();
        postBundle.putSerializable("postInfo",post);

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
        initPostDetailView();

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
        navDrawerItems.setAdapter(new ArrayAdapter<String>(this, R.layout.nav_item, new String[]{"Text", "Photo", "Comments", "More"}));

        navDrawerItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                getSupportFragmentManager().beginTransaction().remove(currentDetailFrag).commit();

                switch (i) {
                    case 0:
                        //text
                        currentDetailFrag = new PostTextFrag();
                        currentDetailFrag.setArguments(postBundle);
                        break;
                    case 1:
                        //photo
                        break;
                    case 2:
                        //comments (also add conmments)
                        break;
                    case 3:
                        //More (see how many views, favorite the post, etc)
                        break;
                }

                navDrawer.closeDrawer(Gravity.LEFT);
                getSupportFragmentManager().beginTransaction().add(R.id.postDetailFrame,currentDetailFrag).commit();
            }
        });
    }

    public void initPostDetailView() {
        currentDetailFrag = new PostTextFrag();
        currentDetailFrag.setArguments(postBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.postDetailFrame,currentDetailFrag).commit();
    }


}
