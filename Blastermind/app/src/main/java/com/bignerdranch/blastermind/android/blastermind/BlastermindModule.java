package com.bignerdranch.blastermind.android.blastermind;

import com.bignerdranch.blastermind.android.blastermind.backend.DataManager;
import com.bignerdranch.blastermind.android.blastermind.backend.LiveDataManager;
import com.bignerdranch.blastermind.android.blastermind.controller.CreateMatchActivity;
import com.bignerdranch.blastermind.android.blastermind.controller.CreateMatchFragment;
import com.bignerdranch.blastermind.android.blastermind.controller.GameActivity;
import com.bignerdranch.blastermind.android.blastermind.controller.GameFragment;
import com.bignerdranch.blastermind.android.blastermind.controller.GamePendingActivity;
import com.bignerdranch.blastermind.android.blastermind.controller.GamePendingFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                GameFragment.class,
                GameActivity.class,

                CreateMatchActivity.class,
                CreateMatchFragment.class,

                GamePendingActivity.class,
                GamePendingFragment.class
        },
        library = true
)
public class BlastermindModule {

    public BlastermindModule() {
    }

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return new LiveDataManager();
    }
}

