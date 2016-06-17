package com.tir38.android.blastermind.analytics;

/**
 * Funnel your analytic events here and we'll take care of them
 */
public interface AnalyticsFunnel {

    /**
     * Initialize analytic tools/framework
     */
    void initialize();

    /**
     * Log an exception
     *
     * @param exception
     */
    void logException(Exception exception);

    /**
     * Log an error message
     *
     * @param errorMessage
     */
    void logErrorMessage(String errorMessage);

    /**
     * Log a {@link Throwable}
     *
     * @param throwable
     */
    void logThrowable(Throwable throwable);

    /**
     * Pass a specific event off to the analytics tools
     *
     * @param event
     */
    void trackEvent(String event);

    void trackPlayerStartedMatch(String playerName);
}