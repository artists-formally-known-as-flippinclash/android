package com.tir38.android.blastermind.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.tir38.android.blastermind.R;

public class LeaveMatchDialogFragment extends DialogFragment {

    public static LeaveMatchDialogFragment newInstance() {
        return new LeaveMatchDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.exit_match)
                .setMessage(R.string.leave_match_msg)
                .setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                    }
                })
                .setNegativeButton(R.string.stay, null)
                .create();
    }
}
