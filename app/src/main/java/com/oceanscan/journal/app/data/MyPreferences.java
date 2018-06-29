package com.oceanscan.journal.app.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseUser;
import com.oceanscan.journal.app.utils.Constants;

public class MyPreferences {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    private String TAG = MyPreferences.class.getSimpleName();

    public MyPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Constants.Preferences.PREFERENCES, Constants.Preferences.PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(Constants.Preferences.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(Constants.Preferences.IS_FIRST_TIME_LAUNCH, true);
    }

    public void setUser(FirebaseUser user) {
        editor.putString(Constants.Preferences.DISPLAY_NAME, user.getDisplayName());
        editor.putString(Constants.Preferences.PHONE_NUMBER, user.getPhoneNumber());
        editor.putString(Constants.Preferences.USER_ID, user.getUid());
        editor.putString(Constants.Preferences.EMAIL, user.getEmail());
        editor.commit();
    }

    public String getDisplayName() {
        if (pref.getString(Constants.Preferences.DISPLAY_NAME, null) != null)
            return pref.getString(Constants.Preferences.DISPLAY_NAME, null);
        else
            return null;
    }

    public String getPhoneNumber() {
        if (pref.getString(Constants.Preferences.PHONE_NUMBER, null) != null)
            return pref.getString(Constants.Preferences.PHONE_NUMBER, null);
        else
            return null;
    }

    public String getUserId() {
        if (pref.getString(Constants.Preferences.USER_ID, null) != null)
            return pref.getString(Constants.Preferences.USER_ID, null);
        else
            return null;
    }

    public String getEmail() {
        if (pref.getString(Constants.Preferences.EMAIL, null) != null)
            return pref.getString(Constants.Preferences.EMAIL, null);
        else
            return null;
    }

}
