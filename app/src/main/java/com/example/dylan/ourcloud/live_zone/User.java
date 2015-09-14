package com.example.dylan.ourcloud.live_zone;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dylan on 9/12/15.
 */
public class User implements Parcelable {

    private String id;
    private String name;
    private String photoUrl;

    public User() {

    }

    public User(Parcel in) {
        String[] items = {};
        in.readStringArray(items);
        id = items[0];
        name = items[1];
        photoUrl = items[2];
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out,int flage) {
        String[] items = {id,name,photoUrl};
        out.writeStringArray(items);
    }


    public User setId(String id) {
        this.id = id;
        return this;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }
    public User setImage(String imageUrl) {
        photoUrl = imageUrl;
        return this;
    }

    public String getId() {return id;}
    public String getName() {
        return name;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }

}
