package com.example.dylan.ourcloud;

import android.content.ContentValues;
import android.content.Context;

import com.example.dylan.ourcloud.live_zone.MessagesDBHelper;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by dylan on 9/18/15.
 */
public class LocalUserInfoController {

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
     *
     */

    private static LocalUserInfoController userInfoController = null;
    private LocalUserDBHelper userDBHelper;

    public static LocalUserInfoController getInstance(Context context) {
        userInfoController = userInfoController == null ? new LocalUserInfoController(context) : userInfoController;
        return userInfoController;
    }

    public LocalUserInfoController(Context context) {
        userDBHelper = new LocalUserDBHelper(context);
    }

    public void userSignIn(Person person) {
        /**
         * Start by deleting all other rows from the DB, no need to store multiple users info at once, only one user will be signed in at a time
         * Then, insert all the persons data into a db row, each row name should correspond to the item name
         */
        userDBHelper.getWritableDatabase().delete(LocalUserDBHelper.tableName,null,null);
        ContentValues userVals = new ContentValues();
        //user person.getX() for values
        userVals.put();
        userVals.put();
        userVals.put();
        userVals.put();
        userVals.put();
        userDBHelper.getWritableDatabase().insert(LocalUserDBHelper.tableName,null,userVals);
    }

}
