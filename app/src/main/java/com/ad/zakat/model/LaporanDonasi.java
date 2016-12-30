package com.ad.zakat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LaporanDonasi implements Parcelable {

    // Attributes
    public String id_donasi;
    public String jumlah_donasi;

    public String id_muzaki;
    public String nama_muzaki;
    public String alamat_muzaki;
    public String no_identitas_muzaki;
    public String no_telp_muzaki;
    public String status_muzaki;

    public String id_mustahiq;
    public String nama_mustahiq;
    public String alamat_mustahiq;
    public String no_identitas_mustahiq;
    public String no_telp_mustahiq;
    public String validasi_mustahiq;
    public String status_mustahiq;
    public String id_amil_zakat;
    public String nama_amil_zakat;

    // Constructor
    public LaporanDonasi(String id_donasi, String jumlah_donasi, String id_muzaki, String nama_muzaki, String alamat_muzaki, String no_identitas_muzaki, String no_telp_muzaki,
                         String status_muzaki,
                         String id_mustahiq, String nama_mustahiq, String alamat_mustahiq, String no_identitas_mustahiq, String no_telp_mustahiq,
                         String validasi_mustahiq, String status_mustahiq,
                         String id_amil_zakat, String nama_amil_zakat) {

        this.id_donasi = id_donasi;
        this.jumlah_donasi = jumlah_donasi;
        this.id_muzaki = id_muzaki;
        this.nama_muzaki = nama_muzaki;
        this.alamat_muzaki = alamat_muzaki;
        this.no_identitas_muzaki = no_identitas_muzaki;
        this.no_telp_muzaki = no_telp_muzaki;
        this.status_muzaki = status_muzaki;

        this.id_mustahiq = id_mustahiq;
        this.nama_mustahiq = nama_mustahiq;
        this.alamat_mustahiq = alamat_mustahiq;
        this.no_identitas_mustahiq = no_identitas_mustahiq;
        this.no_telp_mustahiq = no_telp_mustahiq;
        this.validasi_mustahiq = validasi_mustahiq;
        this.status_mustahiq = status_mustahiq;
        this.id_amil_zakat = id_amil_zakat;
        this.nama_amil_zakat =nama_amil_zakat;
    }

    public LaporanDonasi(Parcel in) {
        this.id_donasi = in.readString();
        this.jumlah_donasi = in.readString();

        this.id_muzaki = in.readString();
        this.nama_muzaki = in.readString();
        this.alamat_muzaki = in.readString();
        this.no_identitas_muzaki = in.readString();
        this.no_telp_muzaki = in.readString();
        this.status_muzaki = in.readString();

        this.id_mustahiq = in.readString();
        this.nama_mustahiq = in.readString();
        this.alamat_mustahiq = in.readString();
        this.no_identitas_mustahiq = in.readString();
        this.no_telp_mustahiq = in.readString();
        this.validasi_mustahiq = in.readString();
        this.status_mustahiq = in.readString();
        this.id_amil_zakat = in.readString();
        this.nama_amil_zakat = in.readString();
    }

    // Parcelable Creator
    public static final Creator CREATOR = new Creator() {
        public LaporanDonasi createFromParcel(Parcel in) {
            return new LaporanDonasi(in);
        }

        public LaporanDonasi[] newArray(int size) {
            return new LaporanDonasi[size];
        }
    };

    // Parcelling methods
    @Override
    public void writeToParcel(Parcel out, int i) {

        out.writeString(id_donasi);
        out.writeString(jumlah_donasi);

        out.writeString(id_muzaki);
        out.writeString(nama_muzaki);
        out.writeString(alamat_muzaki);
        out.writeString(no_identitas_muzaki);
        out.writeString(no_telp_muzaki);
        out.writeString(status_muzaki);

        out.writeString(id_mustahiq);
        out.writeString(nama_mustahiq);
        out.writeString(alamat_mustahiq);
        out.writeString(no_identitas_mustahiq);
        out.writeString(no_telp_mustahiq);
        out.writeString(validasi_mustahiq);
        out.writeString(status_mustahiq);
        out.writeString(id_amil_zakat);
        out.writeString(nama_amil_zakat);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
