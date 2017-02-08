package com.ad.zakat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CalonMustahiq implements Parcelable {

    // Parcelable Creator
    public static final Creator CREATOR = new Creator() {
        public CalonMustahiq createFromParcel(Parcel in) {
            return new CalonMustahiq(in);
        }

        public CalonMustahiq[] newArray(int size) {
            return new CalonMustahiq[size];
        }
    };
    // Attributes
    public String id_calon_mustahiq;
    public String nama_calon_mustahiq;
    public String alamat_calon_mustahiq;
    public String no_identitas_calon_mustahiq;
    public String no_telp_calon_mustahiq;
    public String status_calon_mustahiq;

    // Constructor
    public CalonMustahiq(String id_calon_mustahiq, String nama_calon_mustahiq, String alamat_calon_mustahiq, String no_identitas_calon_mustahiq, String no_telp_calon_mustahiq
            , String status_calon_mustahiq) {
        this.id_calon_mustahiq = id_calon_mustahiq;
        this.nama_calon_mustahiq = nama_calon_mustahiq;
        this.alamat_calon_mustahiq = alamat_calon_mustahiq;
        this.no_identitas_calon_mustahiq = no_identitas_calon_mustahiq;
        this.no_telp_calon_mustahiq = no_telp_calon_mustahiq;
        this.status_calon_mustahiq = status_calon_mustahiq;
    }

    public CalonMustahiq(Parcel in) {
        this.id_calon_mustahiq = in.readString();
        this.nama_calon_mustahiq = in.readString();
        this.alamat_calon_mustahiq = in.readString();
        this.no_identitas_calon_mustahiq = in.readString();
        this.no_telp_calon_mustahiq = in.readString();
        this.status_calon_mustahiq = in.readString();
    }

    // Parcelling methods
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id_calon_mustahiq);
        out.writeString(nama_calon_mustahiq);
        out.writeString(alamat_calon_mustahiq);
        out.writeString(no_identitas_calon_mustahiq);
        out.writeString(no_telp_calon_mustahiq);
        out.writeString(status_calon_mustahiq);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
