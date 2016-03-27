package com.mac.velad.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.mac.velad.R;

/**
 * Created by ruenzuo on 27/03/16.
 */
public class EncouragementDialogFragment extends DialogFragment {

    public interface EncouragementDialogFragmentListener {
        void onSave(int value);
        void onDeactivate();
    }

    private static final String SELECTED_INDEX_KEY = "SELECTED_INDEX_KEY";

    private EncouragementDialogFragmentListener listener;
    private Integer selectedIndex = -1;

    public static EncouragementDialogFragment newInstance(Integer value) {
        EncouragementDialogFragment fragment = new EncouragementDialogFragment();
        if (value != null) {
            int selectedIndex = (value / 10) - 1;
            Bundle bundle = new Bundle();
            bundle.putInt(SELECTED_INDEX_KEY, selectedIndex);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            selectedIndex = getArguments().getInt(SELECTED_INDEX_KEY);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MaterialThemeDialog);
        builder.setSingleChoiceItems(R.array.encouragement_values, selectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex = which;
                    }
                })
                .setPositiveButton(R.string.menu_item_title_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (selectedIndex != -1) {
                            listener.onSave((selectedIndex + 1) * 10);
                        }
                    }
                })
                .setNegativeButton(R.string.menu_item_title_deactivate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDeactivate();
                    }
                });
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
        if (fragment instanceof EncouragementDialogFragmentListener) {
            listener = (EncouragementDialogFragmentListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement EncouragementDialogFragmentListener");
        }
    }

}
