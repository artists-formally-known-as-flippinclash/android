package com.tir38.android.blastermind.helpers;

import android.app.Activity;
import android.content.Intent;

import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

public class ActivityTestHelper {

    /**
     * Start Activity
     *
     * @param activityToStart the class of the activity to start: "MyActivity.class"
     * @param <T>
     * @return
     */
    public static <T extends Activity> T createAndStartActivity(Class<T> activityToStart) {
        ActivityController<T> tActivityController = Robolectric.buildActivity(activityToStart);
        return createAndStartActivityFromController(tActivityController);
    }

    /**
     * Start Activity with Intent
     *
     * @param activityToStart the class of the activity to start: "MyActivity.class"
     * @param i               an Intent to pass to the started activity
     * @param <T>
     * @return
     */
    public static <T extends Activity> T createAndStartActivity(Class<T> activityToStart, Intent i) {
        ActivityController<T> buildActivity = Robolectric.buildActivity(activityToStart);
        buildActivity.withIntent(i);
        return createAndStartActivityFromController(buildActivity);
    }

    /**
     * Shadows the passed-in activity and checks to see if the next started activity is of the expected class.
     *
     * @param currentActivity          activity that started the intent
     * @param expectedNewActivityClass the Class to match
     * @return the intent for the started activity
     */
    public static Intent assertActivityStarted(Activity currentActivity, Class expectedNewActivityClass) {
        ShadowActivity shadowActivity = shadowOf(currentActivity);
        return assertActivityStarted(shadowActivity, expectedNewActivityClass);
    }

    /**
     * Using an activity shadow, checks to see if the next started activity is of the expected clazz.
     *
     * @param shadowActivity shadow of the activity that started the intent
     * @param clazz          the Class to match
     * @return the intent for the started activity
     */
    public static Intent assertActivityStarted(ShadowActivity shadowActivity, Class clazz) {
        Intent intent = shadowActivity.getNextStartedActivity();
        assertNotNull(intent);
        ShadowIntent shadowIntent = shadowOf(intent);
        assertEquals(clazz.getName(), shadowIntent.getComponent().getClassName());
        return intent;
    }

    /**
     * Creates a shadow of the param activity and removes all started intents Roboelectric may have.
     *
     * @param activity Activity to shadow
     * @return the shadowActivity that was used
     */
    public static ShadowActivity removeStartedActivityIntents(Activity activity) {
        ShadowActivity shadowActivity = shadowOf(activity);
        while (shadowActivity.peekNextStartedActivity() != null) {
            shadowActivity.getNextStartedActivity();
        }
        return shadowActivity;
    }


    public static void assertActivityIsFinishing(Activity activity) {
        ShadowActivity shadowActivity = shadowOf(activity);
        assertTrue(shadowActivity.isFinishing());
    }

    /**
     * helper method to start the activity using Robolectric's Activity builder
     *
     * @param activityController
     * @param <T>
     * @return
     */
    private static <T extends Activity> T createAndStartActivityFromController(ActivityController<T> activityController) {
        return activityController
                .create()
                .start()
                .resume()
                .get();
    }
}
