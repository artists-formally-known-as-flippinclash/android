package com.bignerdranch.blastermind.android.blastermind.utils;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class DialogUtils {

    private static final String TAG_PROGRESS_DIALOG = "DialogUtils.progressDialog";

    public static void showLoadingDialog(FragmentManager fragmentManager, int messageResId) {
        if (fragmentManager == null) {
            return;
        }

        Fragment existingDialogFragment = fragmentManager.findFragmentByTag(TAG_PROGRESS_DIALOG);
        boolean dialogExists = existingDialogFragment != null;

        DialogFragment updatedDialogFragment = null;
        if (messageResId > 0) {
            updatedDialogFragment = ProgressDialogFragment.newInstance(messageResId);
        } else {
            updatedDialogFragment = ProgressDialogFragment.newInstance();
        }
        updatedDialogFragment.setCancelable(false);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(updatedDialogFragment, TAG_PROGRESS_DIALOG);
        if (dialogExists) {
            transaction.remove(existingDialogFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    public static void hideLoadingDialog(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }

        Fragment fragment = fragmentManager.findFragmentByTag(TAG_PROGRESS_DIALOG);
        if (fragment instanceof DialogFragment) {
            ((DialogFragment) fragment).dismiss();
        }
    }

    private DialogUtils() {
        // can't instantiate helper class
    }

}