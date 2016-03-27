package com.mac.velad.today;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.velad.R;

import java.util.List;

/**
 * Created by ruenzuo on 27/03/16.
 */
public class TodayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private enum TodayViewType {
        TODAY_VIEW_TYPE_HEADER, TODAY_VIEW_TYPE_ROW;
    }

    private List dataSet;
    private Context context;

    public TodayAdapter(Context context, List dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public void setDataSet(List dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataSet.get(position) instanceof Group) {
            return TodayViewType.TODAY_VIEW_TYPE_HEADER.ordinal();
        } else {
            return TodayViewType.TODAY_VIEW_TYPE_ROW.ordinal();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (TodayViewType.values()[viewType]) {
            case TODAY_VIEW_TYPE_ROW: {
                View view = LayoutInflater.from(context).inflate(R.layout.view_today_row, parent, false);
                RowViewHolder viewHolder = new RowViewHolder(view);
                return viewHolder;
            }
            case TODAY_VIEW_TYPE_HEADER: {
                View view = LayoutInflater.from(context).inflate(R.layout.view_today_header, parent, false);
                HeaderViewHolder viewHolder = new HeaderViewHolder(view);
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
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.textViewTitle.setText(group.getName());
        } else {
            TodayViewModel viewModel = (TodayViewModel) objectAtPosition;
            RowViewHolder rowViewHolder = (RowViewHolder) holder;
            rowViewHolder.textViewTitle.setText(viewModel.getBasicPoint().getName());
            if (viewModel.getRecord() != null) {
                if (viewModel.getRecord().getNotes() != null) {
                    rowViewHolder.imageViewCheckmark.setVisibility(View.GONE);
                    rowViewHolder.imageButtonInfo.setVisibility(View.VISIBLE);
                } else {
                    rowViewHolder.imageViewCheckmark.setVisibility(View.VISIBLE);
                    rowViewHolder.imageButtonInfo.setVisibility(View.GONE);
                }
            } else {
                rowViewHolder.imageViewCheckmark.setVisibility(View.GONE);
                rowViewHolder.imageButtonInfo.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;

        public HeaderViewHolder(View root) {
            super(root);
            this.textViewTitle = (TextView) root.findViewById(R.id.text_view_title);
        }
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public ImageView imageViewCheckmark;
        public ImageButton imageButtonInfo;

        public RowViewHolder(View root) {
            super(root);
            this.textViewTitle = (TextView) root.findViewById(R.id.text_view_title);
            this.imageViewCheckmark = (ImageView) root.findViewById(R.id.image_view_checkmark);
            this.imageButtonInfo = (ImageButton) root.findViewById(R.id.image_button_info);
        }
    }

}
