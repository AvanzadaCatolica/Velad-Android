package com.mac.velad.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.mac.velad.R;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class InfoDialogFragment extends DialogFragment {

    private static final String INFO_KEY = "INFO_KEY";

    public InfoDialogFragment() {
        // Required empty public constructor
    }

    public static InfoDialogFragment newInstance(String info) {
        InfoDialogFragment fragment = new InfoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INFO_KEY, info);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getArguments().getString(INFO_KEY))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

}
