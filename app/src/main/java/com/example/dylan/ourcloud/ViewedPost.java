package com.example.dylan.ourcloud;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 10/7/15.
 */
public class ViewedPost extends Post {

    private String views;

    public ViewedPost( String currentUser ) {
        super( currentUser );
    }

    public void setViews( String viewsJSON ) {
        views = viewsJSON;
    }

    public List<String> getViews() {
        JSONArray views;
        List<String> viewers = new ArrayList<String>();

        try {
            views = new JSONArray( this.views );

            for (int i = 0;i < views.length();i++) {
                viewers.add( views.getString( i ) );
            }

        } catch ( JSONException e) {
            throw new RuntimeException( e.getMessage() );
        }

        return viewers;
    }

}
