package com.mac.velad.today.groups;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.mac.velad.R;

/**
 * Created by ruenzuo on 11/02/2017.
 */

public class GroupDialogFragment extends DialogFragment {

    public interface GroupDialogFragmentListener {
        void onInputGroup(String name);
    }

    public static GroupDialogFragment newInstance() {
        GroupDialogFragment fragment = new GroupDialogFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    private EditText editTextNote;
    private GroupDialogFragmentListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_group, null);
        editTextNote = (EditText) view.findViewById(R.id.edit_text_group);
        builder.setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onInputGroup(editTextNote.getText().toString());
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
        Activity activity = getActivity();
        if (activity instanceof GroupDialogFragmentListener) {
            listener = (GroupDialogFragmentListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement GroupDialogFragmentListener");
        }
    }
}
