package com.tir38.android.blastermind;

import android.util.Log;

import com.tir38.android.blastermind.analytics.AnalyticsFunnel;
import com.tir38.android.blastermind.backend.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides dummy test doubles:
 * <p/>
 * If neither the test nor the SUT cares about these objects,
 * we may choose to pass in a Dummy Object, which may be as
 * simple as a null object reference, an instance of the
 * Object class,
 */
@Module(
        library = true,
        injects = {TestBlastermindApplication.class}
)
public class DummyModule {

    private static final String ERROR_MESSAGE = "Look Out! Your injection framework is trying to provide something that you didn't inject from a micro module. Maybe you need to rework your testing";
    private static final String TAG = DummyModule.class.getSimpleName();

    @Provides
    @Singleton
    protected DataManager provideDataManager() {
        Log.e(TAG, ERROR_MESSAGE);
        return null;
    }

    @Provides
    @Singleton
    protected AnalyticsFunnel provideAnalyticsFunnel() {
        Log.e(TAG, ERROR_MESSAGE);
        return null;
    }
}
