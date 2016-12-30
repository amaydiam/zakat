package com.ad.zakat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ad.zakat.R;
import com.ad.zakat.R2;
import com.ad.zakat.Zakat;
import com.ad.zakat.model.LaporanDonasi;
import com.ad.zakat.utils.ApiHelper;
import com.ad.zakat.utils.CustomVolley;
import com.ad.zakat.utils.TextUtils;
import com.ad.zakat.utils.Utils;
import com.ad.zakat.widget.RobotoBoldTextView;
import com.ad.zakat.widget.RobotoLightTextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import org.json.JSONObject;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LaporanDonasiDetailFragment extends Fragment implements CustomVolley.OnCallbackResponse {

    @BindBool(R.bool.is_tablet)
    boolean isTablet;

    // Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_text_holder)
    View toolbarTextHolder;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_subtitle)
    TextView toolbarSubtitle;

    // Main views
    @BindView(R.id.progress_circle)
    View progressCircle;
    @BindView(R.id.error_message)
    View errorMessage;
    @BindView(R.id.text_error)
    RobotoLightTextView textError;
    @BindView(R.id.try_again)
    RobotoBoldTextView tryAgain;
    @BindView(R.id.movie_detail_holder)
    NestedScrollView movieHolder;

    // Basic info


    @BindView(R.id.foto_profil_muzaki)
    AvatarView fotoProfilMuzaki;
    @BindView(R.id.nama_muzaki)
    RobotoBoldTextView namaMuzaki;
    @BindView(R.id.alamat_muzaki)
    RobotoLightTextView alamatMuzaki;
    @BindView(R.id.no_identitas_muzaki)
    RobotoLightTextView noIdentitasMuzaki;
    @BindView(R.id.no_telp_muzaki)
    RobotoLightTextView noTelpMuzaki;
    @BindView(R.id.status_muzaki)
    RobotoLightTextView statusMuzaki;


    @BindView(R.id.foto_profil_mustahiq)
    AvatarView fotoProfilMustahiq;
    @BindView(R.id.nama_mustahiq)
    RobotoBoldTextView namaMustahiq;
    @BindView(R.id.alamat_mustahiq)
    RobotoLightTextView alamatMustahiq;
    @BindView(R.id.no_identitas_mustahiq)
    RobotoLightTextView noIdentitasMustahiq;
    @BindView(R.id.no_telp_mustahiq)
    RobotoLightTextView noTelpMustahiq;
    @BindView(R.id.validasi_mustahiq)
    RobotoLightTextView validasiMustahiq;
    @BindView(R.id.status_mustahiq)
    RobotoLightTextView statusMustahiq;
    @BindView(R.id.nama_amil_zakat)
    RobotoLightTextView namaAmilZakat;

    @BindView(R.id.jumlah_donasi)
    RobotoLightTextView jumlahDonasi;


    private Unbinder unbinder;
    private String id;
    private LaporanDonasi laporanDonasi;
    private PicassoLoader imageLoader;
    private CustomVolley customVolley;
    private RequestQueue queue;
    private static final String TAG_DETAIL = "TAG_DETAIL";


    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_laporan_donasi_detail, container, false);
        unbinder = ButterKnife.bind(this, v);
        customVolley = new CustomVolley(getActivity());
        customVolley.setOnCallbackResponse(this);
        imageLoader = new PicassoLoader();

        // Setup toolbar
        toolbar.setTitle(R.string.loading);
        if (!isTablet) {
            toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.action_home));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
        }


        // Download laporanDonasi details if new instance, else restore from saved instance
        if (savedInstanceState == null || !(savedInstanceState.containsKey(Zakat.DONASI_ID)
                && savedInstanceState.containsKey(Zakat.LAPORAN_DONASI_OBJECT))) {
            id = getArguments().getString(Zakat.DONASI_ID);
            if (TextUtils.isNullOrEmpty(id)) {
                progressCircle.setVisibility(View.GONE);
                toolbarTextHolder.setVisibility(View.GONE);
                toolbar.setTitle("");
            } else {
                downloadLokasiDetails(id);
            }
        } else {
            id = savedInstanceState.getString(Zakat.DONASI_ID);
            laporanDonasi = savedInstanceState.getParcelable(Zakat.LAPORAN_DONASI_OBJECT);
            onDownloadSuccessful();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Send screen id_user_kru to analytics
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (laporanDonasi != null && id != null) {
            outState.putString(Zakat.DONASI_ID, id);
            outState.putParcelable(Zakat.LAPORAN_DONASI_OBJECT, laporanDonasi);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (queue != null) {
            queue.cancelAll(TAG_DETAIL);
        }

    }


    // JSON parsing and display
    private void downloadLokasiDetails(String id) {
        String urlToDownload = ApiHelper.getLaporanDonasiDetailLink(getActivity(), id);
        queue = customVolley.Rest(Request.Method.GET, urlToDownload, null, TAG_DETAIL);
    }

    private void onDownloadSuccessful() {

        // Toggle visibility
        progressCircle.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        movieHolder.setVisibility(View.VISIBLE);

        toolbar.setTitle(laporanDonasi.nama_mustahiq);
        toolbarTextHolder.setVisibility(View.GONE);

        imageLoader.loadImage(fotoProfilMuzaki, laporanDonasi.nama_muzaki, laporanDonasi.nama_muzaki);
        namaMuzaki.setText("Nama : " + laporanDonasi.nama_muzaki);
        alamatMuzaki.setText("Alamat : " + (TextUtils.isNullOrEmpty(laporanDonasi.alamat_muzaki) ? "-" : laporanDonasi.alamat_muzaki));
        noIdentitasMuzaki.setText("No Identitas : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_identitas_muzaki) ? "-" : laporanDonasi.no_identitas_muzaki));
        noTelpMuzaki.setText("No Telp : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_telp_muzaki) ? "-" : laporanDonasi.no_telp_muzaki));
        statusMuzaki.setText(Html.fromHtml("Status Aktif : " + (laporanDonasi.status_muzaki.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));


        jumlahDonasi.setText(Html.fromHtml("Jumlah Donasi : Rp. " + Utils.Rupiah(laporanDonasi.jumlah_donasi)));


        imageLoader.loadImage(fotoProfilMustahiq, laporanDonasi.nama_mustahiq, laporanDonasi.nama_mustahiq);
        namaMustahiq.setText("Nama : " + laporanDonasi.nama_mustahiq);
        alamatMustahiq.setText("Alamat : " + (TextUtils.isNullOrEmpty(laporanDonasi.alamat_mustahiq) ? "-" : laporanDonasi.alamat_mustahiq));
        noIdentitasMustahiq.setText("No Identitas : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_identitas_mustahiq) ? "-" : laporanDonasi.no_identitas_mustahiq));
        noTelpMustahiq.setText("No Telp : " + (TextUtils.isNullOrEmpty(laporanDonasi.no_telp_mustahiq) ? "-" : laporanDonasi.no_telp_mustahiq));
        validasiMustahiq.setText(Html.fromHtml("Status Validasi : " + (laporanDonasi.validasi_mustahiq.equalsIgnoreCase("ya") ? "<font color='#002800'>Valid</font>" : "<font color='red'>Belum/Tidak Valid</font>")));
        statusMustahiq.setText(Html.fromHtml("Status Aktif : " + (laporanDonasi.status_mustahiq.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));
        namaAmilZakat.setText("Nama Amil Zakat : " + laporanDonasi.nama_amil_zakat);

    }

    private void onDownloadFailed() {
        errorMessage.setVisibility(View.VISIBLE);
        progressCircle.setVisibility(View.GONE);
        movieHolder.setVisibility(View.GONE);
        toolbarTextHolder.setVisibility(View.GONE);
        toolbar.setTitle("");
    }

    private void onNotAvailable(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        textError.setText(message);
        tryAgain.setVisibility(View.GONE);
        progressCircle.setVisibility(View.GONE);
        movieHolder.setVisibility(View.GONE);
        toolbarTextHolder.setVisibility(View.GONE);
        toolbar.setTitle("");
    }

    @OnClick(R.id.try_again)
    public void onTryAgainClicked() {
        errorMessage.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        downloadLokasiDetails(id);
    }


    @Override
    public void onVolleyStart(String TAG) {

    }

    @Override
    public void onVolleyEnd(String TAG) {

    }

    @Override
    public void onVolleySuccessResponse(String TAG, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String isSuccess = jsonObject.getString(Zakat.isSuccess);
            String message = jsonObject.getString(Zakat.message);

            JSONObject jsDetail = new JSONObject(jsonObject.getString(Zakat.donasi));

            String id_donasi = jsDetail.getString(Zakat.id_donasi);
            String jumlah_donasi = jsDetail.getString(Zakat.jumlah_donasi);
            String id_muzaki = jsDetail.getString(Zakat.id_muzaki);
            String nama_muzaki = jsDetail.getString(Zakat.nama_muzaki);
            String alamat_muzaki = jsDetail.getString(Zakat.alamat_muzaki);
            String no_identitas_muzaki = jsDetail.getString(Zakat.no_identitas_muzaki);
            String no_telp_muzaki = jsDetail.getString(Zakat.no_telp_muzaki);
            String status_muzaki = jsDetail.getString(Zakat.status_muzaki);

            String id_mustahiq = jsDetail.getString(Zakat.id_mustahiq);
            String nama_mustahiq = jsDetail.getString(Zakat.nama_mustahiq);
            String alamat_mustahiq = jsDetail.getString(Zakat.alamat_mustahiq);
            String no_identitas_mustahiq = jsDetail.getString(Zakat.no_identitas_mustahiq);
            String no_telp_mustahiq = jsDetail.getString(Zakat.no_telp_mustahiq);
            String validasi_mustahiq = jsDetail.getString(Zakat.validasi_mustahiq);
            String status_mustahiq = jsDetail.getString(Zakat.status_mustahiq);
            String id_amil_zakat = jsDetail.getString(Zakat.id_amil_zakat);
            String nama_amil_zakat = jsDetail.getString(Zakat.nama_amil_zakat);

            laporanDonasi = new LaporanDonasi(
                    id_donasi,
                    jumlah_donasi,
                    id_muzaki,
                    nama_muzaki,
                    alamat_muzaki,
                    no_identitas_muzaki,
                    no_telp_muzaki,
                    status_muzaki,
                    id_mustahiq,
                    nama_mustahiq,
                    alamat_mustahiq,
                    no_identitas_mustahiq,
                    no_telp_mustahiq,
                    validasi_mustahiq,
                    status_mustahiq,
                    id_amil_zakat,
                    nama_amil_zakat);

            if (Boolean.parseBoolean(isSuccess))
                onDownloadSuccessful();
            else
                onNotAvailable(message);

        } catch (Exception ex) {
            // Parsing error
            onDownloadFailed();
            Log.d("Parse Error", ex.getMessage(), ex);
        }
    }

    @Override
    public void onVolleyErrorResponse(String TAG, String response) {

    }


}