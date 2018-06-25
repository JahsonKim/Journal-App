package com.oceanscan.journal.app.data;

import android.content.Context;
import android.content.SharedPreferences;

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



}
