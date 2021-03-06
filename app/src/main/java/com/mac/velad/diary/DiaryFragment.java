package com.mac.velad.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.mac.velad.R;
import com.mac.velad.general.DateFormat;
import com.mac.velad.general.DateIntervalPickerFragment;
import com.mac.velad.general.DividerItemDecoration;
import com.mac.velad.general.ItemClickSupport;
import com.mac.velad.settings.Profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class DiaryFragment extends Fragment implements DateIntervalPickerFragment.DateIntervalPickerFragmentListener {

    private enum DiaryFilterType {
        DIARY_FILTER_TYPE_ALL, DIARY_FILTER_TYPE_REGULAR, DIARY_FILTER_TYPE_CONFESSABLE, DIARY_FILTER_TYPE_CONFESSED, DIARY_FILTER_TYPE_GUIDANCE, DIARY_FILTER_TYPE_WEEKLY
    }

    public final static int MODIFY_NOTE_REQUEST = 9999;
    public final static String NOTE_UUID_EXTRA = "NOTE_UUID";

    private RecyclerView recyclerView;
    private LinearLayout contentEmpty;
    private NoteAdapter adapter;
    private RealmResults<Note> dataSet;
    private DiaryFilterType filterType = DiaryFilterType.DIARY_FILTER_TYPE_ALL;
    private MultiSelector multiSelector = new MultiSelector();
    private ActionMode currentActionMode;

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
        setupEmptyView(view);
        setupRecyclerView(view);
        updateContentEmpty();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add:
                addNote();
                return true;
            case R.id.menu_item_register:
                confess();
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
        MenuItem item = menu.findItem(R.id.menu_item_register);
        if (filterType == DiaryFilterType.DIARY_FILTER_TYPE_CONFESSABLE && dataSet.size() > 0) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
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

    private void updateContentEmpty() {
        if (dataSet.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            contentEmpty.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            contentEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void mail() {
        StringBuilder builder = new StringBuilder();
        DateFormat dateFormat = new DateFormat("dd/MM/yyyy");

        Profile profile = Profile.getProfile(getContext());
        if (profile != null) {
            builder.append(Profile.profileInformation(getContext(), profile));
        }

        if (filterType == DiaryFilterType.DIARY_FILTER_TYPE_WEEKLY) {
            DateIntervalPickerFragment fragment = (DateIntervalPickerFragment) getChildFragmentManager().findFragmentByTag(DateIntervalPickerFragment.class.toString());
            builder.append(String.format(getString(R.string.diary_email_week_format), fragment.getTitle()));
        }

        Confession confession = Confession.getLastConfession(getContext());
        if (confession != null) {
            builder.append(String.format(getString(R.string.diary_email_confession_format), dateFormat.format(confession.getDate())));
        }

        builder.append(getString(R.string.diary_email_notes_header));

        for (Note note : dataSet) {
            builder.append(String.format("%s (%s - %s)\n", note.getText(), note.getState(), dateFormat.format(note.getDate())));
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        intent.setType("text/plain");
        startActivity(intent);
    }

    private void confess() {
        Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), R.string.diary_confess_confirmation_text, Snackbar.LENGTH_LONG)
                .setAction(R.string.diary_confess_confirmation_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registerConfession();
                    }
                })
                .show();
    }

    private void registerConfession() {
        Realm realm = Realm.getInstance(getContext());
        realm.beginTransaction();

        Date now = new Date();
        while (dataSet.size() > 0) {
            Note note = (Note) dataSet.first();
            note.setState(NoteState.CONFESSED.toString());
            note.setDate(now);
        }

        Confession confession = new Confession();
        confession.setUUID(UUID.randomUUID().toString());
        confession.setDate(now);
        realm.copyToRealm(confession);

        realm.commitTransaction();

        updateFragment();
    }

    private void updateFragment() {
        adapter.notifyDataSetChanged();
        adapter.setConfession(Confession.getLastConfession(getContext()));
        updateContentEmpty();
        getActivity().invalidateOptionsMenu();
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

        updateFragment();
    }

    private void setupRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        adapter = new NoteAdapter(getContext(), dataSet, multiSelector);
        adapter.setConfession(Confession.getLastConfession(getContext()));
        recyclerView.setAdapter(adapter);

        ItemClickSupport support = ItemClickSupport.addTo(recyclerView);
        support.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View view) {
                NoteAdapter.ViewHolder holder = (NoteAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                if (multiSelector.tapSelection(holder)) {
                    updateActionMode();
                } else {
                    openNote(position);
                }
            }
        });

        support.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View view) {
                if (currentActionMode != null) {
                    return true;
                }
                Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                currentActionMode = toolbar.startActionMode(new android.view.ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                        multiSelector.setSelectable(true);
                        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
                        fab.hide();
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                        getActivity().getMenuInflater().inflate(R.menu.action_delete_menu, menu);
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_item_delete:
                                deleteNotes(multiSelector.getSelectedPositions());
                                currentActionMode.finish();
                                updateFragment();
                                return true;
                            default:
                                return true;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(android.view.ActionMode mode) {
                        multiSelector.clearSelections();
                        multiSelector.setSelectable(false);
                        currentActionMode = null;
                        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
                        fab.show();
                    }
                });
                NoteAdapter.ViewHolder holder = (NoteAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                multiSelector.setSelected(holder, true);
                updateActionMode();
                return true;
            }
        });
    }

    private void updateActionMode() {
        if (currentActionMode == null) {
            return;
        }
        int selected = multiSelector.getSelectedPositions().size();
        if (selected == 0) {
            currentActionMode.finish();
        } else {
            currentActionMode.setTitle(selected + " " + getString(R.string.action_mode_delete_title));
        }

    }

    private void setupEmptyView(View view) {
        contentEmpty = (LinearLayout) view.findViewById(R.id.content_empty);
    }

    private void openNote(int position) {
        Note note = dataSet.get(position);

        Intent intent = new Intent(getContext(), NoteActivity.class);
        intent.putExtra(NOTE_UUID_EXTRA, note.getUUID());
        startActivityForResult(intent, MODIFY_NOTE_REQUEST);
    }

    private void deleteNotes(List<Integer> positions) {
        Realm realm = Realm.getInstance(getContext());
        realm.beginTransaction();

        List<Note> notesToDelete = new ArrayList<>();
        for (Integer position : positions) {
            Note note = dataSet.get(position);
            notesToDelete.add(note);
        }
        for (Note note : notesToDelete) {
            note.removeFromRealm();
        }

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
        updateFragment();
    }

}
