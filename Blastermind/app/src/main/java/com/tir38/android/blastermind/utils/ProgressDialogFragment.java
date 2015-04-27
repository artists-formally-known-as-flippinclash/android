package com.tir38.android.blastermind.utils;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;

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
        if (!TextUtils.isEmpty(mMessage)) {
            return new MaterialDialog.Builder(getActivity())
                    .content(mMessage)
                    .progress(true, 0)
                    .build();

        } else {
            return new MaterialDialog.Builder(getActivity())
                    .progress(true, 0)
                    .build();

        }
    }
}