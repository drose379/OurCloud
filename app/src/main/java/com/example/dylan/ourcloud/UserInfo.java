package com.example.dylan.ourcloud;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.plus.model.people.Person;

/**
 * Created by dylan on 8/6/15.
 */
public class UserInfo {

    /**
     * Instead of all static, create singleton and use throughout application
     */

    private Person person;
    private String wifiId;

    private static UserInfo userInfo;

    public static UserInfo getInstance() {
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    public void setPerson(Person currentPerson) {person = currentPerson;}
    public void setWifiId(String id) {
        wifiId = id;
    }

    public Person getPerson() {
        return person;
    }
    public String getWifiId() {
        return wifiId;
    }

}
