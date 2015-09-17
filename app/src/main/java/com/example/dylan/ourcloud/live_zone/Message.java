package com.example.dylan.ourcloud.live_zone;

/**
 * Created by dylan on 9/17/15.
 */
public class Message {

    private String text;
    private String otherUserName;
    private int origin;

    public Message setText(String messageText) {
        text = messageText;
        return this;
    }
    public Message setOtherUserName(String name) {
        otherUserName = name;
        return this;
    }
    public Message setOrigin(int origin) {
        this.origin = origin;
        return this;
    }

    public String getText() {
        return text;
    }
    public String getOtherUserName() {
        return otherUserName;
    }
    public int getOrigin() {
        return origin;
    }


}
