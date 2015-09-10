package com.example.dylan.ourcloud.hometabs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.live_zone.ZoneList;
import com.example.dylan.ourcloud.post_detail.PostDetailView;
import com.example.dylan.ourcloud.util.ImageUtil;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.PostComposeActivity;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.UserInfo;
import com.example.dylan.ourcloud.WifiController;

import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dylan on 8/6/15.
 */
public class ThisZone extends Fragment implements View.OnClickListener,ListView.OnScrollListener,AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,ThisZoneController.Callback,
    ImageUtil.ImageCallback{

    private Context context;
    private WifiController wifiController;
    private ThisZoneController thisZoneController;

    Intent newPostTempData;

    SwipeRefreshLayout refreshLayout;
    DrawerLayout menuLayout;
    ListView menuOptionsList;
    FloatingActionButton newPostButton;
    ProgressBar loadingSpinner;
    ListView postContainer;
    TextView noPostsText;
    TextView toolbarTitle;

    boolean dialogsInflated;
    MaterialDialog newPost;
    MaterialDialog enableWifi;
    MaterialDialog newZoneName;
    MaterialDialog mainOptionsMenu;

    int previousVisibleItem;

    String[] menuItems = new String[] {"New Post","People Here","Chat"};

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
        toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbarTitle);
        if(!dialogsInflated) {initDialogs();}

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
        menuLayout = (DrawerLayout) v.findViewById(R.id.menuItems);

        menuOptionsList = (ListView) v.findViewById(R.id.menuOptions);

        menuOptionsList.setAdapter(new ArrayAdapter<String>(context, R.layout.nav_item, menuItems));
        menuOptionsList.setOnItemClickListener(this);

        refreshLayout.setOnRefreshListener(this);
        newPostButton.setOnClickListener(this);

        refreshLayout.setColorSchemeColors(R.color.indicator, R.color.ColorPrimary, R.color.ColorPrimaryDark);

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

    @Override
    public void onStop() {
        super.onStop();
    }

    public void initWifiConnect() {
        if (wifiController.isConnected()) {
            UserInfo.getInstance().setWifiId(wifiController.getWifiId());
            UserInfo.getInstance().setNetworksInRange(wifiController.getNetworksInRange());

            thisZoneController.getZoneId(UserInfo.getInstance().getWifiSSID(), UserInfo.getInstance().getNetworksInRange());
        } else {
            //enableWifi.show();
            //For testing with emulator
            UserInfo.getInstance().setWifiId("UNH-Secure");
            UserInfo.getInstance().setNetworksInRange(Arrays.asList("UNH-Public"));
            thisZoneController.getZoneId(UserInfo.getInstance().getWifiSSID(), UserInfo.getInstance().getNetworksInRange());
        }
    }

    /**
     * postContainer scroll listener needs to be updated each time data changes
     * @param posts
     */
    @Override
    public void getZonePosts(final List<Post> posts) {
        postContainer.setVisibility(View.VISIBLE);
        loadingSpinner.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
        if (posts.size() > 0) {
            postContainer.setVisibility(View.VISIBLE);
            postContainer.setAdapter(new ThisZoneListAdapter(context, posts));
            noPostsText.setVisibility(View.GONE);
        }
        else {
            noPostsText.setVisibility(View.VISIBLE);
            postContainer.setVisibility(View.GONE);
        }

        postContainer.setOnScrollListener(this);
        postContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailActivity = new Intent(context,PostDetailView.class);
                detailActivity.putExtra("selectedPost",posts.get(i));
                startActivity(detailActivity);
            }
        });

    }

    @Override
    public void getZoneId(String zoneId,String zoneName) {

        UserInfo.getInstance().setZoneId(zoneId);
        UserInfo.getInstance().setZoneName(zoneName);

        if(UserInfo.getInstance().getZoneName() == null) {
            newZoneName.show();
        } else {
            toolbarTitle.setText(zoneName);
            thisZoneController.grabZonePosts();
        }

    }

    @Override
    public void postSubmitted() {

        thisZoneController.grabZonePosts();
        //refresh feed
    }

    public void initDialogs() {

        dialogsInflated = true;

        enableWifi = new MaterialDialog.Builder(context)
                .title("Enable Wifi")
                .content("Please enable wifi to post in a zone, press reload when connected. If you want to check your marked zones, click skip")
                .positiveText("Reload")
                .negativeText("Skip")
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Log.i("onDismiss", "Dismiss called");
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

        final View newZoneView = LayoutInflater.from(context).inflate(R.layout.new_zone_name, null);
        newZoneName = new MaterialDialog.Builder(context)
                .title("Please Name This Zone")
                .customView(newZoneView, true)

                .positiveText("Save")
                .positiveColor(getResources().getColor(R.color.ColorPrimary))
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //need to make sure name is set, if not, re inflate the dialog
                        EditText zoneNameArea = (EditText) newZoneView.findViewById(R.id.zoneNameArea);
                        String zoneName = zoneNameArea.getText().toString();
                        if (zoneName.isEmpty()) {
                            newZoneName.show();
                        } else {
                            //submit the zone name, wait for callback, in that callback, assign the zone name to UserInfo, and call .getPosts();
                            thisZoneController.createZoneName(UserInfo.getInstance().getZoneId(), zoneName);
                        }
                    }
                })
                .build();


    }

    @Override
    public void zoneNameReady(String zoneName) {
        UserInfo.getInstance().setZoneName(zoneName);
        toolbarTitle.setText(zoneName);
        thisZoneController.grabZonePosts();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {

        //save post type and send it to the PostDetailView to decide menu items for nav

        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case PostComposeActivity.POST_TEXT_ONLY :
                String postText = data.getStringExtra("postText");
                long expirationDate = data.getLongExtra("expDate", 0);
                thisZoneController.newPost(postText,expirationDate);
                break;

            case PostComposeActivity.POST_BOTH:
                newPostTempData = data;
                ImageUtil.getInstance().uploadImage(this,PostComposeActivity.POST_BOTH,(File)data.getSerializableExtra("postImage"));
                break;

            case PostComposeActivity.POST_PHOTO_ONLY:
                newPostTempData = data;
                ImageUtil.getInstance().uploadImage(this,PostComposeActivity.POST_PHOTO_ONLY,(File)data.getSerializableExtra("postImage"));
                break;
        }

    }

    @Override
    public void imageUploaded(int status,String url) {

        long expirationDate = newPostTempData.getLongExtra("expDate",0);

        switch (status) {
            case PostComposeActivity.POST_BOTH :
                String postText = newPostTempData.getStringExtra("postText");
                thisZoneController.newPostWithImage(postText, url, expirationDate);
                break;
            case PostComposeActivity.POST_PHOTO_ONLY :
                thisZoneController.newPostWithImage("", url, expirationDate);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.newPostButton:
                menuOptionsList.bringToFront();
                menuLayout.requestLayout();
                menuLayout.openDrawer(Gravity.LEFT);

                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> list,View parent,int position,long id) {
        Intent i;
        menuLayout.closeDrawer(Gravity.LEFT);
        switch (position) {
            case 0:
                 i = new Intent(context, PostComposeActivity.class);
                 startActivityForResult(i, 1);
                break;
            case 1:
                i = new Intent(context,ZoneList.class);
                startActivity(i);
                break;
            case 2:
                //chat
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount) {
        int topViewMargin = postContainer.getChildAt(0) != null ? postContainer.getChildAt(0).getTop() : 0;
        refreshLayout.setEnabled(firstVisibleItem == 0 && topViewMargin == 0);


        if (firstVisibleItem > previousVisibleItem) {newPostButton.hide(true);} else if (firstVisibleItem < previousVisibleItem) {newPostButton.show(true);}

        previousVisibleItem = firstVisibleItem;

    }

    @Override
    public void onScrollStateChanged(AbsListView view,int scrollState) {}

    @Override
    public void onRefresh() {
        this.onStart();
    }

}
