package com.example.dylan.ourcloud.hometabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.UserInfo;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by dylan on 8/6/15.
 */
public class ThisZone extends Fragment implements View.OnClickListener,ThisZoneController.Callback {

    private Context context;
    private ThisZoneController thisZoneController;

    FloatingActionButton newPostButton;
    ProgressBar loadingSpinner;
    ListView postContainer;
    MaterialDialog newPost;
    MaterialDialog loading;



    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        thisZoneController = new ThisZoneController(this);
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstance) {
        super.onCreateView(inflater,parent,savedInstance);
        View v = inflater.inflate(R.layout.this_zone,parent,false);

        /**
         * Need a controller to grab this zones posts and have callback to this fragment.
         * Show each post in a card
         * Have fab for user to create a post
         * Swipe down to refresh (call controller method again)
         * INFLATE MATERIAL DIALOG TO ADD NEW POST
         */

        postContainer = (ListView) v.findViewById(R.id.zonePostList);
        loadingSpinner = (ProgressBar) v.findViewById(R.id.initialLoader);
        newPostButton = (FloatingActionButton) v.findViewById(R.id.newPostButton);
        newPostButton.setOnClickListener(this);
        initDialogs();
        thisZoneController.getZonePosts();

        return v;
    }

    @Override
    public void getZonePosts(List<Post> posts) {
        //not functional yet
        loadingSpinner.setVisibility(View.GONE);
        postContainer.setAdapter(new ThisZoneListAdapter(posts));
    }

    @Override
    public void postSubmitted() {
        loading.dismiss();
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
                        initDialogs();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                        initDialogs();
                    }
                })
                .build();

        loading = new MaterialDialog.Builder(context)
                .title("Publishing")
                .customView(R.layout.load_dialog,true)
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

}
