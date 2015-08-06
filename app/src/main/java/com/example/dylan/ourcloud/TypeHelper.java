package com.example.dylan.ourcloud;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by drose379 on 6/12/15.
 */
public class TypeHelper {

    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(),"sourceSans_reg.ttf");
    }
    public static Typeface getTypefaceBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(),"ssp_semiBold.ttf");
    }

}
