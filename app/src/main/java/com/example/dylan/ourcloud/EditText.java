package com.example.dylan.ourcloud;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;


/**
 * Created by dylan on 9/16/15.
 */
public class EditText extends android.widget.EditText {

    public EditText(Context context) {
        super(context);
    }
    public EditText(Context context, AttributeSet attrs) {super(context,attrs);}
    public EditText(Context context, AttributeSet attrs, int style) {super(context,attrs,style);}

    public void clear() {
        this.setText("");
    }

    public boolean isEmpty() {
        return getText().toString().length() == 0;
    }

}
