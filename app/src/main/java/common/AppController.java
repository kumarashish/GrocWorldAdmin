package common;

import android.app.Application;
import android.graphics.Typeface;
import android.location.Location;
import android.support.multidex.MultiDexApplication;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import model.UserProfile;
import utils.Validation;

/**
 * Created by ashish.kumar on 03-07-2018.
 */

public class AppController  extends MultiDexApplication{
    String address="";
    Location currentLocation=null;
    Validation validation=null;
    PrefManager prefManager;


    Typeface bold,normal,logo_font;
    @Override
    public void onCreate() {
        super.onCreate();
        validation=new Validation(this);
        normal = Typeface.createFromAsset(getApplicationContext().getAssets(), "font.ttf");
        bold= Typeface.createFromAsset(getApplicationContext().getAssets(), "bold.ttf");
        logo_font= Typeface.createFromAsset(getApplicationContext().getAssets(), "logofont.otf");
        prefManager=new   PrefManager(this);
        Backendless.initApp(this, Defaults.APPID, Defaults.APIKEY);

    }

    public void setAddress(String address, Location loc) {
        this.address = address;
        this.currentLocation=loc;
    }
    public void setCurrentAddress(String address) {
        this.address = address;

    }

    public void setLocation(LatLng loc) {
       currentLocation.setLatitude(loc.latitude);
       currentLocation.setLongitude(loc.longitude);
    }
    public String getAddress() {
        return address;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setUserProfile(BackendlessUser user) {
        prefManager.setUserProfile(user);
    }

    public String getRememberpassword() {
        return prefManager.getRememberpassword();
    }
    public void setLogout()
    {
        setUserLoggedIn(false);
    }
    public void setRememberId(String remId, String pass) {
        prefManager.setRememberId(remId, pass);
    }

    public String getRememberId() {
        return prefManager.getRememberId();
    }
    public UserProfile getUserProfil() {
        return prefManager.getUserProfile();
    }

    public boolean isUserLoggedIn() {
        return prefManager.isUserLoggedIn();
    }

    public void setUserLoggedIn(boolean isloggedIn) {
        prefManager.setUserLoggedIn(isloggedIn);
    }

    public Typeface getBold() {
        return bold;
    }

    public Typeface getNormal() {
        return normal;
    }


}
