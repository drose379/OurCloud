package com.example.dylan.ourcloud;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Created by dylan on 8/6/15.
 */
public class WifiController {

    private WifiManager wifiManager;

    public WifiController(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public String getWifiId() {
        String wifiId = wifiManager.getConnectionInfo().getBSSID();
        return wifiId;
    }

}
