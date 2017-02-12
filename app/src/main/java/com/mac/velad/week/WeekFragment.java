package com.mac.velad.week;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mac.velad.R;
import com.mac.velad.general.CalendarHelper;
import com.mac.velad.general.DateIntervalPickerFragment;
import com.mac.velad.general.DividerItemDecoration;
import com.mac.velad.settings.Profile;
import com.mac.velad.today.BasicPoint;
import com.mac.velad.today.groups.Group;
import com.mac.velad.today.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;

public class WeekFragment extends Fragment implements DateIntervalPickerFragment.DateIntervalPickerFragmentListener {

    public WeekFragment() {
        // Required empty public constructor
    }

    private List dataSet = new ArrayList();
    private RecyclerView recyclerView;
    private WeekAdapter adapter;
    private LinearLayout contentEmpty;

    public static WeekFragment newInstance() {
        WeekFragment fragment = new WeekFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setupDateIntervalPicker();
        setupDataSet();
        setupFloatingButton();
    }

    private void setupDataSet() {
        dataSet.clear();
        DateIntervalPickerFragment fragment = (DateIntervalPickerFragment) getChildFragmentManager().findFragmentByTag(DateIntervalPickerFragment.class.toString());
        Date selectedStartDate;
        Date selectedEndDate;
        if (fragment == null) {
            selectedStartDate = CalendarHelper.coldCurrentStartWeekDate(getContext());
            selectedEndDate = CalendarHelper.coldCurrentEndWeekDate(getContext());
        } else {
            selectedStartDate = fragment.getSelectedStartDate();
            selectedEndDate = fragment.getSelectedEndDate();
        }

        RealmResults<Group> groups = Group.getAll();
        for (Group group :groups) {
            for (BasicPoint basicPoint : group.getBasicPoints()) {
                if (basicPoint.isEnabled()) {
                    if (!dataSet.contains(group)) {
                        dataSet.add(group);
                    }
                    RealmResults<Record> records = Record.getRecords(basicPoint, selectedStartDate, selectedEndDate);
                    WeekViewModel viewModel = new WeekViewModel(basicPoint, records.size());
                    dataSet.add(viewModel);
                }
            }
        }
    }

    private void setupDateIntervalPicker() {
        getChildFragmentManager().beginTransaction()
                .add(R.id.frame_date_picker, DateIntervalPickerFragment.newInstance(), DateIntervalPickerFragment.class.toString()).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        setupEmptyView(view);
        setupRecyclerView(view);
        updateContentEmpty();
        return view;
    }

    private void setupEmptyView(View view) {
        contentEmpty = (LinearLayout) view.findViewById(R.id.content_empty);
    }

    private void setupRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        adapter = new WeekAdapter(getContext(), dataSet);
        recyclerView.setAdapter(adapter);
    }

    private void updateContentEmpty() {
        if (dataSet.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            contentEmpty.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            contentEmpty.setVisibility(View.VISIBLE);
        }
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
        fab.setImageResource(R.drawable.ic_email_white_24dp);
        if (!fab.isShown()) {
            fab.show();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail();
            }
        });
    }

    private void mail() {
        StringBuilder builder = new StringBuilder();

        Profile profile = Profile.getProfile();
        if (profile != null) {
            builder.append(Profile.profileInformation(getContext(), profile));
        }

        DateIntervalPickerFragment fragment = (DateIntervalPickerFragment) getChildFragmentManager().findFragmentByTag(DateIntervalPickerFragment.class.toString());
        builder.append(String.format(getString(R.string.diary_email_week_format), fragment.getTitle()));

        builder.append(getString(R.string.week_email_report_header));

        for (Object object : dataSet) {
            if (object instanceof WeekViewModel) {
                WeekViewModel viewModel = (WeekViewModel) object;
                builder.append(String.format("%s: %d/%d\n", viewModel.getBasicPoint().getName(), viewModel.getWeekCount(), viewModel.getBasicPoint().getWeekDays().size()));
            }
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        intent.setType("text/plain");
        startActivity(intent);
    }

    @Override
    public void onPickDateInterval(DateIntervalPickerFragment fragment) {
        setupDataSet();
        adapter.setDataSet(dataSet);
        adapter.notifyDataSetChanged();
    }
}
