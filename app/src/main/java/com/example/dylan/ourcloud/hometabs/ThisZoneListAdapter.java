package com.example.dylan.ourcloud.hometabs;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.dylan.ourcloud.Post;

import java.util.List;

/**
 * Created by dylan on 8/6/15.
 */
public class ThisZoneListAdapter extends BaseAdapter {

    private List<Post> posts;

    public ThisZoneListAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public long getItemId(int item) {
        return 0;
    }

    @Override
    public Post getItem(int item) {
        return posts.get(item);
    }

    @Override
    public View getView(int position,View recycledView,ViewGroup parent) {
        View v  = recycledView;
        if (v == null) {
            //create card, assign values
        }
        return v;
    }
}
