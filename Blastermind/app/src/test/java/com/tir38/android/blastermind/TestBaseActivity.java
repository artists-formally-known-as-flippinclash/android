package com.tir38.android.blastermind;

import com.tir38.android.blastermind.controller.BaseActivity;

/**
 * When hosting fragments for testing, we need a dummy activity to host them.
 * (See FragmentTestHelper.class) We ask Robolectric to create a shadow of this Activity.
 * The Activity we pass to Robolectric can't be abstract (it will instantiate an instance).
 * So if we want to pass in BaseActivity, we need a concrete implementation.
 * This is that concrete implementation
 */
public class TestBaseActivity extends BaseActivity {
}
