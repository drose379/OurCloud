package com.example.dylan.ourcloud.live_zone;

/**
 * Created by dylan on 9/12/15.
 */
public class User {

    private String name;
    private String photoUrl;

    public User setName(String name) {
        this.name = name;
        return this;
    }
    public User setImage(String imageUrl) {
        photoUrl = imageUrl;
        return this;
    }

    public String getName() {
        return name;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }

}
