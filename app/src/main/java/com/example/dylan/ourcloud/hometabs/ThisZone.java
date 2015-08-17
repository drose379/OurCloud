package com.example.dylan.ourcloud.hometabs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.util.ImageUtil;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.PostComposeActivity;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.UserInfo;
import com.example.dylan.ourcloud.WifiController;

import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.util.List;

/**
 * Created by dylan on 8/6/15.
 */
public class ThisZone extends Fragment implements View.OnClickListener,ListView.OnScrollListener,SwipeRefreshLayout.OnRefreshListener,ThisZoneController.Callback,
    ImageUtil.ImageCallback{

    private Context context;
    private WifiController wifiController;
    private ThisZoneController thisZoneController;
    File newPostImage = null;

    Intent newPostTempData;

    SwipeRefreshLayout refreshLayout;
    FloatingActionButton newPostButton;
    ProgressBar loadingSpinner;
    ListView postContainer;
    TextView noPostsText;

    boolean dialogsInflated;
    MaterialDialog newPost;
    MaterialDialog loading;
    MaterialDialog enableWifi;
    MaterialDialog imageFromSelect;

    int previousTopMargin;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        wifiController = WifiController.getInstance(context);
        thisZoneController = new ThisZoneController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstance) {
        super.onCreateView(inflater, parent, savedInstance);
        View v = inflater.inflate(R.layout.this_zone,parent,false);

        /**
         * Need a controller to grab this zones posts and have callback to this fragment.
         * Show each post in a card
         * Have fab for user to create a post
         * Swipe down to refresh (call controller method again)
         * INFLATE MATERIAL DIALOG TO ADD NEW POST
         * Whenever user refreshes feed or attempts to add a new post, check WifiController.isConnected() method and show dialog if not connected or zone changed.
         */
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshContainer);
        postContainer = (ListView) v.findViewById(R.id.zonePostList);
        noPostsText = (TextView) v.findViewById(R.id.noPosts);
        loadingSpinner = (ProgressBar) v.findViewById(R.id.initialLoader);
        newPostButton = (FloatingActionButton) v.findViewById(R.id.newPostButton);

        refreshLayout.setOnRefreshListener(this);
        newPostButton.setOnClickListener(this);

        if(!dialogsInflated) {initDialogs();}

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        /**
         * Pull posts for this zone via method (must check wifi is still connected before calling method)
         * Call this same method and wifi checkpoint when user refreshes feed
         */
        initWifiConnect();
    }

    public void initWifiConnect() {
        if (wifiController.isConnected()) {
            UserInfo.getInstance().setWifiId(wifiController.getWifiId());
            thisZoneController.grabZonePosts();
        } else {
            enableWifi.show();
        }
    }

    /**
     * postContainer scroll listener needs to be updated each time data changes
     * @param posts
     */
    @Override
    public void getZonePosts(final List<Post> posts) {
        loadingSpinner.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
        if (posts.size() > 0) {
            postContainer.setVisibility(View.VISIBLE);
            postContainer.setAdapter(new ThisZoneListAdapter(getActivity(), posts));
            noPostsText.setVisibility(View.GONE);
        }
        else {
            noPostsText.setVisibility(View.VISIBLE);
            postContainer.setVisibility(View.GONE);
        }

        postContainer.setOnScrollListener(this);

    }

    @Override
    public void postSubmitted() {

        loading.dismiss();
        thisZoneController.grabZonePosts();
        //refresh feed
    }

    public void initDialogs() {

        dialogsInflated = true;

        newPost = new MaterialDialog.Builder(context)
                .title("New Post")
                .customView(R.layout.new_post_dialog, true)
                .positiveText("Post")
                .positiveColor(getResources().getColor(R.color.ColorPrimary))
                .negativeText("Cancel")
                .negativeColor(getResources().getColor(R.color.indicator))
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        EditText postInput = (EditText) dialog.getCustomView().findViewById(R.id.postInput);
                        String input = postInput.getText().toString();
                        if (!input.isEmpty()) {
                            thisZoneController.newPost(input);

                            dialog.dismiss();
                            loading.show();
                        }
                        //create method to clear edittext of dialogs
                        postInput.setText("");
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                        EditText postInput = (EditText) dialog.getCustomView().findViewById(R.id.postInput);
                        postInput.setText("");
                    }
                })
                .build();

        ImageView addImageButton = (ImageView) newPost.getCustomView().findViewById(R.id.image);
        addImageButton.setOnClickListener(this);


        loading = new MaterialDialog.Builder(context)
                .title("Publishing")
                .customView(R.layout.load_dialog, true)
                .autoDismiss(false)
                .build();

        enableWifi = new MaterialDialog.Builder(context)
                .title("Enable Wifi")
                .content("Please enable wifi to post in a zone, press reload when connected. If you want to check your marked zones, click skip")
                .positiveText("Reload")
                .negativeText("Skip")
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Log.i("onDismiss","Dismiss called");
                        initWifiConnect();
                    }
                })
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        //wants to just view marked zones, account for this with a seperate activity for just marked zones
                    }
                })
                .build();

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (resultCode) {
            case PostComposeActivity.POST_TEXT_ONLY :
                thisZoneController.newPost(data.getStringExtra("postText"));
                break;
            case PostComposeActivity.POST_BOTH:
                newPostTempData = data;
                ImageUtil.getInstance().uploadImage(this,PostComposeActivity.POST_BOTH,(File)data.getSerializableExtra("postImage"));
                //will receive callback with image url, call newpost with text and url there
                break;
            case PostComposeActivity.POST_PHOTO_ONLY:
                newPostTempData = data;
                //just image

                break;
        }
    }

    @Override
    public void imageUploaded(int status,String url) {
        switch (status) {
            case PostComposeActivity.POST_BOTH :
                thisZoneController.newPostWithImage(newPostTempData.getStringExtra("postText"),url);
                break;
            case PostComposeActivity.POST_PHOTO_ONLY :

                break;
        }
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.newPostButton:
                Intent i = new Intent(context, PostComposeActivity.class);
                startActivityForResult(i, 1);
                break;
        }

    }


    @Override
    public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount) {
        int topViewMargin = postContainer.getChildAt(0) != null ? postContainer.getChildAt(0).getTop() : 0;
        refreshLayout.setEnabled(firstVisibleItem == 0 && topViewMargin == 0);

        if (topViewMargin < previousTopMargin) {newPostButton.hide(true);} else {newPostButton.show(true);}

        previousTopMargin = topViewMargin;

        //Hide FAB on sroll.
        // To check if hide or show button, check if the topViewMargin is greater or less then previous topViewMargin (save topViewMargin to field each scroll)
        //If current top margin is greater then previous, hide the button, if its less, show the button (scroll up)

    }
    @Override
    public void onScrollStateChanged(AbsListView view,int scrollState) {}

    @Override
    public void onRefresh() {
        thisZoneController.grabZonePosts();
    }

}
