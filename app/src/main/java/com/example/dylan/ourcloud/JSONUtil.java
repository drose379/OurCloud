package com.example.dylan.ourcloud;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylan on 8/8/15.
 */
public class JSONUtil {

    /**
     * Class for util methods that receive json and parse it into something usable
     * @param items
     * @return
     */

    public static String generateJSONArray(String... items) {
        JSONArray vals = new JSONArray();
        for (String val : items) {
            vals.put(val);
        }
        return vals.toString();
    }

    public static List<Post> toPostList(String postsJson) {
        List<Post> posts = new ArrayList<Post>();

        try {
            JSONArray postsParent = new JSONArray(postsJson);

            for(int i = 0;i<postsParent.length();i++) {
                JSONObject currentObject = postsParent.getJSONObject(i);
                Post currentPost = new Post()
                        .setUser(currentObject.getString("user"))
                        .setUserImage(currentObject.getString("user_photo"))
                        .setPostText(currentObject.getString("postText"))
                        .setPostImage(currentObject.getString("postImage"));
                posts.add(currentPost);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }
        return posts;
    }

}
