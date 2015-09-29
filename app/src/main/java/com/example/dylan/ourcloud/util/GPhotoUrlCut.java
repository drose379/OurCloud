package com.example.dylan.ourcloud.util;

import android.content.res.Resources;

/**
 * Created by dylan on 9/28/15.
 */
public class GPhotoUrlCut {

    public static String getImageSized(String userImageUrl, int size) {
        Resources r = Resources.getSystem();
        String fullUrl = userImageUrl;
        String[] splitUrl = fullUrl.split("\\=");

        return splitUrl[0] + "=" + String.valueOf( size * Math.round(r.getDisplayMetrics().density) );
    }

}
