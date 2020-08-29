package com.example.sangol.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;

/**
 * Created by UTG-120 on 3/7/2018.
 */

public class ConnectionManager {
    //String urlPath = "http://188.166.175.200:81/UTG/";
    String urlPath = "http://192.168.43.142:81/utg/";
//public static final boolean CheckInternetConnection(Context context)
    public boolean isNetworkAvailable(Context context) {
       /** ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;

        } else {
            return false;
        }
        **/

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 192.168.43.142");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}
