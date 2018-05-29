package com.valecom.yingul.Util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.onesignal.OneSignal;

/**
 * Created by gonzalo on 23-05-18.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    public SharedPreferences preferences;
    public String prefName = "app";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "launch";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public MyApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();
        mInstance = this;
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Montserrat-Regular_0.ttf");
    }

    public MyApplication(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void saveTheme1(boolean Theme1) {
        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Theme1", Theme1);
        editor.commit();
    }

    public boolean getTheme1() {
        preferences = this.getSharedPreferences(prefName, 0);
        if (preferences != null) {
            boolean image = preferences.getBoolean("Theme1", true);
            return image;
        }
        return false;
    }

    public void saveTheme2(boolean Theme2) {
        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Theme2", Theme2);
        editor.commit();
    }

    public boolean getTheme2() {
        preferences = this.getSharedPreferences(prefName, 0);
        if (preferences != null) {
            boolean image = preferences.getBoolean("Theme2", false);
            return image;
        }
        return false;
    }
}
