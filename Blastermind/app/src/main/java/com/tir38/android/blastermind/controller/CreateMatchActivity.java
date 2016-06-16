package com.tir38.android.blastermind.controller;


import android.support.v4.app.Fragment;

public class CreateMatchActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return CreateMatchFragment.newInstance();
    }
}
