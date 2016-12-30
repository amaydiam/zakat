package com.ad.zakat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ad.zakat.Zakat;

public final class Prefs {

    public static SharedPreferences get(final Context context) {
        return context.getSharedPreferences("com.ad.zakat",
                Context.MODE_PRIVATE);
    }

    public static String getPref(final Context context, String pref, String def) {
        SharedPreferences prefs = Prefs.get(context);
        String val = prefs.getString(pref, def);

        if (TextUtils.isNullOrEmpty(val))
            return def;
        else
            return val;
    }

    public static void putPref(final Context context, String pref, String val) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(pref, val);
        editor.apply();
    }


    //set URL
    public static String getUrl(final Context context) {
        String e = Prefs.getPref(context, Zakat.URL, null);
        return e;
    }

    public static void putUrl(final Context context, String url) {
        Prefs.putPref(context, Zakat.URL, url);
    }

    // last selected
    public static int getLastSelected(final Context context) {
        String e = Prefs.getPref(context, Zakat.LAST_SELECTED, String.valueOf(Zakat.VIEW_TYPE_MUSTAHIQ));
        return Integer.parseInt(e);
    }

    public static void putLastSelected(final Context context, int view_type) {
        Prefs.putPref(context, Zakat.LAST_SELECTED, String.valueOf(view_type));
    }
}