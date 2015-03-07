package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;

public class CreateMatchActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return CreateMatchFragment.newInstance();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        super.onCreate(savedInstanceState);
    }
}
