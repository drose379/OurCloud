package com.example.dylan.ourcloud.UserOverview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.ViewedPost;

import java.util.List;

/**
 * Created by dylan on 10/6/15.
 */
public class UserPostsFragment extends Fragment implements GrabUserPosts.Callback, AdapterView.OnItemClickListener  {

    private ListView postOverviewList;
    private List<ViewedPost> posts;


    public void onAttach( Activity activity ) {
        super.onAttach( activity );
    }

    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onCreateView( inflater, container, savedInstance );

        View v = inflater.inflate( R.layout.user_post_overview, container, false );

        postOverviewList = (ListView) v.findViewById( R.id.userOverviewPosts );

        return v;
    }

    public void onStart() {
        super.onStart();
        new GrabUserPosts( this ).execute(LocalUser.getInstance( getActivity() ).getItem(LocalUserDBHelper.user_id_col ) );
    }

    @Override
    public void getPosts( List<ViewedPost> posts ) {
        //populate the list with these posts
        this.posts = posts;
        UserPostsAdapter adapter = new UserPostsAdapter( posts, getActivity() );
        postOverviewList.setAdapter(adapter);
        postOverviewList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick( AdapterView list, View view, int position, long id ) {
        ViewedPost post = posts.get( position );

        View v = LayoutInflater.from( getActivity() ).inflate( R.layout.users_viewed_list, null, false );
        ListView userList = (ListView) v.findViewById( R.id.usersViewedList );
        userList.setAdapter( new ViewListAdapter( getActivity(), post.getViews() ) );

        MaterialDialog dialog = new MaterialDialog.Builder( getActivity() )
                .title("Users Who Have Viewed")
                .customView( v, true )
                .build();

        dialog.show();
    }


}
