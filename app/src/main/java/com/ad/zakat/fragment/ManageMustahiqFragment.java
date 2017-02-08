package com.ad.zakat.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.ad.zakat.R;
import com.ad.zakat.Zakat;
import com.ad.zakat.adapter.SpinnerAmilZakatAdapter;
import com.ad.zakat.model.AmilZakat;
import com.ad.zakat.model.CalonMustahiq;
import com.ad.zakat.model.Mustahiq;
import com.ad.zakat.utils.ApiHelper;
import com.ad.zakat.utils.CustomVolley;
import com.ad.zakat.utils.Menus;
import com.ad.zakat.utils.SnackBar;
import com.ad.zakat.widget.RobotoRegularTextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ManageMustahiqFragment extends DialogFragment implements CustomVolley.OnCallbackResponse, DialogPickCalonMustahiqFragment.PickCalonMustahiqListener {
    private static final String TAG_ADD = "TAG_ADD";
    private static final String TAG_EDIT = "TAG_EDIT";
    private static final String TAG_DELETE = "TAG_DELETE";
    String title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.calon_mustahiq)
    LinearLayout calonMustahiq;
    @BindView(R.id.btn_pilih_calon_mustahiq)
    Button btnPilihCalonMustahiq;
    @BindView(R.id.alamat_calon_mustahiq)
    RobotoRegularTextView alamatCalonMustahiq;
    @BindView(R.id.no_identitas_calon_mustahiq)
    RobotoRegularTextView noIdentitasCalonMustahiq;
    @BindView(R.id.no_telp_calon_mustahiq)
    RobotoRegularTextView noTelpCalonMustahiq;
    @BindView(R.id.id_amil_zakat)
    Spinner idAmilZakat;
    @BindView(R.id.aktif)
    RadioButton aktif;
    @BindView(R.id.tidak_aktif)
    RadioButton tidakAktif;
    @BindView(R.id.status_mustahiq)
    RadioGroup statusMustahiq;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private SnackBar snackbar;
    private CustomVolley customVolley;
    private RequestQueue queue;
    private ProgressDialog dialogProgress;
    private Unbinder butterKnife;
    private String val_id_mustahiq = "";
    private String val_id_calon_mustahiq = "";
    private String val_nama_calon_mustahiq = "";
    private String val_alamat_calon_mustahiq = "";
    private String val_no_identitas_calon_mustahiq = "";
    private String val_no_telp_calon_mustahiq = "";
    private String val_status_mustahiq = "";
    private String val_id_amil_zakat = "";
    private Mustahiq mustahiq;
    private Dialog alertDialog;
    private AddEditMustahiqListener callback;
    private ArrayList<AmilZakat> amilZakatList = new ArrayList<>();
    private SpinnerAmilZakatAdapter adapter;
    private String action;

    public ManageMustahiqFragment() {

    }

    @OnClick(R.id.btn_pilih_calon_mustahiq)
    void Pick() {

        FragmentManager fragmentManager = getChildFragmentManager();
        DialogPickCalonMustahiqFragment pickCalonMustahiqFragment = new DialogPickCalonMustahiqFragment();
        pickCalonMustahiqFragment.setTargetFragment(this, 0);
        pickCalonMustahiqFragment.setCancelable(false);
        pickCalonMustahiqFragment.show(fragmentManager, "Pick Calon Mustahiq");
    }

    void Action(int id) {

        switch (id) {

            case Menus.SEND:
                getData();

                if (val_id_calon_mustahiq.length() == 0
                        || val_nama_calon_mustahiq.length() == 0
                        || val_alamat_calon_mustahiq.length() == 0
                        || val_no_identitas_calon_mustahiq.length() == 0
                        || val_no_telp_calon_mustahiq.length() == 0
                        || val_id_amil_zakat.length() == 0
                        || val_status_mustahiq.length() == 0) {
                    snackbar.show("Harap isi semua form...");
                    return;
                }

                Map<String, String> jsonParams = new HashMap<>();

                jsonParams.put(Zakat.id_calon_mustahiq,
                        val_id_calon_mustahiq);
                jsonParams.put(Zakat.status_mustahiq,
                        val_status_mustahiq);
                jsonParams.put(Zakat.id_amil_zakat,
                        val_id_amil_zakat);

                String TAG = null;
                if (action.equals("add")) {
                    TAG = TAG_ADD;
                } else if (action.equals("edit")) {
                    TAG = TAG_EDIT;
                    jsonParams.put(Zakat.id_mustahiq,
                            val_id_mustahiq);
                }

                queue = customVolley.Rest(Request.Method.POST, ApiHelper.getMustahiqAddEditLink(getActivity()), jsonParams, TAG);

                //  dismiss();
                break;
            case Menus.DELETE:
                ConfirmDelete();
                break;
        }
    }

    private void getData() {
        if (statusMustahiq.getCheckedRadioButtonId() == R.id.aktif)
            val_status_mustahiq = "Aktif";
        else
            val_status_mustahiq = "Tidak Aktif";

        val_id_amil_zakat = amilZakatList.get(idAmilZakat.getSelectedItemPosition()).id_amil_zakat;


    }

    private void ConfirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Anda yakin akan menghapus mustahiq ini?");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                queue = customVolley.Rest(Request.Method.GET, ApiHelper.getMustahiqDeleteLink(getActivity(), val_id_mustahiq), null, TAG_DELETE);

            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
        if (queue != null) {
            queue.cancelAll(TAG_ADD);
            queue.cancelAll(TAG_DELETE);
            queue.cancelAll(TAG_EDIT);
        }

    }

    @Override
    public void onVolleyStart(String TAG) {
        progress(true);
    }

    @Override
    public void onVolleyEnd(String TAG) {
        progress(false);
    }

    @Override
    public void onVolleySuccessResponse(String TAG, String response) {

        try {

            JSONObject json = new JSONObject(response);
            String res = json.getString(Zakat.isSuccess);
            String message = json.getString(Zakat.message);
            if (Boolean.valueOf(res)) {
                if (!TAG.equals(TAG_DELETE)) {
                    JSONObject obj = new JSONObject(json.getString(Zakat.mustahiq));
                    String id_mustahiq = obj.getString(Zakat.id_mustahiq);
                    String id_calon_mustahiq = obj.getString(Zakat.id_calon_mustahiq);
                    String nama_calon_mustahiq = obj.getString(Zakat.nama_calon_mustahiq);
                    String alamat_calon_mustahiq = obj.getString(Zakat.alamat_calon_mustahiq);
                    String no_identitas_calon_mustahiq = obj.getString(Zakat.no_identitas_calon_mustahiq);
                    String no_telp_calon_mustahiq = obj.getString(Zakat.no_telp_calon_mustahiq);
                    String status_mustahiq = obj.getString(Zakat.status_mustahiq);
                    String id_amil_zakat = obj.getString(Zakat.id_amil_zakat);
                    String nama_amil_zakat = obj.getString(Zakat.nama_amil_zakat);
                    String waktu_terakhir_donasi = obj.getString(Zakat.waktu_terakhir_donasi);

                    mustahiq = new Mustahiq(id_mustahiq, id_calon_mustahiq, nama_calon_mustahiq, alamat_calon_mustahiq, no_identitas_calon_mustahiq, no_telp_calon_mustahiq, status_mustahiq, id_amil_zakat, nama_amil_zakat, waktu_terakhir_donasi);
                    if (TAG.equals(TAG_ADD)) {
                        callback.onFinishAddMustahiq(mustahiq);
                    } else if (TAG.equals(TAG_EDIT)) {
                        callback.onFinishEditMustahiq(mustahiq);
                    }
                } else {
                    callback.onFinishDeleteMustahiq(mustahiq);
                }
                dismiss();
                snackbar.show(message);
            } else {
                snackbar.show(message);
            }
        } catch (Exception e) {
            snackbar.show("error parsing data");
            Log.v("error", e.getMessage());
        }
    }

    @Override
    public void onVolleyErrorResponse(String TAG, String response) {
        snackbar.show(response);
    }

    public void setAmilZakat(ArrayList<AmilZakat> dataAmilZakat) {
        this.amilZakatList = dataAmilZakat;
        AmilZakat amilZakat = new AmilZakat("", "-Pilih Amil-");
        amilZakatList.add(0, amilZakat);
    }

    @Override
    public void onFinishPickCalonMustahiqL(CalonMustahiq calon_Mustahiq) {

        val_id_calon_mustahiq = calon_Mustahiq.id_calon_mustahiq;
        val_nama_calon_mustahiq = calon_Mustahiq.nama_calon_mustahiq;
        val_alamat_calon_mustahiq = calon_Mustahiq.alamat_calon_mustahiq;
        val_no_identitas_calon_mustahiq = calon_Mustahiq.no_identitas_calon_mustahiq;
        val_no_telp_calon_mustahiq = calon_Mustahiq.no_telp_calon_mustahiq;

        LayoutInflater inflaterView = LayoutInflater.from(getActivity());
        LinearLayout IF = (LinearLayout) inflaterView.inflate(R.layout.item_add_mustahiq, null, false);
        calonMustahiq.removeAllViews();
        calonMustahiq.addView(IF);

        final PicassoLoader imageLoader
                = new PicassoLoader();
        final AvatarView fotoProfil = (AvatarView) IF.findViewById(R.id.foto_profil);
        final TextView namaCalonMustahiq = (TextView) IF.findViewById(R.id.nama_calon_mustahiq);
        imageLoader.loadImage(fotoProfil, val_nama_calon_mustahiq, val_nama_calon_mustahiq);
        namaCalonMustahiq.setText(val_nama_calon_mustahiq);

        alamatCalonMustahiq.setText(val_alamat_calon_mustahiq);
        noIdentitasCalonMustahiq.setText(val_no_identitas_calon_mustahiq);
        noTelpCalonMustahiq.setText(val_no_telp_calon_mustahiq);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (AddEditMustahiqListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement KonfirmasiPendaftaranPesertaListener");
        }
    }

    public void setDialogTitle(String title) {
        this.title = title;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setData(Mustahiq mustahiq) {
        this.mustahiq = mustahiq;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.content_manage_mustahiq, container);

        butterKnife = ButterKnife.bind(this, view);
        customVolley = new CustomVolley(getActivity());
        customVolley.setOnCallbackResponse(this);
        snackbar = new SnackBar(getActivity(), coordinatorLayout);
        toolbar.setTitle(title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Action(item.getItemId());
                return true;
            }
        });
        toolbar.setNavigationIcon(
                new IconDrawable(getActivity(), MaterialIcons.md_close)
                        .colorRes(R.color.white)
                        .actionBarSize());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.inflateMenu(R.menu.menu_manage);
        Menu menu = toolbar.getMenu();
        MenuItem _send = menu.findItem(Menus.SEND);
        MenuItem _delete = menu.findItem(Menus.DELETE);
        _send.setIcon(
                new IconDrawable(getActivity(), MaterialIcons.md_send)
                        .colorRes(R.color.white)
                        .actionBarSize());
        _delete.setIcon(
                new IconDrawable(getActivity(), MaterialIcons.md_delete)
                        .colorRes(R.color.white)
                        .actionBarSize());

        // Spinner adapterMustahiq
        adapter = new SpinnerAmilZakatAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                amilZakatList);

        idAmilZakat.setAdapter(adapter);

        // Spinner on item click listener
        idAmilZakat
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int position, long arg3) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

        if (action.equals("edit")) {
            toolbar.setSubtitle("Ubah");
            _delete.setVisible(true);


            val_id_mustahiq = mustahiq.id_mustahiq;
            val_id_calon_mustahiq = mustahiq.id_calon_mustahiq;
            val_nama_calon_mustahiq = mustahiq.nama_calon_mustahiq;
            val_alamat_calon_mustahiq = mustahiq.alamat_calon_mustahiq;
            val_no_identitas_calon_mustahiq = mustahiq.no_identitas_calon_mustahiq;
            val_no_telp_calon_mustahiq = mustahiq.no_telp_calon_mustahiq;
            val_status_mustahiq = mustahiq.status_mustahiq;
            val_id_amil_zakat = mustahiq.id_amil_zakat;

            LayoutInflater inflaterView = LayoutInflater.from(getActivity());
            LinearLayout IF = (LinearLayout) inflaterView.inflate(R.layout.item_add_mustahiq, null, false);
            calonMustahiq.addView(IF);

            final PicassoLoader imageLoader
                    = new PicassoLoader();
            final AvatarView fotoProfil = (AvatarView) IF.findViewById(R.id.foto_profil);
            final TextView namaCalonMustahiq = (TextView) IF.findViewById(R.id.nama_calon_mustahiq);
            imageLoader.loadImage(fotoProfil, val_nama_calon_mustahiq, val_nama_calon_mustahiq);
            namaCalonMustahiq.setText(val_nama_calon_mustahiq);

            alamatCalonMustahiq.setText(val_alamat_calon_mustahiq);
            noIdentitasCalonMustahiq.setText(val_no_identitas_calon_mustahiq);
            noTelpCalonMustahiq.setText(val_no_telp_calon_mustahiq);

            btnPilihCalonMustahiq.setVisibility(View.GONE);

            if (val_status_mustahiq.equalsIgnoreCase("Aktif"))
                aktif.setChecked(true);
            else
                tidakAktif.setChecked(true);

            for (int i = 0; i < amilZakatList.size(); i++) {
                if (amilZakatList.get(i).id_amil_zakat.equals(val_id_amil_zakat)) {
                    idAmilZakat.setSelection(i);
                }
            }

        } else {
            toolbar.setSubtitle("Tambah");
            _delete.setVisible(false);
        }

        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    private void progress(boolean status) {
        if (status) {
            dialogProgress = ProgressDialog.show(getActivity(), "Submit",
                    "Harap menunggu...", true);
        } else {

            if (dialogProgress.isShowing())
                dialogProgress.dismiss();
        }
    }


    public interface AddEditMustahiqListener {
        void onFinishEditMustahiq(Mustahiq mustahiq);

        void onFinishAddMustahiq(Mustahiq mustahiq);

        void onFinishDeleteMustahiq(Mustahiq mustahiq);
    }


}