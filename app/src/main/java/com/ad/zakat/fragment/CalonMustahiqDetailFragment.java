package com.ad.zakat.fragment;

import android.app.ProgressDialog;
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
import com.ad.zakat.Zakat;
import com.ad.zakat.model.CalonMustahiq;
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
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CalonMustahiqDetailFragment extends Fragment implements ManageCalonMustahiqFragment.AddEditCalonMustahiqListener, CustomVolley.OnCallbackResponse {

    private static final String TAG_DETAIL = "TAG_DETAIL";
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
    @BindView(R.id.foto_profil)
    AvatarView fotoProfil;
    @BindView(R.id.nama_calon_mustahiq)
    RobotoBoldTextView namaCalonMustahiq;
    @BindView(R.id.alamat_calon_mustahiq)
    RobotoLightTextView alamatCalonMustahiq;
    @BindView(R.id.no_identitas_calon_mustahiq)
    RobotoLightTextView noIdentitasCalonMustahiq;
    @BindView(R.id.no_telp_calon_mustahiq)
    RobotoLightTextView noTelpCalonMustahiq;
    @BindView(R.id.status_calon_mustahiq)
    RobotoLightTextView statusCalonMustahiq;
    private ProgressDialog dialogProgress;
    private Unbinder unbinder;
    private String id;
    private CalonMustahiq calonMustahiq;
    private PicassoLoader imageLoader;
    private CustomVolley customVolley;
    private RequestQueue queue;

    @OnClick(R.id.fab_action)
    void EditMustahiq() {

        FragmentManager fragmentManager = getChildFragmentManager();
        ManageCalonMustahiqFragment manageMustahiq = new ManageCalonMustahiqFragment();
        manageMustahiq.setTargetFragment(this, 0);
        manageMustahiq.setCancelable(false);
        manageMustahiq.setDialogTitle("Calon Mustahiq");
        manageMustahiq.setAction("edit");
        manageMustahiq.setData(calonMustahiq);
        manageMustahiq.show(fragmentManager, "Manage Calon Mustahiq");
    }

    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calon_mustahiq_detail, container, false);
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
                new IconDrawable(getActivity(), MaterialIcons.md_edit)
                        .colorRes(R.color.white)
                        .actionBarSize());

        // Download calon_mustahiq details if new instance, else restore from saved instance
        if (savedInstanceState == null || !(savedInstanceState.containsKey(Zakat.CALON_MUSTAHIQ_ID)
                && savedInstanceState.containsKey(Zakat.CALON_MUSTAHIQ_OBJECT))) {
            id = getArguments().getString(Zakat.CALON_MUSTAHIQ_ID);
            if (TextUtils.isNullOrEmpty(id)) {
                progressCircle.setVisibility(View.GONE);
                toolbarTextHolder.setVisibility(View.GONE);
                toolbar.setTitle("");
            } else {
                downloadLokasiDetails(id);
            }
        } else {
            id = savedInstanceState.getString(Zakat.CALON_MUSTAHIQ_ID);
            calonMustahiq = savedInstanceState.getParcelable(Zakat.CALON_MUSTAHIQ_OBJECT);
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
        if (calonMustahiq != null && id != null) {
            outState.putString(Zakat.CALON_MUSTAHIQ_ID, id);
            outState.putParcelable(Zakat.CALON_MUSTAHIQ_OBJECT, calonMustahiq);
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
        String urlToDownload = ApiHelper.getCalonMustahiqDetailLink(getActivity(), id);
        queue = customVolley.Rest(Request.Method.GET, urlToDownload, null, TAG_DETAIL);
    }

    private void onDownloadSuccessful() {

        // Toggle visibility
        progressCircle.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        movieHolder.setVisibility(View.VISIBLE);
        fabAction.setVisibility(View.VISIBLE);

        toolbar.setTitle(calonMustahiq.nama_calon_mustahiq);
        toolbarTextHolder.setVisibility(View.GONE);

        imageLoader.loadImage(fotoProfil, calonMustahiq.nama_calon_mustahiq, calonMustahiq.nama_calon_mustahiq);
        namaCalonMustahiq.setText("Nama : " + calonMustahiq.nama_calon_mustahiq);
        alamatCalonMustahiq.setText("Alamat : " + (TextUtils.isNullOrEmpty(calonMustahiq.alamat_calon_mustahiq) ? "-" : calonMustahiq.alamat_calon_mustahiq));
        noIdentitasCalonMustahiq.setText("No Identitas : " + (TextUtils.isNullOrEmpty(calonMustahiq.no_identitas_calon_mustahiq) ? "-" : calonMustahiq.no_identitas_calon_mustahiq));
        noTelpCalonMustahiq.setText("No Telp : " + (TextUtils.isNullOrEmpty(calonMustahiq.no_telp_calon_mustahiq) ? "-" : calonMustahiq.no_telp_calon_mustahiq));
        statusCalonMustahiq.setText(Html.fromHtml("Status Aktif : " + (calonMustahiq.status_calon_mustahiq.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));

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
        if (TAG.equals(TAG_DETAIL)) {
        }
    }

    @Override
    public void onVolleyEnd(String TAG) {
        if (TAG.equals(TAG_DETAIL)) {
        }
    }

    @Override
    public void onVolleySuccessResponse(String TAG, String response) {
        if (TAG.equals(TAG_DETAIL)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String isSuccess = jsonObject.getString(Zakat.isSuccess);
                String message = jsonObject.getString(Zakat.message);
                JSONObject jsDetail = new JSONObject(jsonObject.getString(Zakat.calon_mustahiq));
                String id_calon_mustahiq = jsDetail.getString(Zakat.id_calon_mustahiq);
                String nama_calon_mustahiq = jsDetail.getString(Zakat.nama_calon_mustahiq);
                String alamat_calon_mustahiq = jsDetail.getString(Zakat.alamat_calon_mustahiq);
                String no_identitas_calon_mustahiq = jsDetail.getString(Zakat.no_identitas_calon_mustahiq);
                String no_telp_calon_mustahiq = jsDetail.getString(Zakat.no_telp_calon_mustahiq);
                String status_calon_mustahiq = jsDetail.getString(Zakat.status_calon_mustahiq);

                calonMustahiq = new CalonMustahiq(id_calon_mustahiq, nama_calon_mustahiq, alamat_calon_mustahiq, no_identitas_calon_mustahiq, no_telp_calon_mustahiq, status_calon_mustahiq);

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
    }

    @Override
    public void onVolleyErrorResponse(String TAG, String response) {
        if (TAG.equals(TAG_DETAIL)) {
            TastyToast.makeText(getActivity(), "Error ..", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    @Override
    public void onFinishEditCalonMustahiq(CalonMustahiq calon_mustahiq) {
        this.calonMustahiq = calon_mustahiq;
        onDownloadSuccessful();

    }

    @Override
    public void onFinishAddCalonMustahiq(CalonMustahiq calon_mustahiq) {

    }

    @Override
    public void onFinishDeleteCalonMustahiq(CalonMustahiq calon_mustahiq) {

        onNotAvailable("Mustahiq ini tidak ada/dihapus");
    }


}