package com.tir38.android.blastermind.controller;

import android.app.Fragment;

public class CreateMatchActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return CreateMatchFragment.newInstance();
    }
}
