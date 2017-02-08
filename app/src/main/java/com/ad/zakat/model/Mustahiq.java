package com.ad.zakat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mustahiq implements Parcelable {

    public static final Creator<Mustahiq> CREATOR = new Creator<Mustahiq>() {
        @Override
        public Mustahiq createFromParcel(Parcel in) {
            return new Mustahiq(in);
        }

        @Override
        public Mustahiq[] newArray(int size) {
            return new Mustahiq[size];
        }
    };
    // Attributes
    public String id_mustahiq;
    public String id_calon_mustahiq;
    public String nama_calon_mustahiq;
    public String alamat_calon_mustahiq;
    public String no_identitas_calon_mustahiq;
    public String no_telp_calon_mustahiq;
    public String status_mustahiq;
    public String id_amil_zakat;
    public String nama_amil_zakat;
    public String waktu_terakhir_donasi;

    public Mustahiq(String id_mustahiq, String id_calon_mustahiq, String nama_calon_mustahiq, String alamat_calon_mustahiq, String no_identitas_calon_mustahiq, String no_telp_calon_mustahiq, String status_mustahiq, String id_amil_zakat, String nama_amil_zakat, String waktu_terakhir_donasi) {
        this.id_mustahiq = id_mustahiq;
        this.id_calon_mustahiq = id_calon_mustahiq;
        this.nama_calon_mustahiq = nama_calon_mustahiq;
        this.alamat_calon_mustahiq = alamat_calon_mustahiq;
        this.no_identitas_calon_mustahiq = no_identitas_calon_mustahiq;
        this.no_telp_calon_mustahiq = no_telp_calon_mustahiq;
        this.status_mustahiq = status_mustahiq;
        this.id_amil_zakat = id_amil_zakat;
        this.nama_amil_zakat = nama_amil_zakat;
        this.waktu_terakhir_donasi = waktu_terakhir_donasi;
    }

    protected Mustahiq(Parcel in) {
        id_mustahiq = in.readString();
        id_calon_mustahiq = in.readString();
        nama_calon_mustahiq = in.readString();
        alamat_calon_mustahiq = in.readString();
        no_identitas_calon_mustahiq = in.readString();
        no_telp_calon_mustahiq = in.readString();
        status_mustahiq = in.readString();
        id_amil_zakat = in.readString();
        nama_amil_zakat = in.readString();
        waktu_terakhir_donasi = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_mustahiq);
        dest.writeString(id_calon_mustahiq);
        dest.writeString(nama_calon_mustahiq);
        dest.writeString(alamat_calon_mustahiq);
        dest.writeString(no_identitas_calon_mustahiq);
        dest.writeString(no_telp_calon_mustahiq);
        dest.writeString(status_mustahiq);
        dest.writeString(id_amil_zakat);
        dest.writeString(nama_amil_zakat);
        dest.writeString(waktu_terakhir_donasi);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
