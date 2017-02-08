package com.ad.zakat;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


public class Zakat extends Application {


    public static final String LAST_SELECTED = "last_drawer_selection";
    public static final String VIEW_TYPE = "view_type";

    public static final int VIEW_TYPE_CALON_MUSTAHIQ = 1;
    public static final int VIEW_TYPE_MUSTAHIQ = 2;
    public static final int VIEW_TYPE_DONASI = 3;
    public static final int VIEW_TYPE_LAPORAN_DONASI = 4;

    public static final int MY_SOCKET_TIMEOUT_MS = 60000;
    public static final int JPEG_OUTPUT_QUALITY = 70;
    public static final String CALON_MUSTAHIQ_ID = "id_calon_mustahiq";
    public static final String MUSTAHIQ_ID = "id_mustahiq";
    public static final String MUSTAHIQ_OBJECT = "mustahiq_object";
    public static final String LAPORAN_DONASI_OBJECT = "laporan_donasi_object";
    public static final String DONASI_ID = "donasi_id";
    public static final String CALON_MUSTAHIQ_OBJECT = "calon_mustahiq_object";
    public static final String URL = "url";
    public static final String KEYWORD = "keyword";

    public static final String data = "data";

    public static final String TOOLBAR_TITLE = "toolbar_title";
    public static final String IS_FINISH_LOADING_AWAL_DATA = "is_loading";
    public static final String IS_LOADING_MORE_DATA = "is_locked";

    public static final String TAG_GRID_FRAGMENT = "movie_grid_fragment";
    public static final String isSuccess = "isSuccess";
    public static final String message = "message";


    public static final String foto_bukti_pembayaran = "foto_bukti_pembayaran";

    public static final String PAGE = "PAGE";


    public static final String calon_mustahiq = "calon_mustahiq";
    public static final String id_calon_mustahiq = "id_calon_mustahiq";
    public static final String nama_calon_mustahiq = "nama_calon_mustahiq";
    public static final String alamat_calon_mustahiq = "alamat_calon_mustahiq";
    public static final String no_identitas_calon_mustahiq = "no_identitas_calon_mustahiq";
    public static final String no_telp_calon_mustahiq = "no_telp_calon_mustahiq";
    public static final String status_calon_mustahiq = "status_calon_mustahiq";


    public static final String jumlah_donasi = "jumlah_donasi";

    public static final String id_muzaki = "id_muzaki";
    public static final String nama_muzaki = "nama_muzaki";
    public static final String alamat_muzaki = "alamat_muzaki";
    public static final String no_identitas_muzaki = "no_identitas_muzaki";
    public static final String no_telp_muzaki = "no_telp_muzaki";
    public static final String status_muzaki = "status_muzaki";

    public static final String donasi = "donasi";
    public static final String id_donasi = "id_donasi";
    public static final String id_amil_zakat = "id_amil_zakat";
    public static final String nama_amil_zakat = "nama_amil_zakat";
    public static final String amil_zakat = "amil_zakat";
    public static final String waktu_terakhir_donasi = "waktu_terakhir_donasi";

    public static String mustahiq = "mustahiq";
    public static String id_mustahiq = "id_mustahiq";
    public static String status_mustahiq = "status_mustahiq";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
