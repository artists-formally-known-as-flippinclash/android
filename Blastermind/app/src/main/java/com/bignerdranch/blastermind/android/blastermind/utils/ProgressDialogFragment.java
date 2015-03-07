package com.bignerdranch.blastermind.android.blastermind.utils;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;

public class ProgressDialogFragment extends DialogFragment {

    private static final String ARG_MESSAGE_ID = "ProgressDialogFragment.MessageResId";

    private String mMessage;

    public static ProgressDialogFragment newInstance() {
        return newInstance("");
    }

    public static ProgressDialogFragment newInstance(String message) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();

        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE_ID, message);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMessage = getArguments().getString(ARG_MESSAGE_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        if (!TextUtils.isEmpty(mMessage)) {
            dialog.setMessage(mMessage);
        }
        return dialog;
    }
}