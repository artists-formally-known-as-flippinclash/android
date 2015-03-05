package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.bignerdranch.blastermind.andorid.core.Player;
import com.bignerdranch.blastermind.android.blastermind.R;


public class GameActivity extends SingleFragmentActivity {

    private static final String EXTRA_PLAYER = "GameActivity.EXTRA_PLAYER";

    public static Intent newIntent(Context context, Player player) {

        Intent intent =  new Intent(context, GameActivity.class);
        intent.putExtra(EXTRA_PLAYER, player);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        Player player = (Player) getIntent().getSerializableExtra(EXTRA_PLAYER);
        return GameFragment.newInstance(player);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


