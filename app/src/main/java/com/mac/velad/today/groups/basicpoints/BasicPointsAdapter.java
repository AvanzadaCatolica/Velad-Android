package com.mac.velad.today.groups.basicpoints;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.mac.velad.R;
import com.mac.velad.today.BasicPoint;

import io.realm.RealmList;

/**
 * Created by ruenzuo on 11/02/2017.
 */

public class BasicPointsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private RealmList<BasicPoint> dataSet;
    private MultiSelector multiSelector;

    public BasicPointsAdapter(Context context, RealmList<BasicPoint> dataSet, MultiSelector multiSelector) {
        this.context = context;
        this.dataSet = dataSet;
        this.multiSelector = multiSelector;
    }

    private Drawable getStateDrawable() {
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.colorSelected));
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16843518}, colorDrawable);
        stateListDrawable.addState(StateSet.WILD_CARD, null);
        return stateListDrawable;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_basic_point_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, multiSelector);
        viewHolder.setSelectionModeBackgroundDrawable(getStateDrawable());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BasicPoint basicPoint = dataSet.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.textViewTitle.setText(basicPoint.getName());
        if (basicPoint.isEnabled()) {
            viewHolder.imageViewCheckmark.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imageViewCheckmark.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends SwappingHolder {
        public TextView textViewTitle;
        public ImageView imageViewCheckmark;

        public ViewHolder(View root, MultiSelector multiSelector) {
            super(root, multiSelector);
            this.textViewTitle = (TextView) root.findViewById(R.id.text_view_title);
            this.imageViewCheckmark = (ImageView) root.findViewById(R.id.image_view_checkmark);
        }
    }

}
