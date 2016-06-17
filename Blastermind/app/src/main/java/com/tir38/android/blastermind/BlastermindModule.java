package com.tir38.android.blastermind;

import android.content.Context;

import com.tir38.android.blastermind.analytics.AnalyticsFunnel;
import com.tir38.android.blastermind.analytics.CrashlyticsAnalyticsFunnel;
import com.tir38.android.blastermind.backend.GameSupervisor;
import com.tir38.android.blastermind.backend.LiveGameSupervisor;
import com.tir38.android.blastermind.controller.CreateMatchActivity;
import com.tir38.android.blastermind.controller.CreateMatchFragment;
import com.tir38.android.blastermind.controller.GameActivity;
import com.tir38.android.blastermind.controller.GameFragment;
import com.tir38.android.blastermind.controller.GamePendingActivity;
import com.tir38.android.blastermind.controller.GamePendingFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                BlastermindApplication.class,

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

    private Context mContext;

    public BlastermindModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    GameSupervisor provideGameSupervisor() {
        return new LiveGameSupervisor();
    }

    @Provides
    @Singleton
    AnalyticsFunnel provideAnalyticsFunnel() {
        return new CrashlyticsAnalyticsFunnel(mContext);
    }
}

