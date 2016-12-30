package com.ad.zakat.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.ad.zakat.activity.ActionDonasiBaruActivity;
import com.ad.zakat.model.Mustahiq;
import com.ad.zakat.utils.ApiHelper;
import com.ad.zakat.utils.CustomVolley;
import com.ad.zakat.utils.TextUtils;
import com.ad.zakat.widget.RobotoBoldTextView;
import com.ad.zakat.widget.RobotoLightTextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.github.clans.fab.FloatingActionButton;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.json.JSONObject;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DonasiDetailFragment extends Fragment implements ManageDonasiFragment.AddEditDonasiListener, CustomVolley.OnCallbackResponse {

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
    @BindView(R.id.fab_action)
    FloatingActionButton fabAction;

    // Basic info
    @BindView(R.id.foto_profil)
    AvatarView fotoProfil;
    @BindView(R.id.nama_mustahiq)
    RobotoBoldTextView namaMustahiq;
    @BindView(R.id.alamat_mustahiq)
    RobotoLightTextView alamatMustahiq;
    @BindView(R.id.no_identitas)
    RobotoLightTextView noIdentitas;
    @BindView(R.id.no_telp_mustahiq)
    RobotoLightTextView noTelpMustahiq;
    @BindView(R.id.validasi_mustahiq)
    RobotoLightTextView validasiMustahiq;
    @BindView(R.id.status_mustahiq)
    RobotoLightTextView statusMustahiq;
    @BindView(R.id.nama_amil_zakat)
    RobotoLightTextView namaAmilZakat;
    @BindView(R.id.waktu_terakhir_donasi)
    RobotoLightTextView waktuTerakhirDonasi;


    @OnClick(R.id.fab_action)
    void DonasiMustahiq() {
        Intent i = new Intent(getActivity(), ActionDonasiBaruActivity.class);
        i.putExtra(Zakat.mustahiq, mustahiq);
        getActivity().startActivityForResult(i, 1);
    }


    private Unbinder unbinder;
    private String id;
    private Mustahiq mustahiq;
    private PicassoLoader imageLoader;
    private CustomVolley customVolley;
    private RequestQueue queue;
    private static final String TAG_DETAIL = "TAG_DETAIL";


    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mustahiq_detail, container, false);
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

        //setup fab
        fabAction.setImageDrawable(
                new IconDrawable(getActivity(), MaterialIcons.md_attach_money)
                        .colorRes(R.color.white)
                        .actionBarSize());

        // Download mustahiq details if new instance, else restore from saved instance
        if (savedInstanceState == null || !(savedInstanceState.containsKey(Zakat.MUSTAHIQ_ID)
                && savedInstanceState.containsKey(Zakat.MUSTAHIQ_OBJECT))) {
            id = getArguments().getString(Zakat.MUSTAHIQ_ID);
            if (TextUtils.isNullOrEmpty(id)) {
                progressCircle.setVisibility(View.GONE);
                toolbarTextHolder.setVisibility(View.GONE);
                toolbar.setTitle("");
            } else {
                downloadLokasiDetails(id);
            }
        } else {
            id = savedInstanceState.getString(Zakat.MUSTAHIQ_ID);
            mustahiq = savedInstanceState.getParcelable(Zakat.MUSTAHIQ_OBJECT);
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
        if (mustahiq != null && id != null) {
            outState.putString(Zakat.MUSTAHIQ_ID, id);
            outState.putParcelable(Zakat.MUSTAHIQ_OBJECT, mustahiq);
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
        String urlToDownload = ApiHelper.getMustahiqDetailLink(getActivity(), id);
        queue = customVolley.Rest(Request.Method.GET, urlToDownload, null, TAG_DETAIL);
    }

    private void onDownloadSuccessful() {

        // Toggle visibility
        progressCircle.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        movieHolder.setVisibility(View.VISIBLE);
        fabAction.setVisibility(View.VISIBLE);

        toolbar.setTitle(mustahiq.nama_mustahiq);
        toolbarTextHolder.setVisibility(View.GONE);

        imageLoader.loadImage(fotoProfil, mustahiq.nama_mustahiq, mustahiq.nama_mustahiq);
        namaMustahiq.setText("Nama : " + mustahiq.nama_mustahiq);
        alamatMustahiq.setText("Alamat : " + (TextUtils.isNullOrEmpty(mustahiq.alamat_mustahiq) ? "-" : mustahiq.alamat_mustahiq));
        noIdentitas.setText("No Identitas : " + (TextUtils.isNullOrEmpty(mustahiq.no_identitas_mustahiq) ? "-" : mustahiq.no_identitas_mustahiq));
        noTelpMustahiq.setText("No Telp : " + (TextUtils.isNullOrEmpty(mustahiq.no_telp_mustahiq) ? "-" : mustahiq.no_telp_mustahiq));
        validasiMustahiq.setText(Html.fromHtml("Status Validasi : " + (mustahiq.validasi_mustahiq.equalsIgnoreCase("ya") ? "<font color='#002800'>Valid</font>" : "<font color='red'>Belum/Tidak Valid</font>")));
        statusMustahiq.setText(Html.fromHtml("Status Aktif : " + (mustahiq.status_mustahiq.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));
        namaAmilZakat.setText("Nama Amil Zakat : " + mustahiq.nama_amil_zakat);
        waktuTerakhirDonasi.setText("Waktu Terakhir Menerima Donasi : " + (TextUtils.isNullOrEmpty(mustahiq.waktu_terakhir_donasi) ? "-" : mustahiq.waktu_terakhir_donasi));


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
        fabAction.setVisibility(View.GONE);
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

            JSONObject jsDetail = new JSONObject(jsonObject.getString(Zakat.mustahiq));
            String id_mustahiq = jsDetail.getString(Zakat.id_mustahiq);
            String nama_mustahiq = jsDetail.getString(Zakat.nama_mustahiq);
            String alamat_mustahiq = jsDetail.getString(Zakat.alamat_mustahiq);
            String no_identitas_mustahiq = jsDetail.getString(Zakat.no_identitas_mustahiq);
            String no_telp_mustahiq = jsDetail.getString(Zakat.no_telp_mustahiq);
            String validasi_mustahiq = jsDetail.getString(Zakat.validasi_mustahiq);
            String status_mustahiq = jsDetail.getString(Zakat.status_mustahiq);
            String id_amil_zakat = jsDetail.getString(Zakat.id_amil_zakat);
            String nama_amil_zakat = jsDetail.getString(Zakat.nama_amil_zakat);
            String waktu_terakhir_donasi = jsDetail.getString(Zakat.waktu_terakhir_donasi);

            mustahiq = new Mustahiq(id_mustahiq, nama_mustahiq, alamat_mustahiq, no_identitas_mustahiq, no_telp_mustahiq, validasi_mustahiq, status_mustahiq, id_amil_zakat, nama_amil_zakat,waktu_terakhir_donasi);

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

    @Override
    public void onFinishEditDonasi(Mustahiq mustahiq) {
        this.mustahiq = mustahiq;
        onDownloadSuccessful();

    }

    @Override
    public void onFinishAddDonasi(Mustahiq mustahiq) {

    }

    @Override
    public void onFinishDeleteDonasi(Mustahiq mustahiq) {

        onNotAvailable("Donasi ini tidak ada/dihapus");
    }


}