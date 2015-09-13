package com.example.dylan.ourcloud.home_zone;

/**
 * Created by dylan on 9/12/15.
 */
public class MenuOption {

    private int type;
    private String title;
    private String imageUrl;

    public MenuOption setType(int type) {
        this.type = type;
        return this;
    }
    public MenuOption setTitle(String title) {
        this.title = title;
        return this;
    }
    public MenuOption setImage(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getType() {
        return type;
    }
    public String getTitle() {
        return title;
    }
    public String getImageUrl() {
        return imageUrl;
    }

}
