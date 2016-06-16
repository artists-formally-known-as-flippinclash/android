package com.tir38.android.blastermind.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.tir38.android.blastermind.R;

public class EndOfGameDialogFragment extends DialogFragment {

    private static final String ARGS_MESSAGE = "ARGS_MESSAGE";

    public static EndOfGameDialogFragment newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(ARGS_MESSAGE, message);
        EndOfGameDialogFragment fragment = new EndOfGameDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString(ARGS_MESSAGE);
        return new AlertDialog.Builder(getContext())
                .setMessage(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                    }
                })
                .create();
    }
}
