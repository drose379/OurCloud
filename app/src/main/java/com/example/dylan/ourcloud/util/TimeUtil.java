package com.example.dylan.ourcloud.util;

import java.util.Calendar;

/**
 * Created by dylan on 9/1/15.
 */
public class TimeUtil {

    public static long getCurrentTimeMillis() {
        Calendar currentCal = Calendar.getInstance();
        return currentCal.getTimeInMillis();
    }

}
