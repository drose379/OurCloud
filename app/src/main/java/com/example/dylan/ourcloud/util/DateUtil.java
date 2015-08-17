package com.example.dylan.ourcloud.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dylan on 8/16/15.
 */
public class DateUtil {
    public static String currentDate(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        Date currentDate = calendar.getTime();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("M/d/y @ h:ma",Locale.US);

        return dateFormatter.format(currentDate);
    }
}
