package com.mac.velad.today;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.mac.velad.R;

/**
 * Created by ruenzuo on 27/03/16.
 */
public class NoteDialogFragment extends DialogFragment {

    public interface NoteDialogFragmentListener {
        void onInputNote(String note, int position);
    }

    private static final String POSITION_KEY = "POSITION_KEY";
    private static final String NOTES_KEY = "NOTES_KEY";
    private NoteDialogFragmentListener listener;
    private EditText editTextNote;

    public static NoteDialogFragment newInstance(String notes, int position) {
        NoteDialogFragment fragment = new NoteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_KEY, position);
        bundle.putString(NOTES_KEY, notes);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_note, null);
        editTextNote = (EditText) view.findViewById(R.id.edit_text_note);
        editTextNote.setText(getArguments().getString(NOTES_KEY));
        builder.setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onInputNote(editTextNote.getText().toString(), getArguments().getInt(POSITION_KEY));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment fragment = getParentFragment();
        if (fragment instanceof NoteDialogFragmentListener) {
            listener = (NoteDialogFragmentListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement NoteDialogFragmentListener");
        }
    }

}
