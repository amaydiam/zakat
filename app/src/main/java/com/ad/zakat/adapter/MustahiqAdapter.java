package com.ad.zakat.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;

import com.ad.zakat.R;
import com.ad.zakat.model.Mustahiq;
import com.ad.zakat.utils.TextUtils;
import com.ad.zakat.widget.RobotoBoldTextView;
import com.ad.zakat.widget.RobotoLightTextView;
import com.joanzapata.iconify.widget.IconButton;

import java.util.ArrayList;

import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MustahiqAdapter extends RecyclerView.Adapter<MustahiqAdapter.ViewHolder> implements View.OnTouchListener, View.OnClickListener {

    private final GestureDetector gestureDetector;
    public final ArrayList<Mustahiq> data;
    private boolean isTablet = false;
    private final PicassoLoader imageLoader;
    private String keyword_alamat;
    private Activity activity;
    private SparseBooleanArray mSelectedItemsIds;
    private int selected = -1;
    GradientDrawable bgShape = new GradientDrawable();


    private OnMustahiqItemClickListener OnMustahiqItemClickListener;


    public void setOnMustahiqItemClickListener(OnMustahiqItemClickListener onMustahiqItemClickListener) {
        this.OnMustahiqItemClickListener = onMustahiqItemClickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final int viewId = v.getId();
        if (viewId == R.id.btn_action) {
            if (gestureDetector.onTouchEvent(event)) {
                if (OnMustahiqItemClickListener != null) {
                    AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.playSoundEffect(SoundEffectConstants.CLICK);
                    OnMustahiqItemClickListener.onActionClick(v, (Integer) v.getTag());
                }
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (OnMustahiqItemClickListener != null) {
            OnMustahiqItemClickListener.onRootClick(v, (Integer) v.getTag());
        }
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }


    public interface OnMustahiqItemClickListener {
        void onActionClick(View v, int position);

        void onRootClick(View v, int position);

    }

    public MustahiqAdapter(Activity activity, ArrayList<Mustahiq> mustahiqList, boolean isTable) {
        this.activity = activity;
        this.data = mustahiqList;
        mSelectedItemsIds = new SparseBooleanArray();
        gestureDetector = new GestureDetector(activity, new SingleTapConfirm());
        this.isTablet = isTable;
        imageLoader = new PicassoLoader();

    }


    public void delete_all() {
        int count = getItemCount();
        if (count > 0) {
            data.clear();
            notifyDataSetChanged();
        }

    }

    public long getItemId(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.btn_action)
        IconButton btnAction;
        @BindView(R.id.root_parent)
        CardView rootParent;

        public ViewHolder(View vi) {
            super(vi);
            ButterKnife.bind(this, vi);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mustahiq_list, parent, false);
        ViewHolder holder = new ViewHolder(v);
        holder.rootParent.setOnClickListener(this);
        holder.btnAction.setOnTouchListener(this);
        return holder;
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.btnAction.setVisibility(View.GONE);
        Mustahiq mustahiq = data.get(position);
        imageLoader.loadImage(holder.fotoProfil, mustahiq.nama_mustahiq, mustahiq.nama_mustahiq);
        holder.namaMustahiq.setText("Nama : " + mustahiq.nama_mustahiq);
        holder.alamatMustahiq.setText(Html.fromHtml("Alamat : " + (!TextUtils.isNullOrEmpty(keyword_alamat) && mustahiq.alamat_mustahiq.contains(keyword_alamat) ?
                mustahiq.alamat_mustahiq.replaceAll("(?i)" + keyword_alamat, "<font color='" + ContextCompat.getColor(activity, R.color.accent) + "'>"+keyword_alamat+"</font>") : mustahiq.alamat_mustahiq)));
        holder.noIdentitas.setText("No Identitas : " + (TextUtils.isNullOrEmpty(mustahiq.no_identitas_mustahiq) ? "-" : mustahiq.no_identitas_mustahiq));
        holder.noTelpMustahiq.setText("No Telp : " + (TextUtils.isNullOrEmpty(mustahiq.no_telp_mustahiq) ? "-" : mustahiq.no_telp_mustahiq));
        holder.validasiMustahiq.setText(Html.fromHtml("Status Validasi : " + (mustahiq.validasi_mustahiq.equalsIgnoreCase("ya") ? "<font color='#002800'>Valid</font>" : "<font color='red'>Belum/Tidak Valid</font>")));
        holder.statusMustahiq.setText(Html.fromHtml("Status Aktif : " + (mustahiq.status_mustahiq.equalsIgnoreCase("aktif") ? "<font color='#002800'>Aktif</font>" : "<font color='red'>Tidak Aktif</font>")));
        holder.namaAmilZakat.setText("Nama Amil Zakat : " + mustahiq.nama_amil_zakat);
        holder.waktuTerakhirDonasi.setText("Waktu Terakhir Menerima Donasi : " + (TextUtils.isNullOrEmpty(mustahiq.waktu_terakhir_donasi) ? "-" : mustahiq.waktu_terakhir_donasi));

        if (isTablet) {
            if (selected == position)
                holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_selected_background));
            else
                holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_background));
        } else {
            holder.rootParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.card_background));

        }

        holder.btnAction.setTag(position);
        holder.rootParent.setTag(position);

    }

    public int getItemCount() {
        return data.size();
    }

    /**
     * Here is the key method to apply the animation
     */

    public void remove(int position) {
        data.remove(data.get(position));
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }


    }

    public void setValSearchAlamat(String keyword_alamat) {
        this.keyword_alamat = keyword_alamat;
        notifyDataSetChanged();
    }

}
