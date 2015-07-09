package com.tir38.android.blastermind.controller;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tir38.android.blastermind.R;

import java.io.Serializable;

public class MaterialDialogFragment extends DialogFragment {

    public static final String EXTRA_WHICH = "MaterialDialogFragment.Which";

    private static final String ARG_BUILDER = "MaterialDialogFragment.ARG_BUILDER";

    public static DialogFragment newInstance(FragmentBuilder fragmentBuilder) {
        DialogFragment fragment = new MaterialDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BUILDER, fragmentBuilder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentBuilder fragmentBuilder = (FragmentBuilder) getArguments().getSerializable(ARG_BUILDER);

        if (fragmentBuilder == null) {
            return null;
        }

        MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(getActivity());

        if (fragmentBuilder.mTitleResId > 0) {
            materialDialogBuilder.title(fragmentBuilder.mTitleResId);
        }

        if (fragmentBuilder.mTitle != null) {
            materialDialogBuilder.title(fragmentBuilder.mTitle);
        }

        if (fragmentBuilder.mContentResId > 0) {
            materialDialogBuilder.content(fragmentBuilder.mContentResId);
        }

        if (fragmentBuilder.mPositiveButtonResId > 0) {
            materialDialogBuilder.positiveText(fragmentBuilder.mPositiveButtonResId);
        }

        if (fragmentBuilder.mNeutralButtonResId > 0) {
            materialDialogBuilder.neutralText(fragmentBuilder.mNeutralButtonResId);
        }

        if (fragmentBuilder.mNegativeButtonResId > 0) {
            materialDialogBuilder.negativeText(fragmentBuilder.mNegativeButtonResId);
        }

        int color = getResources().getColor(R.color.dark_text);
        materialDialogBuilder.negativeColor(color);
        materialDialogBuilder.contentColor(color);
        materialDialogBuilder.positiveColor(color);
        materialDialogBuilder.neutralColor(color);
        materialDialogBuilder.titleColor(color);

        materialDialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                sendResult(DialogInterface.BUTTON_POSITIVE);
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                sendResult(DialogInterface.BUTTON_NEGATIVE);
            }

            @Override
            public void onNeutral(MaterialDialog dialog) {
                sendResult(DialogInterface.BUTTON_NEUTRAL);
            }
        });

        return materialDialogBuilder.build();
    }

    /**
     * responds via onActivityResult *
     */
    protected void sendResult(int which) {
        Fragment fragment = getTargetFragment();

        if (fragment == null) {
            // lets start by assuming target fragment is only way we got here
            return;
        }

        Intent data = createResultData(which);
        int resultCode = which;
        fragment.onActivityResult(getTargetRequestCode(), resultCode, data);
    }

    protected Intent createResultData(int which) {
        Intent data = new Intent();
        data.putExtra(EXTRA_WHICH, which);
        return data;
    }

    public static class FragmentBuilder implements Serializable {

        protected int mTitleResId;
        protected int mContentResId;
        protected int mPositiveButtonResId;
        protected int mNeutralButtonResId;
        protected int mNegativeButtonResId;
        protected String mTitle;

        public FragmentBuilder titleResId(int titleResId) {
            mTitleResId = titleResId;
            return this;
        }

        public FragmentBuilder title(@NonNull String title) {
            mTitle = title;
            return this;
        }

        public FragmentBuilder contentResId(int contentResId) {
            mContentResId = contentResId;
            return this;
        }

        public FragmentBuilder positiveButtonResId(int positiveButtonResId) {
            mPositiveButtonResId = positiveButtonResId;
            return this;
        }

        public FragmentBuilder neutralButtonResId(int neutralButtonResId) {
            mNeutralButtonResId = neutralButtonResId;
            return this;
        }

        public FragmentBuilder negativeButtonResId(int negativeButtonResId) {
            mNegativeButtonResId = negativeButtonResId;
            return this;
        }

        public DialogFragment build() {
            return MaterialDialogFragment.newInstance(this);
        }
    }
}

