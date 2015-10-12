package com.example.dylan.ourcloud.home_zone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Network;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.LocalUserDashboard;
import com.example.dylan.ourcloud.MainActivity;
import com.example.dylan.ourcloud.MarkedZoneDashboard;
import com.example.dylan.ourcloud.NavDrawerAdapter;
import com.example.dylan.ourcloud.NetworkListenerActivity;
import com.example.dylan.ourcloud.PostListenerActivity;
import com.example.dylan.ourcloud.TypeHelper;
import com.example.dylan.ourcloud.live_zone.ExitLiveUser;
import com.example.dylan.ourcloud.live_zone.LiveUsers;
import com.example.dylan.ourcloud.live_zone.NewLiveUser;
import com.example.dylan.ourcloud.live_zone.PrivateMessagesOverview;
import com.example.dylan.ourcloud.live_zone.WifiStateListener;
import com.example.dylan.ourcloud.live_zone.ZoneUserList;
import com.example.dylan.ourcloud.post_detail.PostDetailView;
import com.example.dylan.ourcloud.util.ImageUtil;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.PostComposeActivity;
import com.example.dylan.ourcloud.R;

import com.example.dylan.ourcloud.WifiController;

import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dylan on 8/6/15.
 */
public class ThisZone extends PostListenerActivity implements View.OnClickListener,ListView.OnScrollListener,AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,ThisZoneController.Callback,
    ImageUtil.ImageCallback{

    private WifiController wifiController;
    private ThisZoneController thisZoneController;
    private LocalUser localUser;

    Intent newPostTempData;

    SwipeRefreshLayout refreshLayout;
    DrawerLayout menuLayout;
    ListView menuOptionsList;
    FloatingActionButton newPostButton;
    ProgressBar loadingSpinner;
    ListView postContainer;
    TextView noPostsText;
    TextView toolbarTitle;
    ImageView menuButton;

    boolean dialogsInflated;
    MaterialDialog newZoneName;
    MaterialDialog exitZone;

    int previousVisibleItem;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.this_zone);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        menuButton = (ImageView) toolbar.findViewById(R.id.toolbarMenuButton);
        /**
         * Need a controller to grab this zones posts and have callback to this fragment.
         * Show each post in a card
         * Have fab for user to create a post
         * Swipe down to refresh (call controller method again)
         * INFLATE MATERIAL DIALOG TO ADD NEW POST
         * Whenever user refreshes feed or attempts to add a new post, check WifiController.isConnected() method and show dialog if not connected or zone changed.
         */

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshContainer);
        postContainer = (ListView) findViewById(R.id.zonePostList);
        noPostsText = (TextView) findViewById(R.id.noPosts);
        //loadingSpinner = (ProgressBar) findViewById(R.id.initialLoader);
        newPostButton = (FloatingActionButton) findViewById(R.id.newPostButton);
        menuLayout = (DrawerLayout) findViewById(R.id.menuItems);
        menuOptionsList = (ListView) findViewById(R.id.menuOptions);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(R.color.indicator, R.color.ColorPrimary, R.color.ColorPrimaryDark);

        menuButton.setOnClickListener(this);
        newPostButton.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        wifiController = wifiController == null ? WifiController.getInstance(this) : wifiController;
        thisZoneController = thisZoneController == null ? new ThisZoneController(this) : thisZoneController;
        localUser = LocalUser.getInstance(this);
        LiveUsers.appActive = true;
        initNavMenu();
        initDialogs();
    }

    @Override
    public void onResume() {
        super.onResume();
        initWifiConnect();

    }

    public void initWifiConnect() {
        /** For testing, setting Zone statically
         */
        LocalUser localUser = LocalUser.getInstance(this);
        if (wifiController.isConnected()) {
            localUser.setWifiId(wifiController.getWifiId());
            localUser.setNetworksInRange(wifiController.getNetworksInRange());

            thisZoneController.getZoneId(localUser.getItem(LocalUserDBHelper.wifi_id_col), localUser.getNetworksInRange());
        }
/**

        //Testing
        localUser.setWifiId("UNH-Secure");
        localUser.setNetworksInRange(Arrays.asList("UNH-Public"));
        thisZoneController.getZoneId(localUser.getItem(LocalUserDBHelper.wifi_id_col), localUser.getNetworksInRange());
*/
    }

    /**
     * postContainer scroll listener needs to be updated each time data changes
     * @param posts
     */
    @Override
    public void getZonePosts(final List<Post> posts) {
        postContainer.setVisibility(View.VISIBLE);
        //loadingSpinner.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
        if (posts.size() > 0) {
            postContainer.setVisibility(View.VISIBLE);
            postContainer.setAdapter(new ThisZoneListAdapter(this, posts));
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
                Intent detailActivity = new Intent(ThisZone.this, PostDetailView.class);
                detailActivity.putExtra("selectedPost", posts.get(i));
                startActivity(detailActivity);
            }
        });
    }

    @Override
    public void getZoneId(String zoneId,final String zoneName) {

        localUser.setZoneId(zoneId);
        localUser.setZoneName(zoneName);

        if(localUser.getItem(LocalUserDBHelper.zone_name_col).equals("null")) {
            newZoneName.show();
        } else {

            Intent enterLiveUser = new Intent(this,NewLiveUser.class);
            startService(enterLiveUser);

            toolbarTitle.setText(zoneName);
            thisZoneController.grabZonePosts();

        }

    }

    @Override
    public void postSubmitted() {

        thisZoneController.grabZonePosts();
        //refresh feed
    }

    @Override
    public void newPostReceived() {
        thisZoneController.grabZonePosts();
    }

    public void initDialogs() {

        dialogsInflated = true;

        final View newZoneView = LayoutInflater.from(this).inflate(R.layout.new_zone_name, null);
        newZoneName = new MaterialDialog.Builder(this)
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
                            thisZoneController.createZoneName(localUser.getItem(LocalUserDBHelper.zone_id_col), zoneName);
                        }
                    }
                })
                .build();


        exitZone = new MaterialDialog.Builder(ThisZone.this)
                .title("You left the zone!")
                .positiveText("Exit")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        ThisZone.this.finish();
                    }
                })
                .build();

    }

    public void initNavMenu() {
        List<MenuOption> menuOptions = new ArrayList<MenuOption>();

        menuOptions.add(new MenuOption()
                        .setTitle(localUser.getItem(LocalUserDBHelper.nameCol))
                        .setImage(localUser.getProfilePhotoSized(80))
                        .setType(3)
        );
        menuOptions.add(new MenuOption()
                        .setTitle("People Here")
                        .setType(1)
        );
        menuOptions.add(new MenuOption()
                        .setTitle("Chat")
                        .setType(1)
        );
        menuOptions.add(new MenuOption()
                        .setTitle("Marked Zones")
                        .setType(1)
        );
        menuOptions.add(new MenuOption()
                        .setTitle("Exit")
                        .setType(1)
        );

        menuOptionsList.setAdapter(new NavDrawerAdapter(this,menuOptions));
        menuOptionsList.setOnItemClickListener(this);
    }

    @Override
    public void zoneNameReady(String zoneName) {
        localUser.setZoneName(zoneName);
        toolbarTitle.setText(zoneName);
        thisZoneController.grabZonePosts();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case PostComposeActivity.POST_TEXT_ONLY :
                String postText = data.getStringExtra("postText");
                long expirationDate = data.getLongExtra("expDate", 0);
                thisZoneController.newPost(postText,expirationDate);
                break;

            case PostComposeActivity.POST_BOTH:
                newPostTempData = data;
                ImageUtil.getInstance(this).uploadImage(this,PostComposeActivity.POST_BOTH,(File)data.getSerializableExtra("postImage"));
                break;

            case PostComposeActivity.POST_PHOTO_ONLY:
                newPostTempData = data;
                ImageUtil.getInstance(this).uploadImage(this,PostComposeActivity.POST_PHOTO_ONLY,(File)data.getSerializableExtra("postImage"));
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
                Intent i = new Intent(this, PostComposeActivity.class);
                startActivityForResult(i, 1);
                break;
            case R.id.toolbarMenuButton :
                menuOptionsList.bringToFront();
                menuLayout.requestLayout();
                if (menuLayout.isDrawerOpen(Gravity.LEFT)) {
                    menuLayout.closeDrawer(Gravity.LEFT);
                } else {
                    menuLayout.openDrawer(Gravity.LEFT);
                }

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> list,View parent,int position,long id) {
        Intent i;
        menuLayout.closeDrawer(Gravity.LEFT);
        switch (position) {
            case 0:
                i = new Intent( this, LocalUserDashboard.class);
                startActivity( i );
                break;
            case 1:
                i = new Intent(this, ZoneUserList.class);
                startActivity(i);
                break;
            case 2:
                Intent chatThreads = new Intent( this, PrivateMessagesOverview.class);
                startActivity(chatThreads);
                break;
            case 3:
                i = new Intent( this, MarkedZoneDashboard.class );
                startActivity( i );
                break;
            case 4:
                Intent exit = new Intent(this,ExitLiveUser.class);
                LiveUsers.appActive = false;
                startService(exit);
                this.finish();
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
        initWifiConnect();
    }

}
