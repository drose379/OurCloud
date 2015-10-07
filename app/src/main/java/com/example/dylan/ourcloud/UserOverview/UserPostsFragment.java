package com.example.dylan.ourcloud.UserOverview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.ViewedPost;

import java.util.List;

/**
 * Created by dylan on 10/6/15.
 */
public class UserPostsFragment extends Fragment implements GrabUserPosts.Callback  {

    private ListView postOverviewList;



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
        for (ViewedPost post : posts) {
            Log.i("viewers",post.getPostText() + "Viewers " + post.getViews().toString());
        }
    }


}
