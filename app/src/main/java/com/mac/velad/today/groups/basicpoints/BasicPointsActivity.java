package com.mac.velad.today.groups.basicpoints;

import android.content.Intent;
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
import com.mac.velad.today.BasicPoint;
import com.mac.velad.today.groups.Group;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class BasicPointsActivity extends AppCompatActivity {

    public final static String GROUP_UUID_EXTRA = "GROUP_UUID";

    private Group group;
    private RecyclerView recyclerView;
    private MultiSelector multiSelector = new MultiSelector();
    private BasicPointsAdapter adapter;
    private ActionMode currentActionMode;
    private LinearLayout contentEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_points);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            group = Group.getGroup(bundle.getString(GROUP_UUID_EXTRA));
        } else {
            throw new IllegalStateException("Group UUID not set");
        }
        setupRecyclerView();
        setupFloatingButton();
        setupEmptyView();
        updateContentEmpty();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra(GROUP_UUID_EXTRA, group.getUUID());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateContentEmpty() {
        if (group.getBasicPoints().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            contentEmpty.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            contentEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void setupEmptyView() {
        contentEmpty = (LinearLayout) findViewById(R.id.content_empty);
    }

    private void setupFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewBasicPoint();
            }
        });
    }

    private void showNewBasicPoint() {

    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        adapter = new BasicPointsAdapter(this, group.getBasicPoints(), multiSelector);
        recyclerView.setAdapter(adapter);

        ItemClickSupport support = ItemClickSupport.addTo(recyclerView);
        support.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View view) {
                BasicPointsAdapter.ViewHolder holder = (BasicPointsAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                if (multiSelector.tapSelection(holder)) {
                    updateActionMode();
                } else {
                    openBasicPoint(position);
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
                                deleteBasicPoints(multiSelector.getSelectedPositions());
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
                BasicPointsAdapter.ViewHolder holder = (BasicPointsAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                multiSelector.setSelected(holder, true);
                updateActionMode();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateActivity() {
        adapter.notifyDataSetChanged();
        updateContentEmpty();
    }

    private void deleteBasicPoints(List<Integer> positions) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        List<BasicPoint> basicPointsToDelete = new ArrayList<>();
        for (Integer position : positions) {
            BasicPoint basicPoint = group.getBasicPoints().get(position);
            basicPointsToDelete.add(basicPoint);
        }
        for (BasicPoint basicPoint: basicPointsToDelete) {
            group.getBasicPoints().remove(basicPoint);
            basicPoint.deleteFromRealm();
        }

        realm.commitTransaction();
    }

    private void openBasicPoint(int position) {

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
}
