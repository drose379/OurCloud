package com.example.dylan.ourcloud;

import org.json.JSONArray;

/**
 * Created by dylan on 8/8/15.
 */
public class JSONUtil {

    public static String generateJSONArray(String... items) {
        JSONArray vals = new JSONArray();
        for (String val : items) {
            vals.put(val);
        }
        return vals.toString();
    }

}
