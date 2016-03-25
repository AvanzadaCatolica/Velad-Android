package com.mac.velad;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mac.velad.diary.DiaryFragment;
import com.mac.velad.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_today);
        navigationView.setNavigationItemSelectedListener(this);

        navigate(R.id.nav_today);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        navigate(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigate(int menuId) {
        getSupportActionBar().setTitle(getTitle(menuId));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, getFragment(menuId)).commit();
    }

    private String getTitle(int menuId) {
        switch (menuId) {
            case R.id.nav_today: return getString(R.string.menu_item_title_today);
            case R.id.nav_diary: return getString(R.string.menu_item_title_diary);
            case R.id.nav_week: return getString(R.string.menu_item_title_week);
            case R.id.nav_reports: return getString(R.string.menu_item_title_reports);
            case R.id.nav_settings: return getString(R.string.menu_item_title_settings);
            default: return null;
        }
    }

    private Fragment getFragment(int menuId) {
        switch (menuId) {
            case R.id.nav_today: return TodayFragment.newInstance();
            case R.id.nav_diary: return DiaryFragment.newInstance();
            case R.id.nav_week: return WeekFragment.newInstance();
            case R.id.nav_reports: return ReportsFragment.newInstance();
            case R.id.nav_settings: return SettingsFragment.newInstance();
            default: return null;
        }
    }
}
