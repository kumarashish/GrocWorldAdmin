package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



/**
 * Created by ashish.kumar on 06-07-2018.
 */

public class Utils {
    static String strAdd = "";
    public static boolean isNetworkAvailable(Activity act) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            Toast.makeText(act, "Internet Unavailable", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static double distance(double lat1, double lon1, double lat2, double lon2, String sr) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (sr.equals("K")) {
            dist = dist * 1.609344;
        } else if (sr.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }
    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    public static String getOrderStatusString(String typ) {
        String value = "";
        int type = Integer.parseInt(typ);
        switch (type) {
            case 1:
                value = "Pending Acceptance from storeowner";
                break;
            case 2:
                value = "Accepted will be delivered in 3 hours";
                break;
            case 3:
                value = "Delivered";
                break;
            case 4:
                value = "Cancelled";
                break;
        }
        return value;
    }

}
