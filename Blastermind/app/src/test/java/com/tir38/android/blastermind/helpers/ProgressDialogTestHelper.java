package com.tir38.android.blastermind.helpers;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Field;

import static org.assertj.android.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ProgressDialogTestHelper {

    private static final String TAG = ProgressDialogTestHelper.class.getSimpleName();

    /**
     * @param dialogFragment A DialogFragment hosting a ProgressDialog
     * @param message        String message
     */
    public static void assertDialogFragmentHasProgressMessage(DialogFragment dialogFragment, String message) {
        // Get ProgressDialog.mMessageView through reflection.
        // Using reflection in testing is usually a sign of bad testing because we
        // shouldn't be testing the internal, private implementation details of a class.
        //
        // However because we are trying to access a UI element
        // (an inherently non-internal component), things are different.
        Dialog dialog = dialogFragment.getDialog();

        if (!(dialog instanceof ProgressDialog)) {
            fail("Dialog is not of class ProgressDialog");
            return;
        }

        ProgressDialog progressDialog = (ProgressDialog) dialog;
        Class target = ProgressDialog.class;

        try {
            Field messageField = target.getDeclaredField("mMessageView");
            messageField.setAccessible(true);
            TextView messageView = (TextView) messageField.get(progressDialog);
            assertThat(messageView).hasText(message);

        } catch (NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException: " + e);
            fail();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "IllegalAccessException: " + e);
            fail();
        }
    }
}
