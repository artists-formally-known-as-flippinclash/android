package com.tir38.android.blastermind.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tir38.android.blastermind.R;

public class ProgressDialogFragment extends DialogFragment {

    private static final String ARG_MESSAGE_ID = "ProgressDialogFragment.MessageResId";

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


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString(ARG_MESSAGE_ID);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_progress_dialog, null);

        if (!TextUtils.isEmpty(message)) {
            TextView messageTextView = (TextView) view.findViewById(R.id.view_progress_dialog_message);
            messageTextView.setText(message);
        }

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .create();
    }
}