package com.example.dylan.ourcloud;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;

public class CustomTextView extends TextView {

    private Context context;

    public CustomTextView(Context context) {
        super(context,null);
        this.context = context;
        Typeface customFont = Typeface.createFromAsset(context.getAssets(),"sourceSans_reg.ttf");
        this.setTypeface(customFont);
    }

    public CustomTextView(Context context,AttributeSet attrs) {
        super(context,attrs);

        TypedArray attributeInfo = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        int attr = attributeInfo.getIndex(0);
        boolean  boldType = attributeInfo.getBoolean(attr,false);

        Typeface customFont;

        if (boldType) {
            customFont = Typeface.createFromAsset(context.getAssets(), "ssp_semiBold.ttf");
        } else {
            customFont = Typeface.createFromAsset(context.getAssets(), "sourceSans_reg.ttf");
        }

        this.setTypeface(customFont);
    }




}
