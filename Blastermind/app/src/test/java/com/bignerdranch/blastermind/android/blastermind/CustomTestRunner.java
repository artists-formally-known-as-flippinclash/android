package com.bignerdranch.blastermind.android.blastermind;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.Fs;

public class CustomTestRunner extends RobolectricTestRunner {

    public CustomTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        String userDir = System.getProperty("user.dir");
        String appRoot = userDir + "/app/src/main/";
        String manifestPath = appRoot + "AndroidManifest.xml";
        String resDir = appRoot + "res";
        String assetsDir = appRoot + "assets";
        AndroidManifest manifest = createAppManifest(Fs.fileFromPath(manifestPath), Fs.fileFromPath(resDir), Fs.fileFromPath(assetsDir));
        manifest.setPackageName("com.bignerdranch.blastermind.android.blastermind");
        return manifest;
    }
}