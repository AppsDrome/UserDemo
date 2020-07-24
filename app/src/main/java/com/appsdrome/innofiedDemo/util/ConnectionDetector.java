package com.appsdrome.innofiedDemo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

@SuppressLint("InlinedApi")
public class ConnectionDetector {
    private Context _context;

    // Initialize constructor
    public ConnectionDetector(Context context) {
        this._context = context;
    }

    // Method returns connection available or not & if yes then which type as well
    public boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context
                .CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo blueToothNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        if (blueToothNetwork != null && blueToothNetwork.isConnected()) {
            return true;
        }

        NetworkInfo ethernetNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (ethernetNetwork != null && ethernetNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        return false;
    }
}
