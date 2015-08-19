package com.example.dylan.ourcloud;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.plus.model.people.Person;

/**
 * Created by dylan on 8/6/15.
 */
public class UserInfo {

    /**
     * Once twitter login is implemented, UserInfo needs to hold a reference to the login method, TWITTER or GOOGLE
     */

    private Person person;
    private String wifiId;
    private boolean inDatabase;

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
    public void setInDatabase(boolean exists) {inDatabase = exists;}

    public Person getPerson() {
        return person;
    }
    public String getDisplayName() {return person.getDisplayName();}
    public String getWifiId() {
        return wifiId;
    }
    public String getProfileImage() {
        return person.getImage().getUrl();
    }
    public String getProfileImageSized(int size) {
        String fullUrl = person.getImage().getUrl();
        String[] splitUrl = fullUrl.split("\\=");

        return splitUrl[0] + "=" + String.valueOf(size);
    }
    public String getId() {
        return person.getId();
    }
    public boolean getInDatabase() {return inDatabase;}

}
