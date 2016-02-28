package com.mac.velad.diary;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mac.velad.R;

import java.text.SimpleDateFormat;

import io.realm.RealmResults;

/**
 * Created by ruenzuo on 28/02/16.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private RealmResults<Note> dataSet;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_note, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = dataSet.get(position);
        holder.textViewContent.setText(note.getText());
        holder.textViewDate.setText(dateFormat.format(note.getDate()));
        holder.textViewState.setText(note.getState());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public NoteAdapter(RealmResults<Note> dataSet) {
        this.dataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewContent;
        public TextView textViewDate;
        public TextView textViewState;

        public ViewHolder(View root) {
            super(root);
            this.textViewContent = (TextView) root.findViewById(R.id.text_view_content);
            this.textViewDate = (TextView) root.findViewById(R.id.text_view_date);
            this.textViewState = (TextView) root.findViewById(R.id.text_view_state);
        }
    }

}
