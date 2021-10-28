package com.amirmohammed.ultras11july2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPref {

    private static SharedPreferences mSharedPref;

    private SharedPref() {

    }

    public static void init(Context context) {
        if (mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
//        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
//        prefsEditor.putString(key, value);
//        prefsEditor.apply();

        mSharedPref.edit().putString(key, value).apply();
    }

    public static boolean read(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    public static Integer read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).apply();
    }

    public static void write(String key, User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        mSharedPref.edit().putString(key, userJson).apply();
    }

    public static User read(String key) {
        String userJson = mSharedPref.getString(key, "");
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        return user;
    }

}
