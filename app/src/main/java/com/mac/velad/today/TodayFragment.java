package com.mac.velad.today;

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
import com.mac.velad.general.DateFormat;
import com.mac.velad.general.DatePickerFragment;
import com.mac.velad.general.DividerItemDecoration;
import com.mac.velad.general.ItemClickSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;


public class TodayFragment extends Fragment implements DatePickerFragment.DatePickerFragmentListener, NoteDialogFragment.NoteDialogFragmentListener, OnNoteClickListener {

    public TodayFragment() {
        // Required empty public constructor
    }

    private List dataSet = new ArrayList();
    private RecyclerView recyclerView;
    private TodayAdapter adapter;
    private LinearLayout contentEmpty;

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setupDatePicker();
        setupDataSet();
        setupFloatingButton();
    }

    private void setupDatePicker() {
        getChildFragmentManager().beginTransaction()
                .add(R.id.frame_date_picker, DatePickerFragment.newInstance(), DatePickerFragment.class.toString()).commit();
    }

    private void setupEmptyView(View view) {
        contentEmpty = (LinearLayout) view.findViewById(R.id.content_empty);
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

    private void setupDataSet() {
        dataSet.clear();
        DatePickerFragment fragment = (DatePickerFragment) getChildFragmentManager().findFragmentByTag(DatePickerFragment.class.toString());
        Date selectedDate;
        if (fragment == null) {
            selectedDate = CalendarHelper.coldCurrentDate(getContext());
        } else {
            selectedDate = fragment.getSelectedDate();
        }
        DateFormat dateFormat = new DateFormat("EEEE");

        RealmResults<Group> groups = Group.getAll(getContext());
        for (Group group :groups) {
            for (BasicPoint basicPoint : group.getBasicPoints()) {
                if (basicPoint.isEnabled() &&
                        BasicPoint.weekDaySymbols(basicPoint).contains(dateFormat.format(selectedDate))) {
                    if (!dataSet.contains(group)) {
                        dataSet.add(group);
                    }
                    TodayViewModel viewModel = new TodayViewModel(Record.getRecord(getContext(), basicPoint, selectedDate), basicPoint);
                    dataSet.add(viewModel);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        setupEmptyView(view);
        setupRecyclerView(view);
        updateContentEmpty();
        return view;
    }

    private void setupRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        adapter = new TodayAdapter(getContext(), dataSet, this);
        recyclerView.setAdapter(adapter);

        ItemClickSupport support = ItemClickSupport.addTo(recyclerView);
        support.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View view) {
                toogleRecord(position, null);
                adapter.notifyItemChanged(position);
            }
        });
        support.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View view) {
                showNoteDialog(position);
                return true;
            }
        });
    }

    private void showNoteDialog(int position) {
        TodayViewModel viewModel = (TodayViewModel) dataSet.get(position);
        String notes = "";
        if (viewModel.getRecord() != null && viewModel.getRecord().getNotes() != null) {
            notes = viewModel.getRecord().getNotes();
        }
        NoteDialogFragment dialogFragment = NoteDialogFragment.newInstance(notes, position);
        dialogFragment.show(getChildFragmentManager(), NoteDialogFragment.class.toString());
    }

    private void toogleRecord(int position, String notes) {
        Object objectAtIndex = dataSet.get(position);
        if (objectAtIndex instanceof Group) {
            return;
        }
        TodayViewModel viewModel = (TodayViewModel) objectAtIndex;
        DatePickerFragment fragment = (DatePickerFragment) getChildFragmentManager().findFragmentByTag(DatePickerFragment.class.toString());

        Realm realm = Realm.getInstance(getContext());
        realm.beginTransaction();

        if (viewModel.getRecord() != null) {
            if (notes != null) {
                Record record = viewModel.getRecord();
                record.setNotes(notes);
                realm.copyToRealm(record);
            } else {
                viewModel.getRecord().removeFromRealm();
                viewModel.setRecord(null);
            }
        } else {
            Record record = realm.createObject(Record.class);
            record.setUUID(UUID.randomUUID().toString());
            record.setDate(fragment.getSelectedDate());
            record.setBasicPoint(viewModel.getBasicPoint());
            record.setNotes(notes);
            realm.copyToRealm(record);
            viewModel.setRecord(record);
        }

        realm.commitTransaction();
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
        fab.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);
        if (!fab.isShown()) {
            fab.show();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onPickDate(DatePickerFragment fragment) {
        setupDataSet();
        adapter.setDataSet(dataSet);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onInputNote(String note, int position) {
        toogleRecord(position, note);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onNoteClick(TodayViewModel viewModel) {
        int position = dataSet.indexOf(viewModel);
        showNoteDialog(position);
    }
}
