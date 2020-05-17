package com.example.android.moviemea.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.preference.PreferenceManager;

import com.example.android.moviemea.R;


public class AppSharedPreferences {

    private static final String LOG_TAG = AppSharedPreferences.class.getSimpleName();

    private static SharedPreferences mSharedPreferences;

    public static String LANGUAGE_PARAM_VALUE_PREF_KEY;
    public static String SORT_BY_PARAM_VALUE_PREF_KEY;
    public static String MOVIES_ORDER_PATH_PARAM_PREF_KEY;
    public static String IMAGE_SIZE_PATH_PARAM_PREF_KEY;

    public static String LANGUAGE_PARAM_VALUE_DEFAULT_VALUE;
    public static String SORT_BY_PARAM_VALUE_DEFAULT_VALUE;
    public static String MOVIES_ORDER_PATH_PARAM_DEFAULT_VALUE;
    public static String IMAGE_SIZE_PATH_PARAM_DEFAULT_VALUE;


    private AppSharedPreferences() {
    }

    public static void init(Context context) {
        if (mSharedPreferences == null) {
            initConstants(context);

            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(LANGUAGE_PARAM_VALUE_PREF_KEY, LANGUAGE_PARAM_VALUE_DEFAULT_VALUE);
//            editor.putString(SORT_BY_PARAM_VALUE_PREF_KEY, SORT_BY_PARAM_VALUE_DEFAULT_VALUE);
//            editor.putString(MOVIES_ORDER_PATH_PARAM_PREF_KEY, MOVIES_ORDER_PATH_PARAM_DEFAULT_VALUE);
            editor.putString(IMAGE_SIZE_PATH_PARAM_PREF_KEY, IMAGE_SIZE_PATH_PARAM_DEFAULT_VALUE);
            editor.apply();
        }
    }

    private static void initConstants(Context context) {
        Resources res = context.getResources();

        LANGUAGE_PARAM_VALUE_PREF_KEY = res.getString(R.string.pref_language_param_key);
        LANGUAGE_PARAM_VALUE_DEFAULT_VALUE = res.getString(R.string.pref_language_param_default);
        SORT_BY_PARAM_VALUE_PREF_KEY = res.getString(R.string.pref_sort_by_param_key);
        SORT_BY_PARAM_VALUE_DEFAULT_VALUE = res.getString(R.string.pref_sort_by_param_default);
        MOVIES_ORDER_PATH_PARAM_PREF_KEY = res.getString(R.string.pref_movies_order_key);
        MOVIES_ORDER_PATH_PARAM_DEFAULT_VALUE = res.getString(R.string.pref_movies_order_entry_now_playing);
        IMAGE_SIZE_PATH_PARAM_PREF_KEY = res.getString(R.string.pref_image_size_key);
        IMAGE_SIZE_PATH_PARAM_DEFAULT_VALUE = res.getString(R.string.pref_image_size_default);
    }

    public static boolean read(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public static int read(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public static String read(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public static float read(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value).apply();
    }

    public static void write(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value).apply();
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    public static void write(String key, float value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(key, value).apply();
    }
}
