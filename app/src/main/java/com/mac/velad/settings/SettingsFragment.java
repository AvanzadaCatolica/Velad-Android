package com.mac.velad.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mac.velad.R;
import com.mac.velad.SplashActivity;
import com.mac.velad.general.CalendarHelper;
import com.mac.velad.general.DividerItemDecoration;
import com.mac.velad.general.ItemClickSupport;
import com.mac.velad.general.VerticalSpaceItemDecoration;
import com.mac.velad.today.Encouragement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SettingsFragment extends Fragment implements EncouragementDialogFragment.EncouragementDialogFragmentListener{

    private RecyclerView recyclerView;
    private SettingAdapter adapter;
    private List<Setting> dataSet = new ArrayList<>();

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setupDataSet();
        setupFloatingButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setupRecyclerView(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab == null) {
            return;
        }
        if (fab.isShown()) {
            fab.hide();
        }
    }

    private String getEncouragementStatus(Encouragement encouragement) {
        String encouragementStatus;
        if (encouragement.isEnabled()) {
            encouragementStatus = encouragement.getPercentage() + "%";
        } else {
            encouragementStatus = "Desactivado";
        }
        return encouragementStatus;
    }

    private void setupDataSet() {
        String versionName;
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = getString(R.string.undefined);
        }

        Setting profile = new Setting(getString(R.string.settings_profile_title), Setting.SettingType.SETTING_TYPE_NORMAL);
        dataSet.add(profile);
        Setting cheer = new InfoSetting(getString(R.string.settings_cheer_title), Setting.SettingType.SETTING_TYPE_INFO, getEncouragementStatus(Encouragement.getEncouragement()));
        dataSet.add(cheer);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        boolean start = sharedPreferences.getBoolean(CalendarHelper.SHARED_PREFERENCES_START_MONDAY, false);
        Setting monday = new BooleanSetting(getString(R.string.settings_monday_title), Setting.SettingType.SETTING_TYPE_BOOLEAN, start, CalendarHelper.SHARED_PREFERENCES_START_MONDAY);
        monday.setDetails(getString(R.string.settings_monday_details));
        dataSet.add(monday);

        Setting restore = new ActionSetting(getString(R.string.settings_restore_title), Setting.SettingType.SETTING_TYPE_ACTION);
        dataSet.add(restore);

        Setting opinion = new Setting(getString(R.string.settings_opinion_title), Setting.SettingType.SETTING_TYPE_NORMAL);
        dataSet.add(opinion);

        Setting licenses = new Setting(getString(R.string.settings_licenses_title), Setting.SettingType.SETTING_TYPE_NORMAL);
        dataSet.add(licenses);
        Setting openSource = new Setting(getString(R.string.settings_open_source_title), Setting.SettingType.SETTING_TYPE_NORMAL);
        dataSet.add(openSource);
        Setting version = new InfoSetting(getString(R.string.settings_version_title), Setting.SettingType.SETTING_TYPE_INFO, versionName);
        dataSet.add(version);
    }

    private void setupRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SettingAdapter(getContext(), getChildFragmentManager(), dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(90, Arrays.asList(2, 3, 4));
        recyclerView.addItemDecoration(verticalSpaceItemDecoration);

        ItemClickSupport support = ItemClickSupport.addTo(recyclerView);
        support.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View view) {
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(getContext(), ProfileActivity.class);
                        intent.putExtra(SplashActivity.FIRST_LAUNCH, false);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Encouragement encouragement = Encouragement.getEncouragement();
                        EncouragementDialogFragment dialogFragment = EncouragementDialogFragment.newInstance(encouragement.isEnabled() ? encouragement.getPercentage() : null);
                        dialogFragment.show(getChildFragmentManager(), EncouragementDialogFragment.class.toString());
                    }
                }
            }
        });
    }

    @Override
    public void onSave(int value) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Encouragement encouragement = Encouragement.getEncouragement();
        encouragement.setPercentage(value);
        encouragement.setEnabled(true);
        realm.copyToRealm(encouragement);

        realm.commitTransaction();

        InfoSetting cheer = (InfoSetting) dataSet.get(1);
        cheer.setInfo(getEncouragementStatus(encouragement));
        adapter.notifyItemChanged(1);
    }

    @Override
    public void onDeactivate() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Encouragement encouragement = Encouragement.getEncouragement();
        encouragement.setEnabled(false);
        realm.copyToRealm(encouragement);

        realm.commitTransaction();

        InfoSetting cheer = (InfoSetting) dataSet.get(1);
        cheer.setInfo(getEncouragementStatus(encouragement));
        adapter.notifyItemChanged(1);
    }
}
