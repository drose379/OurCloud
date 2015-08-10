package com.example.dylan.ourcloud.hometabs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.UserInfo;
import com.example.dylan.ourcloud.WifiController;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by dylan on 8/6/15.
 */
public class ThisZone extends Fragment implements View.OnClickListener,ThisZoneController.Callback {

    private Context context;
    private WifiController wifiController;
    private ThisZoneController thisZoneController;

    FloatingActionButton newPostButton;
    ProgressBar loadingSpinner;
    ListView postContainer;
    TextView noPostsText;
    MaterialDialog newPost;
    MaterialDialog loading;
    MaterialDialog enableWifi;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        wifiController = WifiController.getInstance(context);
        thisZoneController = new ThisZoneController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstance) {
        super.onCreateView(inflater,parent,savedInstance);
        View v = inflater.inflate(R.layout.this_zone,parent,false);

        /**
         * Need a controller to grab this zones posts and have callback to this fragment.
         * Show each post in a card
         * Have fab for user to create a post
         * Swipe down to refresh (call controller method again)
         * INFLATE MATERIAL DIALOG TO ADD NEW POST
         * Whenever user refreshes feed or attempts to add a new post, check WifiController.isConnected() method and show dialog if not connected or zone changed.
         */
        postContainer = (ListView) v.findViewById(R.id.zonePostList);
        noPostsText = (TextView) v.findViewById(R.id.noPosts);
        loadingSpinner = (ProgressBar) v.findViewById(R.id.initialLoader);
        newPostButton = (FloatingActionButton) v.findViewById(R.id.newPostButton);

        newPostButton.setOnClickListener(this);
        newPostButton.attachToListView(postContainer);

        initDialogs();

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

    @Override
    public void getZonePosts(final List<Post> posts) {
        loadingSpinner.setVisibility(View.GONE);
        if (posts.size() > 0) {postContainer.setAdapter(new ThisZoneListAdapter(getActivity(), posts));}
        else {noPostsText.setVisibility(View.VISIBLE);}
        Log.i("postsCount", String.valueOf(posts.size()));
    }

    @Override
    public void postSubmitted() {

        loading.dismiss();
        thisZoneController.grabZonePosts();
        //refresh feed
    }

    public void initDialogs() {

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
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.newPostButton :

                newPost.show();

                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            Log.i("motionDown",String.valueOf(getView().getScrollY()));
        }
        return super.onTouchEvent())
    }


}
