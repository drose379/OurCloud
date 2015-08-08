package com.example.dylan.ourcloud;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Created by dylan on 8/6/15.
 */
public class WifiController {

    private WifiManager wifiManager;
    public boolean connected = false;

    private static WifiController wifiController;

    public static WifiController getInstance(Context context) {
        if (wifiController == null) {
            wifiController = new WifiController(context);
        }
        return wifiController;
    }

    public WifiController(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public String getWifiId() {
        String wifiId = wifiManager.getConnectionInfo().getBSSID();
        return wifiId;
    }

    public boolean isConnected() {
        connected = getWifiId() == null ? false : true;
        return connected;
    }

}
