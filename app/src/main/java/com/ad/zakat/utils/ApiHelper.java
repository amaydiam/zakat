package com.ad.zakat.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.ad.zakat.R;
import com.ad.zakat.R2;

public class ApiHelper {


    private static String getBaseUrl(Context context) {
        return Prefs.getUrl(context) + "/" + context.getString(R.string.base_url);
    }

    private static String getApiUrl(Context context) {
        return Prefs.getUrl(context) + "/" + context.getString(R.string.base_url) + "/index.php/";
    }

    // Add Donasi Baru
    public static String getDonasiBaruLink(Context context) {
        return getApiUrl(context) + "donasi/adddonasi";
    }

    //laporan donasi
    public static String getLaporanDonasiLink(Context context, int page, String keyword) {
        return getApiUrl(context) + "donasi/donasi/" + page + (!TextUtils.isNullOrEmpty(keyword) ? ("/" + keyword) : "");
    }

    public static String getLaporanDonasiDetailLink(Context context, String id) {
        return getApiUrl(context) + "donasi/detail_donasi/" + id;
    }

    //Mustahiq
    public static String getMustahiqLink(Context context, int page) {
        return getApiUrl(context) + "mustahiq/mustahiq/" + page;
    }

    public static String getMustahiqDetailLink(Context context, String id) {
        return getApiUrl(context) + "mustahiq/detail_mustahiq/" + id;
    }

    public static String getMustahiqAddEditLink(Context context) {
        return getApiUrl(context) + "mustahiq/addeditmustahiq/";
    }


    public static String getMustahiqDeleteLink(Context context, String id) {
        return getApiUrl(context) + "mustahiq/delete_mustahiq/" + id;
    }

    //donasi
    public static String getDonasiLink(Context context, int page, String keyword) {
        return getApiUrl(context) + "mustahiq/donasi/" + page + (!TextUtils.isNullOrEmpty(keyword) ? ("/" + keyword) : "");
    }

    public static String getTesUrl(Context context, String val_url) {
        return "http://" + val_url + "/" + context.getString(R.string.base_url) + "/index.php/tes_url";
    }

    //donasi
    public static String getAmilZakatLink(Context context) {
        return getApiUrl(context) + "amil_zakat/all_amil_zakat";
    }


}
