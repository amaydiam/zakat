package com.ad.zakat.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ad.zakat.R;
import com.ad.zakat.R2;
import com.ad.zakat.Zakat;
import com.ad.zakat.fragment.DialogDetailDonasiFragment;
import com.ad.zakat.fragment.DonasiDetailFragment;
import com.ad.zakat.fragment.DrawerFragment;
import com.ad.zakat.fragment.LaporanDonasiDetailFragment;
import com.ad.zakat.fragment.ManageMustahiqFragment;
import com.ad.zakat.fragment.MustahiqDetailFragment;
import com.ad.zakat.model.LaporanDonasi;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;

import butterknife.BindBool;
import butterknife.ButterKnife;

import static com.ad.zakat.Zakat.mustahiq;

public class DrawerActivity extends AppCompatActivity {

    @BindBool(R.bool.is_tablet)
    boolean isTablet;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule());
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        if (isTablet && savedInstanceState == null) {
            loadDetailMustahiqFragmentWith("null");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        hideSoftKeyboard();
    }

    public void loadDetailMustahiqFragmentWith(String id_mustahiq) {
        MustahiqDetailFragment fragment = new MustahiqDetailFragment();
        Bundle args = new Bundle();
        args.putString(Zakat.MUSTAHIQ_ID, id_mustahiq);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();
    }


    public void loadDetailDonasiFragmentWith(String id_mustahiq) {
        DonasiDetailFragment fragment = new DonasiDetailFragment();
        Bundle args = new Bundle();
        args.putString(Zakat.MUSTAHIQ_ID, id_mustahiq);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();
    }


    public void loadDetailLaporanDonasiFragmentWith(String id_donasi) {
        LaporanDonasiDetailFragment fragment = new LaporanDonasiDetailFragment();
        Bundle args = new Bundle();
        args.putString(Zakat.DONASI_ID, id_donasi);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, fragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Iconify
                .with(new MaterialModule())
                .with(new MaterialCommunityModule());
    }

    @Override
    public void onBackPressed() {

        DrawerFragment fragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (fragment != null) {

            if (fragment.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                fragment.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Tekan sekali lagi untuk keluar.", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } else {
            super.onBackPressed();
            return;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                LaporanDonasi laporanDonasi = (LaporanDonasi) data.getParcelableExtra(Zakat.LAPORAN_DONASI_OBJECT);
                if (laporanDonasi != null) {

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    DialogDetailDonasiFragment dialogDetailDonasiFragment = new DialogDetailDonasiFragment();
                    dialogDetailDonasiFragment.setCancelable(false);
                    dialogDetailDonasiFragment.setData(laporanDonasi);
                    ft.add(dialogDetailDonasiFragment, null);
                    ft.commitAllowingStateLoss();
                }


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
