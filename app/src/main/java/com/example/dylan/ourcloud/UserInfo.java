package com.example.dylan.ourcloud;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by dylan on 8/6/15.
 */
public class UserInfo {

    /**
     * Once twitter login is implemented, UserInfo needs to hold a reference to the login method, TWITTER or GOOGLE
     */

    private Person person;
    private String wifiId;
    private List<String> networksInRange;
    private boolean inDatabase;
    private String zoneId;
    private String zoneName;

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
    public void setNetworksInRange(List<String> networks) {networksInRange = networks;}
    public void setInDatabase(boolean exists) {inDatabase = exists;}
    public void setZoneId(String zoneId) {this.zoneId = zoneId;}
    public void setZoneName(String zoneName) {this.zoneName = zoneName.equals("null") ? null : zoneName;}

    public Person getPerson() {
        return person;
    }
    public String getDisplayName() {return person.getDisplayName();}
    public String getWifiSSID() {
        return wifiId.replace("\"","");
    }
    public JSONArray getNetworksInRange() {
        //this.getWifiID has quotes around it, need to replace quotes with blank in the current wifiid and then loop to remove it from list
        //just strip the quotes in the wifiController method that sets the wifiId here
        JSONArray networks = new JSONArray();
        for(String network : networksInRange) {
            networks.put(network);
        }
        return networks;
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
    public String getZoneId() {return zoneId;}
    public String getZoneName() {return zoneName;}

}
