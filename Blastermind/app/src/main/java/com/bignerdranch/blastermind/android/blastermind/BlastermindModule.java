package com.bignerdranch.blastermind.android.blastermind;

import com.bignerdranch.blastermind.android.blastermind.backend.DataManager;
import com.bignerdranch.blastermind.android.blastermind.backend.LiveDataManager;
import com.bignerdranch.blastermind.android.blastermind.controller.MainActivity;
import com.bignerdranch.blastermind.android.blastermind.controller.MainFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {MainFragment.class,
                MainActivity.class,
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

