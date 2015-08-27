package com.example.dylan.ourcloud.post_detail;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.TypeHelper;

/**
 * Created by dylan on 8/27/15.
 */
public class PostTextFrag extends Fragment {

    private Context context;
    private Post post;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        post = (Post) getArguments().getSerializable("postInfo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstance) {
        super.onCreateView(inflater,container,savedInstance);
        View v = inflater.inflate(R.layout.post_detail_text,container,false);

        TextView postText = (TextView) v.findViewById(R.id.postText);
        postText.setText(post.getPostText());


        return v;
    }


}
