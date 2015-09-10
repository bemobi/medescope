package br.com.bemobi.medescope.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by brunocosta on 9/10/15.
 */
public class PreferencesUtils {
    // private static Map<Context, SharedPreferences> preferences = new HashMap<Context, SharedPreferences>();

    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences pref
                // = preferences.get(context.getApplicationContext())
                ;
        // if (pref == null) {
        pref = context.getApplicationContext().getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        // preferences.put(context, pref);
        // }

        return pref;
    }

    public static void clearPreferences(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static boolean getBooleanPreference(Context context, int resId, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(context.getString(resId), defaultValue);
    }

    public static boolean getBooleanPreference(Context context, String prefKey, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(prefKey, defaultValue);
    }

    public static int getIntPreference(Context context, int resId, int defaultValue) {
        return getSharedPreferences(context).getInt(context.getString(resId), defaultValue);
    }

    public static int getIntPreference(Context context, String prefKey, int defaultValue) {
        return getSharedPreferences(context).getInt(prefKey, defaultValue);
    }

    public static long getLongPreference(Context context, int resId, long defaultValue) {
        return getSharedPreferences(context).getLong(context.getString(resId), defaultValue);
    }

    public static long getLongPreference(Context context, String prefKey, long defaultValue) {
        return getSharedPreferences(context).getLong(prefKey, defaultValue);
    }

    public static String getStringPreference(Context context, int resId, String defaultValue) {
        return getSharedPreferences(context).getString(context.getString(resId), defaultValue);
    }

    public static String getStringPreference(Context context, String prefKey, String defaultValue) {
        return getSharedPreferences(context).getString(prefKey, defaultValue);
    }

    public static void savePreference(Context context, int resId, int newValue) {
        SharedPreferences mPreferences = PreferencesUtils.getSharedPreferences(context);
        Editor editor = mPreferences.edit();
        editor.putInt(context.getString(resId), newValue);
        editor.commit();
    }

    public static void savePreference(Context context, String prefKey, int newValue) {
        SharedPreferences mPreferences = PreferencesUtils.getSharedPreferences(context);
        Editor editor = mPreferences.edit();
        editor.putInt(prefKey, newValue);
        editor.commit();
    }

    public static void savePreference(Context context, int resId, long newValue) {
        SharedPreferences mPreferences = PreferencesUtils.getSharedPreferences(context);
        Editor editor = mPreferences.edit();
        editor.putLong(context.getString(resId), newValue);
        editor.commit();
    }

    public static void savePreference(Context context, String prefKey, long newValue){
        SharedPreferences mPreferences = PreferencesUtils.getSharedPreferences(context);
        Editor editor = mPreferences.edit();
        editor.putLong(prefKey, newValue);
        editor.commit();
    }

    public static void savePreference(Context context, int resId, String newValue) {
        SharedPreferences mPreferences = PreferencesUtils.getSharedPreferences(context);
        Editor editor = mPreferences.edit();
        editor.putString(context.getString(resId), newValue);
        editor.commit();
    }

    public static void savePreference(Context context, String prefKey, String newValue) {
        SharedPreferences mPreferences = PreferencesUtils.getSharedPreferences(context);
        Editor editor = mPreferences.edit();
        editor.putString(prefKey, newValue);
        editor.commit();
    }

    public static void savePreference(Context context, int resId, Boolean newValue) {
        SharedPreferences mPreferences = PreferencesUtils.getSharedPreferences(context);
        Editor editor = mPreferences.edit();
        editor.putBoolean(context.getString(resId), newValue);
        editor.commit();
    }

    public static void savePreference(Context context, String prefKey, Boolean newValue) {
        SharedPreferences mPreferences = PreferencesUtils.getSharedPreferences(context);
        Editor editor = mPreferences.edit();
        editor.putBoolean(prefKey, newValue);
        editor.commit();
    }

    public static void removePreference(Context context, int resId) {
        SharedPreferences mPreferences = PreferencesUtils.getSharedPreferences(context);
        Editor editor = mPreferences.edit();
        editor.remove(context.getString(resId));
        editor.commit();
    }

    public static void removePreference(Context context, String prefKey) {
        SharedPreferences mPreferences = PreferencesUtils.getSharedPreferences(context);
        Editor editor = mPreferences.edit();
        editor.remove(prefKey);
        editor.commit();
    }
}
