package com.example.dylan.ourcloud.live_zone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.R;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dylan on 9/12/15.
 */
public class LiveUserListAdapter extends BaseAdapter {

    private Context context;
    private List<User> users;

    public LiveUserListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    public int getCount() {
        return users.size();
    }
    public long getItemId(int item) {
        return 0;
    }
    public User getItem(int item) {
        return users.get(item);
    }

    public void updateUsers(List<User> users) {
        this.users = null;
        this.users = users;
        notifyDataSetChanged();
    }

    public View getView(int item,View recycledView,ViewGroup parent) {
        View v = recycledView;

        TextView userName;
        CircleImageView userImage;

        User user = users.get(item);

        v = v == null ? LayoutInflater.from(context).inflate(R.layout.user_card,parent,false) : v;

        userName = (TextView) v.findViewById(R.id.userName);
        userImage = (CircleImageView) v.findViewById(R.id.userImage);

        userName.setText(user.getName().equals(LocalUser.getInstance(context).getItem(LocalUserDBHelper.nameCol)) ? "Me" : user.getName());
        Picasso.with(context).load(user.getPhotoUrl()).into(userImage);

        return v;
    }

}
