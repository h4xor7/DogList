package com.example.doglistjet.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SharedPrefrencesHelper {

    private static final String PREF_TIME = "Pref_time";
    private static SharedPrefrencesHelper instance;
    private SharedPreferences prefs;

    private SharedPrefrencesHelper(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

    }


    public static SharedPrefrencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefrencesHelper(context);
        }
        return instance;
    }

    public void saveUpdateTime(long time) {
        prefs.edit().putLong(PREF_TIME, time).apply();
    }


    public long getUpdateTime() {
        return prefs.getLong(PREF_TIME, 0);
    }

    public String getCacheDuration(){

        return  prefs.getString("pref_cache_duration","");


    }



}
