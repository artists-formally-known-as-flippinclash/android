package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Activity;

import com.bignerdranch.blastermind.android.blastermind.utils.DialogUtils;

public abstract class BaseActivity extends Activity {

    protected void showProgressDialog(String message) {
        DialogUtils.showLoadingDialog(getFragmentManager(), message);
    }

    protected void dismissProgressDialog() {
        DialogUtils.hideLoadingDialog(getFragmentManager());
    }
}
