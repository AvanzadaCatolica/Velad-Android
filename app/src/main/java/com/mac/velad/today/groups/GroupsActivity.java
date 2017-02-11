package com.mac.velad.today.groups;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.mac.velad.R;
import com.mac.velad.general.DividerItemDecoration;
import com.mac.velad.general.ItemClickSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class GroupsActivity extends AppCompatActivity implements OnGroupClickListener, GroupDialogFragment.GroupDialogFragmentListener {

    private RealmResults<Group> dataSet;
    private RecyclerView recyclerView;
    private GroupsAdapter adapter;
    private ActionMode currentActionMode;
    private MultiSelector multiSelector = new MultiSelector();
    private LinearLayout contentEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dataSet = Group.getAll(this);
        setupRecyclerView();
        setupFloatingButton();
        setupEmptyView();
        updateContentEmpty();
    }

    private void setupEmptyView() {
        contentEmpty = (LinearLayout) findViewById(R.id.content_empty);
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

    private void setupFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewGroupDialog();
            }
        });
    }

    private void showNewGroupDialog() {
        GroupDialogFragment dialogFragment = GroupDialogFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), GroupDialogFragment.class.toString());
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        adapter = new GroupsAdapter(this, dataSet, multiSelector);
        recyclerView.setAdapter(adapter);

        ItemClickSupport support = ItemClickSupport.addTo(recyclerView);
        support.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View view) {
                GroupsAdapter.ViewHolder holder = (GroupsAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                if (multiSelector.tapSelection(holder)) {
                    updateActionMode();
                } else {
                    openGroup(position);
                }
            }
        });

        support.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View view) {
                if (currentActionMode != null) {
                    return true;
                }
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                currentActionMode = startActionMode(new android.view.ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        multiSelector.setSelectable(true);
                        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                        fab.hide();
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        getMenuInflater().inflate(R.menu.action_delete_menu, menu);
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_item_delete:
                                deleteGroups(multiSelector.getSelectedPositions());
                                currentActionMode.finish();
                                updateActivity();
                                return true;
                            default:
                                return true;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        multiSelector.clearSelections();
                        multiSelector.setSelectable(false);
                        currentActionMode = null;
                        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                        fab.show();
                    }
                });
                GroupsAdapter.ViewHolder holder = (GroupsAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                multiSelector.setSelected(holder, true);
                updateActionMode();
                return true;
            }
        });
    }

    private void openGroup(int position) {

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

    private void deleteGroups(List<Integer> positions) {
        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();

        List<Group> groupsToDelete = new ArrayList<>();
        for (Integer position : positions) {
            Group group = dataSet.get(position);
            groupsToDelete.add(group);
        }
        for (Group group: groupsToDelete) {
            group.removeFromRealm();
        }

        realm.commitTransaction();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onGroupClick(Group group) {

    }

    @Override
    public void onInputGroup(String name) {
        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();

        Group group = realm.createObject(Group.class);
        group.setUUID(UUID.randomUUID().toString());
        group.setName(name);
        group.setCreatedAt(new Date());
        realm.copyToRealm(group);

        realm.commitTransaction();

        updateActivity();
    }

    private void updateActivity() {
        adapter.notifyDataSetChanged();
        updateContentEmpty();
    }
}
