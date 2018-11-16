package common;

import android.content.Context;
import android.content.SharedPreferences;

import com.backendless.BackendlessUser;

import model.UserProfile;

/**
 * Created by Ashish.Kumar on 16-01-2018.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "GrocWorldAdmin";
    private static final String LoggedIn = "GrocWorldAdminUserLoggedIn";
    private static final String FcmToken = "GrocWorldAdminFcmToken";
    private static final String UserToken = "GrocWorldAdminUSerToken";
    private static final String rememberId = "GrocWorldAdminUSerRemId";
    private static final String rememberpassword = "GrocWorldAdminUSerRemPass";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public SharedPreferences getPref() {
        return pref;
    }

    public void clearPreferences() {
        editor.clear().commit();
    }
    public void setUserLoggedIn(boolean isloggedIn) {
        editor.putBoolean(LoggedIn , isloggedIn);
        editor.commit();
    }
    public void setFcmToken(String token) {
        editor.putString(FcmToken , token);
        editor.commit();
    }

    public String getFcmToken() {
        return  pref.getString(FcmToken,"");
    }

    public boolean isUserLoggedIn()
 {
     return pref.getBoolean(LoggedIn , false);
 }
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setUserProfile(BackendlessUser profile) {

        editor.putString(Common.name, profile.getProperty("name").toString());
        editor.putString(Common.email, profile.getProperty("email").toString());
        editor.putString(Common.mobile, profile.getProperty("phoneNumber").toString());
        editor.putString(Common.address, profile.getProperty("Address").toString());
        editor.putString(Common.userId, profile.getProperty("ownerId").toString());
        editor.putString(Common.isAdmin,profile.getProperty("isAdmin").toString());

        editor.commit();
    }

    public void setRememberId(String remId, String pass) {
        editor.putString(rememberId, remId);
        editor.putString(rememberpassword, pass);
        editor.commit();


    }

    public String getRememberId() {
        return pref.getString(rememberId,"");
    }

    public String getRememberpassword() {
        return pref.getString(rememberpassword,"");
    }

    public UserProfile getUserProfile() {
        return new UserProfile(pref);
    }
}
