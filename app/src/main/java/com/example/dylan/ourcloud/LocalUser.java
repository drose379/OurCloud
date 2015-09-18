package com.example.dylan.ourcloud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by dylan on 9/18/15.
 */
public class LocalUser {

    /**
     * This class is used to communicate with the Local database containing the signed in users info
     * Communicates with LocalUserDBHelper to insert user data into DB, also to read data from DB
     *
     * When user signs in, get the userID, if the ID already exists in the DB, do nothing
        * If the userId does not exist in local DB, add a column with all the users info
        *
     *
     *
     * Getter methods for:
        * In getter methods, get readableDatabase from LocalDBHelper and get the corresponding value, return it
        * Only ever have 1 user saved in Local DB at once
     *
     * String wifiId
     * List<String> networksInRange (May have to change to JSONArray for easy .toString() format
     * String zoneId
     * String zoneName
     * String displayName
     * String profileImageUrl (also method to get the sized version, resize in the getter with regex cutting off the last number and  replacing with size
     * String getId (google userID for user)
     *
     * Setter methods for setting the wifiZone and the networksInRange, those are added after the user signs in, so need setters, UPDATE query for current row
     */

    private static LocalUser userInfoController = null;
    private LocalUserDBHelper userDBHelper;

    public static LocalUser getInstance(Context context) {
        userInfoController = userInfoController == null ? new LocalUser(context) : userInfoController;
        return userInfoController;
    }

    public LocalUser(Context context) {
        userDBHelper = new LocalUserDBHelper(context);
    }

    public void userSignIn(Person person) {
        /**
         * Start by deleting all other rows from the DB, no need to store multiple users info at once, only one user will be signed in at a time
         * Then, insert all the persons data into a db row, each row name should correspond to the item name
         */
        userDBHelper.getWritableDatabase().delete(LocalUserDBHelper.tableName,null,null);
        ContentValues userVals = new ContentValues();

        userVals.put("id",person.getId());
        userVals.put("name",person.getDisplayName());
        userVals.put("profile_image",person.getImage().getUrl());

        userDBHelper.getWritableDatabase().insert(LocalUserDBHelper.tableName, null, userVals);
    }

    public void setWifiId(String wifiId) {
        ContentValues vals = new ContentValues();
        vals.put(LocalUserDBHelper.wifi_id_col,wifiId);
        userDBHelper.getWritableDatabase().update(LocalUserDBHelper.tableName, vals, null, null);
    }

    public void setNetworksInRange(List<String> networksInRange) {
        JSONArray networks = new JSONArray();
        ContentValues vals = new ContentValues();

        for (String network : networksInRange) {
            networks.put(network);
        }


        vals.put(LocalUserDBHelper.networks_in_range_col,networks.toString());
        userDBHelper.getWritableDatabase().update(LocalUserDBHelper.tableName,vals,null,null);
    }

    public void setZoneId(String zoneId) {
        ContentValues vals = new ContentValues();
        vals.put(LocalUserDBHelper.zone_id_col,zoneId);

        userDBHelper.getWritableDatabase().update(LocalUserDBHelper.tableName, vals,null,null);
    }
    public void setZoneName(String zoneName) {
        ContentValues vals = new ContentValues();
        vals.put(LocalUserDBHelper.zone_name_col, zoneName);

        userDBHelper.getWritableDatabase().update(LocalUserDBHelper.tableName,vals,null,null);
    }


    public String getItem(String column) {
        Cursor result = userDBHelper.getReadableDatabase().rawQuery("SELECT * FROM user",null);
        result.moveToFirst();
        int nameCol = result.getColumnIndex(column);
        return result.getString(nameCol);
    }
    public JSONArray getNetworksInRange() {
        JSONArray networks = null;
        Cursor result = userDBHelper.getReadableDatabase().rawQuery("SELECT * FROM user",null);
        result.moveToFirst();
        int networksCol = result.getColumnIndex(LocalUserDBHelper.networks_in_range_col);

        try {
            networks = new JSONArray(result.getString(networksCol));
        } catch (JSONException e) {e.printStackTrace();}

        return networks;
    }

    public String getProfilePhotoSized(int size) {
        Cursor result = userDBHelper.getReadableDatabase().rawQuery("SELECT * FROM user",null);
        result.moveToFirst();
        int photoCol = result.getColumnIndex(LocalUserDBHelper.profile_image_col);
        String splitUrl = result.getString(photoCol).split("\\=")[0];
        return splitUrl + "=" + String.valueOf(size);
    }

    /**
     * Getter methods below here.. use userDBHelper.getReadableDb().raqQeury() to grab item
     */

}
