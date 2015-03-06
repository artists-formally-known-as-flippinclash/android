package com.bignerdranch.blastermind.android.blastermind.utils;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.bignerdranch.blastermind.android.blastermind.R;

public class ProgressDialogFragment extends DialogFragment {

    private static final String ARG_MESSAGE_ID = "ProgressDialogFragment.MessageResId";

    private int mMessageId;

    public static ProgressDialogFragment newInstance() {
        return newInstance(R.string.loading);
    }

    public static ProgressDialogFragment newInstance(int messsageId) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_MESSAGE_ID, messsageId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMessageId = getArguments().getInt(ARG_MESSAGE_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        if (mMessageId > 0) {
            dialog.setMessage(getString(mMessageId));
        }
        return dialog;
    }
}