package com.orangehrm.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer implements IRetryAnalyzer to retry failed tests a specified number of times.
 * This helps handle flaky tests by automatically retrying them before marking as failed.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2; // Maximum number of retry attempts

    /**
     * Determines whether a test method should be retried.
     *
     * @param result The result of the test method that just ran.
     * @return true if the test method should be retried, false otherwise.
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            return true;
        }
        return false;
    }

    /**
     * Gets the current retry count.
     *
     * @return The current retry count.
     */
    public int getRetryCount() {
        return retryCount;
    }
}
