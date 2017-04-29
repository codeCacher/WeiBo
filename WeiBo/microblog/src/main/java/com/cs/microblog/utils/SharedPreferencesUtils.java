package com.cs.microblog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.cs.microblog.custom.Constants;

/**
 * Created by Administrator on 2017/4/29.
 * SharedPreferences Utils
 */

public class SharedPreferencesUtils {
    private static SharedPreferences sp;

    /**
     * put a string into the SharedPreferences file
     * @param context Context
     * @param key Key
     * @param value the string
     */
    public static void putString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * get the string from the SharedPreferences file via the key
     * @param context Context
     * @param key Key
     * @param defaultValue if can't find the string through the key,return the defaultValue
     * @return
     */
    public static String getString(Context context, String key, String defaultValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defaultValue);
    }

    /**
     * put a int value into the SharedPreferences file
     * @param context Context
     * @param key Key
     * @param value the int value
     */
    public static void putInt(Context context, String key, int value) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * get the int value from the SharedPreferences file via the key
     * @param context Context
     * @param key Key
     * @param defaultValue if can't find the string through the key,return the defaultValue
     * @return
     */
    public static int getInt(Context context, String key, int defaultValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defaultValue);
    }
}
