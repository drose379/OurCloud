package com.example.dylan.ourcloud.UserOverview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;

import java.util.List;

/**
 * Created by dylan on 10/7/15.
 */
public class ViewListAdapter extends BaseAdapter {

    private Context context;
    private List<String> users;

    public ViewListAdapter( Context context, List<String> users ) {
        this.context = context;
        this.users = users;
    }

    public String getItem( int item ) {
        return users.get( item );
    }

    public int getCount() {
        return users.size();
    }

    public long getItemId( int item ) {
        return 0;
    }

    public View getView( int item, View recycledView, ViewGroup parent ) {
        View v = recycledView == null ? LayoutInflater.from( context ).inflate( R.layout.user_viewed_card, parent, false ) : recycledView ;

        TextView usersName = (TextView) v.findViewById( R.id.name );

        usersName.setText( users.get( item ) );

        return v;
    }

}
