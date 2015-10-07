package com.example.dylan.ourcloud.post_detail;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.NetworkListenerActivity;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.TypeHelper;
import com.example.dylan.ourcloud.NavDrawerAdapter;
import com.example.dylan.ourcloud.home_zone.MenuOption;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * need to get rest of post info such as post text and photo, and comments
 * also show date of post, number of comments, number of views
 * also give option to add a comment to the post
 * when user views a post, OP of post knows which users have viewed their posts, so upload their name to server
*/


public class PostDetailView extends NetworkListenerActivity implements View.OnClickListener {

    private Post post;
    private Bundle postBundle;

    FloatingActionButton newCommentButton;
    ImageView toolbarMenuButton;
    DrawerLayout navDrawer;
    ListView navDrawerItems;
    MaterialDialog newComment;

    Fragment currentDetailFrag;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.post_detail);

        post = (Post) getIntent().getSerializableExtra("selectedPost");
        postBundle = new Bundle();
        postBundle.putSerializable("postInfo",post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarMenuButton = (ImageView) toolbar.findViewById(R.id.toolbarMenuButton);
        newCommentButton = (FloatingActionButton) findViewById(R.id.newCommentButton);
        navDrawer = (DrawerLayout) findViewById(R.id.postInfoLayout);
        navDrawerItems = (ListView) findViewById(R.id.navDrawerItems);

        toolbarTitle.setTypeface(TypeHelper.getTypefaceBold(this));
        //toolbarTitle.setText(post.getPostText().isEmpty() ? "Post Detail" : post.getPostText().substring(0, post.getPostText().length() > 10 ? post.getPostText().substring(0,10)) + "..."); // what if post text is fewer then 10 chars
        if (post.getPostText().isEmpty()) {
            toolbarTitle.setText("Post Detail");
        } else if (post.getPostText().length() > 10) {
            toolbarTitle.setText(post.getPostText().substring(0,10));
        } else {
            toolbarTitle.setText(post.getPostText());
        }

        toolbarMenuButton.setOnClickListener(this);
        newCommentButton.setOnClickListener(this);

        Intent postView = new Intent( this, PostViewRequest.class );
        postView.putExtra("postID",post.getId());
        postView.putExtra("userID",LocalUser.getInstance( this ).getItem(LocalUserDBHelper.user_id_col));
        startService( postView );

        initHeaderView();
        initNavDrawer();
        initPostDetailView();
        initDialog();

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.toolbarMenuButton :
                //check if nav drawer is open or closed, if open, close it, if closed, open it.

                if (navDrawer.isDrawerOpen(Gravity.LEFT)) {
                    navDrawer.closeDrawer(Gravity.LEFT);
                } else {
                    navDrawer.openDrawer(Gravity.LEFT);
                }

                break;
            case R.id.newCommentButton :
                Log.i("newComment","Button clicked");
                newComment.show();
                break;
        }
    }

    public void initHeaderView() {
        CircleImageView userHeader = (CircleImageView) findViewById(R.id.userHeaderPhoto);
        TextView userName = (TextView) findViewById(R.id.userHeaderName);

        //click listener for photo to show full screen photo fragment

        Picasso.with(this).load(post.getUserImageSized(70)).into(userHeader);
        userName.setText(post.getUser());
    }

    public void initNavDrawer() {
        List<MenuOption> menuOptions = new ArrayList<>();
        final String[] type1 = new String[] {"Text","Comments","More"};
        final String[] type2 = new String[] {"Text","Photo","Comments","More"};
        final String[] type3 = new String[] {"Photo","Comments","More"};

        //switch over type, set the
        switch (post.getType()) {
            case "1" :
                for (String option : type1) {
                    menuOptions.add(new MenuOption()
                                    .setTitle(option)
                                    .setType(1)
                    );
                }
                break;
            case "2" :
                for (String option : type2) {
                    menuOptions.add(new MenuOption()
                                    .setTitle(option)
                                    .setType(1)
                    );
                }
                break;
            case "3" :
                for (String option : type3) {
                    menuOptions.add(new MenuOption()
                                    .setTitle(option)
                                    .setType(1)
                    );
                }
                break;
        }

        navDrawerItems.setAdapter(new NavDrawerAdapter(this, menuOptions));


        navDrawerItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                getSupportFragmentManager().beginTransaction().remove(currentDetailFrag).commit();

                String itemSelected = post.getType().equals("1") ? type1[i] : (post.getType().equals("2") ? type2[i] : type3[i]);

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
                getSupportFragmentManager().beginTransaction().add(R.id.postDetailFrame, currentDetailFrag).commit();
            }
        });
    }

    public void initDialog() {
        View newCommentView = LayoutInflater.from(this).inflate(R.layout.new_comment, null);

        CircleImageView userImage = (CircleImageView) newCommentView.findViewById(R.id.userImageMini);
        TextView userName = (TextView) newCommentView.findViewById(R.id.userName);

        Picasso.with(this).load(LocalUser.getInstance(this).getProfilePhotoSized(80)).into(userImage);
        userName.setText(LocalUser.getInstance(this).getItem(LocalUserDBHelper.nameCol));

        newComment = new MaterialDialog.Builder(this)
                .title("New Comment")
                .customView(newCommentView, true)
                .positiveText("Done")
                .positiveColor(getResources().getColor(R.color.ColorPrimary))
                .negativeText("Cancel")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        EditText commentArea = (EditText) dialog.getCustomView().findViewById(R.id.newCommentArea);
                        if (!commentArea.getText().toString().isEmpty()) {
                            String comment = commentArea.getText().toString();
                            CommentController.getInstance(PostDetailView.this).newComment(comment, post);
                            commentArea.setText("");
                            //why can I not show Loading dialog here? Saying window has leaked...
                        }
                    }
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        EditText commentArea = (EditText) dialog.getCustomView().findViewById(R.id.newCommentArea);
                        commentArea.setText("");
                    }
                })
                        //Need to implement onDismiss to clear edittext whenever dialog is dismissed, Use LayoutInflater to inflate view instead
                .build();
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
