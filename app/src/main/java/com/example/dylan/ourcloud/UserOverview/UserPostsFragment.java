package com.example.dylan.ourcloud.UserOverview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dylan.ourcloud.R;

/**
 * Created by dylan on 10/6/15.
 */
public class UserPostsFragment extends Fragment {

    public void onAttach( Activity activity ) {
        super.onAttach( activity );
    }

    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onCreateView( inflater, container, savedInstance );

        View v = inflater.inflate( R.layout.user_post_overview, container, false );


        return v;
    }

    public void onStart() {
        super.onStart();
    }


}
