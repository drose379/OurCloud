package com.example.dylan.ourcloud.home_zone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;
import com.google.android.gms.maps.model.Circle;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dylan on 9/12/15.
 */
public class HomeNavDrawerAdapter extends BaseAdapter {

    private Context context;
    private List<MenuOption> menuItems;

    public HomeNavDrawerAdapter(Context context,List<MenuOption> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    public int getCount() {
        return menuItems.size();
    }
    public long getItemId(int item) {return 0;}
    public MenuOption getItem(int item) {return menuItems.get(item);}

    public View getView(int item,View recycledView,ViewGroup parent) {
        MenuOption menuOption = menuItems.get(item);
        View v = recycledView;

        TextView optionText;
        ImageView optionIcon;
        CircleImageView userIcon;

        // Need to set the menu item layout according to its type
        switch(menuOption.getType()) {
            case 1 :
                //just text

                v = LayoutInflater.from(context).inflate(R.layout.menu_option_1,parent,false);
                optionText = (TextView) v.findViewById(R.id.optionText);
                optionText.setText(menuOption.getTitle());

                break;
            case 2 :
                //text and icon to the left
                break;
            case 3:
                //header item with user image at top and name below (with padding)
                v = LayoutInflater.from(context).inflate(R.layout.menu_option_3, parent, false);
                userIcon = (CircleImageView) v.findViewById(R.id.userImage);
                optionText = (TextView) v.findViewById(R.id.userText);

                Picasso.with(context).load(menuOption.getImageUrl()).into(userIcon);
                optionText.setText(menuOption.getTitle());
                break;
        }
        return v;
    }


}
