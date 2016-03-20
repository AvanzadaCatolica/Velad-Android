package com.mac.velad.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mac.velad.R;
import com.mac.velad.general.DateIntervalPickerFragment;
import com.mac.velad.general.RecyclerItemClickListener;

import io.realm.Realm;
import io.realm.RealmResults;

public class DiaryFragment extends Fragment implements DateIntervalPickerFragment.DateIntervalPickerFragmentListener {

    private enum DiaryFilterType {
        DIARY_FILTER_TYPE_ALL, DIARY_FILTER_TYPE_REGULAR, DIARY_FILTER_TYPE_CONFESSABLE, DIARY_FILTER_TYPE_CONFESSED, DIARY_FILTER_TYPE_GUIDANCE, DIARY_FILTER_TYPE_WEEKLY
    }

    public final static int MODIFY_NOTE_REQUEST = 9999;
    public final static String NOTE_UUID_EXTRA = "NOTE_UUID";

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private RealmResults dataSet;
    private DiaryFilterType filterType = DiaryFilterType.DIARY_FILTER_TYPE_ALL;

    public DiaryFragment() {
        // Required empty public constructor
    }

    public static DiaryFragment newInstance() {
        DiaryFragment fragment = new DiaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setupFloatingButton();
        dataSet = Note.getAll(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        setupRecyclerView(view);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add:
                addNote();
                return true;
            case R.id.menu_item_filter_all:
                setupFilter(DiaryFilterType.DIARY_FILTER_TYPE_ALL);
                return true;
            case R.id.menu_item_filter_regular:
                setupFilter(DiaryFilterType.DIARY_FILTER_TYPE_REGULAR);
                return true;
            case R.id.menu_item_filter_confessable:
                setupFilter(DiaryFilterType.DIARY_FILTER_TYPE_CONFESSABLE);
                return true;
            case R.id.menu_item_filter_confessed:
                setupFilter(DiaryFilterType.DIARY_FILTER_TYPE_CONFESSED);
                return true;
            case R.id.menu_item_filter_guidance:
                setupFilter(DiaryFilterType.DIARY_FILTER_TYPE_GUIDANCE);
                return true;
            case R.id.menu_item_filter_weekly:
                addWeeklyFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_diary_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_email_white_24dp);
        if (!fab.isShown()) {
            fab.show();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void addNote() {
        Intent intent = new Intent(getContext(), NoteActivity.class);
        startActivityForResult(intent, MODIFY_NOTE_REQUEST);
    }

    private void removeWeeklyFilter() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(DateIntervalPickerFragment.class.toString());
        if (fragment != null) {
            getChildFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void addWeeklyFilter() {
        if (this.filterType == DiaryFilterType.DIARY_FILTER_TYPE_WEEKLY) {
            return;
        }
        getChildFragmentManager().beginTransaction()
                .add(R.id.frame_date_interval_picker, DateIntervalPickerFragment.newInstance(), DateIntervalPickerFragment.class.toString()).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MODIFY_NOTE_REQUEST:
                handleCreateNote(resultCode);
                return;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleCreateNote(int resultCode) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NoteAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openNote(position);
            }

            @Override
            public void onItemLongPress(View childView, int position) {
                showDeleteConfirmation(position);
            }
        }));
    }

    private void showDeleteConfirmation(final int position) {
        Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), "Borrar nota?", Snackbar.LENGTH_LONG)
                .setAction("Borrar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteNote(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    private void openNote(int position) {
        Note note = (Note) dataSet.get(position);

        Intent intent = new Intent(getContext(), NoteActivity.class);
        intent.putExtra(NOTE_UUID_EXTRA, note.getUUID());
        startActivityForResult(intent, MODIFY_NOTE_REQUEST);
    }

    private void deleteNote(int position) {
        Realm realm = Realm.getInstance(getContext());
        realm.beginTransaction();

        Note note = (Note) dataSet.get(position);
        note.removeFromRealm();

        realm.commitTransaction();
    }

    @Override
    public void onPickDateInterval(DateIntervalPickerFragment fragment) {
        setupFilter(DiaryFilterType.DIARY_FILTER_TYPE_WEEKLY);
    }

    private void setupFilter(DiaryFilterType filterType) {
        if (filterType != DiaryFilterType.DIARY_FILTER_TYPE_WEEKLY) {
            removeWeeklyFilter();
            if (this.filterType == filterType) {
                // No need to setup again this filter, unless it's weekly
                return;
            }
        }

        this.filterType = filterType;

        switch (filterType) {
            case DIARY_FILTER_TYPE_ALL:
                dataSet = Note.getAll(getContext());
                break;
            case DIARY_FILTER_TYPE_REGULAR:
                dataSet = Note.getNotes(getContext(), NoteState.REGULAR);
                break;
            case DIARY_FILTER_TYPE_CONFESSABLE:
                dataSet = Note.getNotes(getContext(), NoteState.CONFESSABLE);
                break;
            case DIARY_FILTER_TYPE_CONFESSED:
                dataSet = Note.getNotes(getContext(), NoteState.CONFESSED);
                break;
            case DIARY_FILTER_TYPE_GUIDANCE:
                dataSet = Note.getNotes(getContext(), NoteState.GUIDANCE);
                break;
            case DIARY_FILTER_TYPE_WEEKLY:
                DateIntervalPickerFragment fragment = (DateIntervalPickerFragment) getChildFragmentManager().findFragmentByTag(DateIntervalPickerFragment.class.toString());
                dataSet = Note.getNotesBetween(getContext(), fragment.getSelectedStartDate(), fragment.getSelectedEndDate());
                break;
        }

        adapter.setDataSet(dataSet);
        adapter.notifyDataSetChanged();
    }

}
