package com.username.listeners;

import com.username.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Runs a failed test again, up to retry.count times (config.properties).
 * Retries hide nothing: the first failure stays in the report as "skipped".
 * Use it for timing problems in the environment, not to hide real bugs.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LogManager.getLogger(RetryAnalyzer.class);
    private int attempt = 0;

    @Override
    public boolean retry(ITestResult result) {
        int max = ConfigReader.getInt("retry.count");
        if (attempt < max) {
            attempt++;
            LOG.warn("Retrying {} (attempt {} of {})", result.getMethod().getMethodName(), attempt, max);
            return true;
        }
        return false;
    }
}
