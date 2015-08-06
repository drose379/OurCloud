package com.example.dylan.ourcloud;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.plus.model.people.Person;

/**
 * Created by dylan on 8/6/15.
 */
public class UserInfo {

    private static Person person;
    private static String wifiId;

    public static void setPerson(Person currentPerson) {person = currentPerson;}
    public static void setWifiId(String id) {
        wifiId = id;
    }

    public static Person getPerson() {
        return person;
    }
    public static String getWifiId() {
        return wifiId;
    }

}
