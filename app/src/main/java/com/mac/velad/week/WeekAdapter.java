package com.mac.velad.week;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mac.velad.R;
import com.mac.velad.today.Group;
import com.mac.velad.today.GroupHeaderViewHolder;

import java.util.List;

/**
 * Created by ruenzuo on 27/03/16.
 */
public class WeekAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private enum WeekViewType {
        WEEK_VIEW_TYPE_HEADER, WEEK_VIEW_TYPE_ROW;
    }

    private List dataSet;
    private Context context;

    public WeekAdapter(Context context, List dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public void setDataSet(List dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataSet.get(position) instanceof Group) {
            return WeekViewType.WEEK_VIEW_TYPE_HEADER.ordinal();
        } else {
            return WeekViewType.WEEK_VIEW_TYPE_ROW.ordinal();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (WeekViewType.values()[viewType]) {
            case WEEK_VIEW_TYPE_ROW: {
                View view = LayoutInflater.from(context).inflate(R.layout.view_week_row, parent, false);
                RowViewHolder viewHolder = new RowViewHolder(view);
                return viewHolder;
            }
            case WEEK_VIEW_TYPE_HEADER: {
                View view = LayoutInflater.from(context).inflate(R.layout.view_group_header, parent, false);
                GroupHeaderViewHolder viewHolder = new GroupHeaderViewHolder(view);
                return viewHolder;
            }
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object objectAtPosition = dataSet.get(position);
        if (objectAtPosition instanceof Group) {
            Group group = (Group) objectAtPosition;
            GroupHeaderViewHolder headerViewHolder = (GroupHeaderViewHolder) holder;
            headerViewHolder.textViewTitle.setText(group.getName());
        } else {
            WeekViewModel viewModel = (WeekViewModel) objectAtPosition;
            RowViewHolder rowViewHolder = (RowViewHolder) holder;
            rowViewHolder.textViewTitle.setText(viewModel.getBasicPoint().getName());
            rowViewHolder.textViewValue.setText(viewModel.getWeekCount() + "/" + viewModel.getBasicPoint().getWeekDays().size());
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewValue;

        public RowViewHolder(View root) {
            super(root);
            this.textViewTitle = (TextView) root.findViewById(R.id.text_view_title);
            this.textViewValue = (TextView) root.findViewById(R.id.text_view_value);

        }
    }

}
