package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;

public class CreateMatchActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return CreateMatchFragment.newInstance();
    }
}
