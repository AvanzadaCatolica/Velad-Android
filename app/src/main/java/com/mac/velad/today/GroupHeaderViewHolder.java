package com.mac.velad.today;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mac.velad.R;

/**
 * Created by ruenzuo on 27/03/16.
 */
public class GroupHeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewTitle;

    public GroupHeaderViewHolder(View root) {
        super(root);
        this.textViewTitle = (TextView) root.findViewById(R.id.text_view_title);
    }
}
