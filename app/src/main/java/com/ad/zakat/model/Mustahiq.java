package com.ad.zakat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mustahiq implements Parcelable {

    // Attributes
    public String id_mustahiq;
    public String nama_mustahiq;
    public String alamat_mustahiq;
    public String no_identitas_mustahiq;
    public String no_telp_mustahiq;
    public String validasi_mustahiq;
    public String status_mustahiq;
    public String id_amil_zakat;
    public String nama_amil_zakat;
    public String waktu_terakhir_donasi;

    // Constructor
    public Mustahiq(String id_mustahiq, String nama_mustahiq, String alamat_mustahiq, String no_identitas_mustahiq, String no_telp_mustahiq,
                    String validasi_mustahiq, String status_mustahiq, String id_amil_zakat, String nama_amil_zakat, String waktu_terakhir_donasi) {
        this.id_mustahiq = id_mustahiq;
        this.nama_mustahiq = nama_mustahiq;
        this.alamat_mustahiq = alamat_mustahiq;
        this.no_identitas_mustahiq = no_identitas_mustahiq;
        this.no_telp_mustahiq = no_telp_mustahiq;
        this.validasi_mustahiq = validasi_mustahiq;
        this.status_mustahiq = status_mustahiq;
        this.id_amil_zakat = id_amil_zakat;
        this.nama_amil_zakat =nama_amil_zakat;
        this.waktu_terakhir_donasi=waktu_terakhir_donasi;
    }

    public Mustahiq(Parcel in) {
        this.id_mustahiq = in.readString();
        this.nama_mustahiq = in.readString();
        this.alamat_mustahiq = in.readString();
        this.no_identitas_mustahiq = in.readString();
        this.no_telp_mustahiq = in.readString();
        this.validasi_mustahiq = in.readString();
        this.status_mustahiq = in.readString();
        this.id_amil_zakat = in.readString();
        this.nama_amil_zakat = in.readString();
        this.waktu_terakhir_donasi=in.readString();
    }

    // Parcelable Creator
    public static final Creator CREATOR = new Creator() {
        public Mustahiq createFromParcel(Parcel in) {
            return new Mustahiq(in);
        }

        public Mustahiq[] newArray(int size) {
            return new Mustahiq[size];
        }
    };

    // Parcelling methods
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id_mustahiq);
        out.writeString(nama_mustahiq);
        out.writeString(alamat_mustahiq);
        out.writeString(no_identitas_mustahiq);
        out.writeString(no_telp_mustahiq);
        out.writeString(validasi_mustahiq);
        out.writeString(status_mustahiq);
        out.writeString(id_amil_zakat);
        out.writeString(nama_amil_zakat);
        out.writeString(waktu_terakhir_donasi);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
