package com.ad.zakat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ad.zakat.R;
import com.ad.zakat.R2;
import com.ad.zakat.Zakat;
import com.ad.zakat.utils.Menus;
import com.ad.zakat.utils.Prefs;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.MaterialCommunityIcons;
import com.joanzapata.iconify.fonts.MaterialIcons;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;

public class DrawerFragment extends Fragment implements OnMenuItemClickListener, OnNavigationItemSelectedListener {

    private Fragment fragment;
    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    public
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    private MenuItem prevMenuItem;

    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drawer, container, false);
        unbinder = ButterKnife.bind(this, v);

        // Setup toolbar
        toolbar.inflateMenu(R.menu.menu_zakat);
        toolbar.setTitle(getActivity().getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        // Setup navigation drawer
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);

        SetMenuDrawer();

        if (Prefs.getUrl(getActivity()) == null)
            SetUrl();

        actionBarDrawerToggle.syncState();

        // Load previously selected drawer item
        int view_type = Prefs.getLastSelected(getActivity());
        if (savedInstanceState == null) {
            setSelectedDrawerItem(view_type);
        } else {
            fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Zakat.TAG_GRID_FRAGMENT);
            if (savedInstanceState.containsKey(Zakat.TOOLBAR_TITLE)) {
                toolbar.setSubtitle(savedInstanceState.getString(Zakat.TOOLBAR_TITLE));
            } else {
                toolbar.setSubtitle(navigationView.getMenu().findItem(view_type).getTitle());
            }
        }
        return v;
    }

    private void SetMenuDrawer() {

        // ============ list menu drawer ==============
        Menu menu = navigationView.getMenu();//donasi
        MenuItem drawer_donasi = menu.findItem(R.id.drawer_donasi);
        drawer_donasi.setIcon(
                new IconDrawable(getActivity(), MaterialIcons.md_attach_money)
                        .actionBarSize());
//donasi
        MenuItem drawer_laporan_donasi = menu.findItem(R.id.drawer_laporan_donasi);
        drawer_laporan_donasi.setIcon(new IconDrawable(getActivity(), MaterialCommunityIcons.mdi_file_document).actionBarSize());

        //mustahiq
        MenuItem drawer_mustahiq = menu.findItem(R.id.drawer_mustahiq);
        drawer_mustahiq.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_user).actionBarSize());

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Zakat.TOOLBAR_TITLE, toolbar.getTitle().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // Toolbar action menu
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_set_url:

                SetUrl();

                return true;

            default:
                return false;
        }
    }

    private void SetUrl() {

        FragmentManager fragmentManager = getChildFragmentManager();
        SetUrlFragment setUrlFragment = new SetUrlFragment();
        setUrlFragment.setTargetFragment(this, 0);
        setUrlFragment.setDialogTitle("Set Url");
        setUrlFragment.show(fragmentManager, "Set Url");
    }


    // Drawer item selection
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawers();
        int id = item.getItemId();
        switch (id) {
            case Menus.DRAWER_DONASI:
                setSelectedDrawerItem(Zakat.VIEW_TYPE_DONASI);
                return true;
            case Menus.DRAWER_LAPORAN_DONASI:
                setSelectedDrawerItem(Zakat.VIEW_TYPE_LAPORAN_DONASI);
                return true;
            case Menus.DRAWER_MUSTAHIQ:
                setSelectedDrawerItem(Zakat.VIEW_TYPE_MUSTAHIQ);
                return true;
            default:
                return false;
        }
    }


    public void setSelectedDrawerItem(int view_type) {
        int id;
        switch (view_type) {
            case Zakat.VIEW_TYPE_DONASI:
                id = Menus.DRAWER_DONASI;
                fragment = new DonasiListFragment();
                break;
            case Zakat.VIEW_TYPE_LAPORAN_DONASI:
                id = Menus.DRAWER_LAPORAN_DONASI;
                fragment = new LaporanDonasiListFragment();
                break;
            case Menus.DRAWER_MUSTAHIQ:
                id = Menus.DRAWER_MUSTAHIQ;
                fragment = new MustahiqListFragment();
                break;
            default:
                id = Menus.DRAWER_MUSTAHIQ;
                fragment = new MustahiqListFragment();
                break;
        }
        MenuItem item = navigationView.getMenu().findItem(id);
        if (prevMenuItem != null) {
            prevMenuItem.setChecked(false);
        }

        item.setChecked(true);
        prevMenuItem = item;
        toolbar.setSubtitle(item.getTitle());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment, Zakat.TAG_GRID_FRAGMENT);
        transaction.commitAllowingStateLoss();
        Prefs.putLastSelected(getActivity(), view_type);
    }


    @Override
    public void onResume() {
        super.onResume();
        SetMenuDrawer();

    }
}
