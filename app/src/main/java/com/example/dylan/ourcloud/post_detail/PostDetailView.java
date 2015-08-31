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
        toolbarTitle.setText(post.getPostText().isEmpty() ? "Post Detail" : post.getPostText().substring(0, 10) + "..."); // what if post text is fewer then 10 chars

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
        final String[] type1 = new String[] {"Text","Comments","More"};
        final String[] type2 = new String[] {"Text","Photo","Comments","More"};
        final String[] type3 = new String[] {"Photo","Comments","More"};

        //switch over type, set the
        switch (post.getType()) {
            case "1" :
                navDrawerItems.setAdapter(new ArrayAdapter<String>(this,R.layout.nav_item,type1));
                break;
            case "2" :
                navDrawerItems.setAdapter(new ArrayAdapter<String>(this,R.layout.nav_item,type2));
                break;
            case "3" :
                navDrawerItems.setAdapter(new ArrayAdapter<String>(this,R.layout.nav_item,type3));
                break;
        }


        navDrawerItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                getSupportFragmentManager().beginTransaction().remove(currentDetailFrag).commit();

                String itemSelected = post.getType().equals(type1) ? type1[i] : (post.getType().equals(type2) ? type2[i] : type3[i]);

                switch (itemSelected) {
                    case "Text":
                        //text
                        currentDetailFrag = new PostTextFrag();
                        currentDetailFrag.setArguments(postBundle);
                        break;
                    case "Photo":
                        //photo
                        currentDetailFrag = new PostPhotoFrag();
                        currentDetailFrag.setArguments(postBundle);
                        break;
                    case "Comments":
                        //comments (also add comments functionality)
                        currentDetailFrag = new PostCommentsFrag();
                        currentDetailFrag.setArguments(postBundle);
                        break;
                    case "More":
                        //More (see how many views, favorite the post, etc)
                        break;
                }

                navDrawer.closeDrawer(Gravity.LEFT);
                getSupportFragmentManager().beginTransaction().add(R.id.postDetailFrame,currentDetailFrag).commit();
            }
        });
    }

    public void initPostDetailView() {
        //instead of setting post text to default, switch over the postType and assign either photo or text as currentDetailFrag depending if post has text or not
        switch (post.getType()) {
            case "1" :
                currentDetailFrag = new PostTextFrag();
                break;
            case "2" :
                currentDetailFrag = new PostPhotoFrag();
                break;
            case "3"  :
                currentDetailFrag = new PostPhotoFrag();
                break;
        }

        currentDetailFrag.setArguments(postBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.postDetailFrame,currentDetailFrag).commit();
    }


}
