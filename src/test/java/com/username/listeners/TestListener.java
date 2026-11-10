package com.username.listeners;

import com.username.config.ConfigReader;
import com.username.demoapp.DemoAppServer;
import com.username.driver.DriverManager;
import com.username.reports.ExtentManager;
import com.username.reports.ExtentTestManager;
import com.username.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Connects TestNG to the report: one report entry per test, a screenshot
 * on failure, and the report written when the suite ends. It also starts the
 * Practice Shop before the suite.
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
        DemoAppServer.startIfNeeded();
        ExtentManager.getInstance();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getTestClass().getRealClass().getSimpleName() + " : " + result.getMethod().getMethodName();
        if (result.getParameters().length > 0) {
            name += " " + java.util.Arrays.toString(result.getParameters());
        }
        ExtentTestManager.start(name, result.getMethod().getDescription(), result.getMethod().getGroups());
        LOG.info("START {}", name);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (ConfigReader.getBoolean("screenshot.on.pass") && DriverManager.hasDriver()) {
            ExtentTestManager.attachScreenshot("Final screen", ScreenshotUtils.asBase64());
        }
        ExtentTestManager.pass("Test passed");
        LOG.info("PASS  {}", result.getMethod().getMethodName());
        ExtentTestManager.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String screenshot = null;
        if (DriverManager.hasDriver()) {
            screenshot = ScreenshotUtils.asBase64();
            String file = ScreenshotUtils.saveToFile(result.getMethod().getMethodName());
            LOG.error("Screenshot saved: {}", file);
        }
        ExtentTestManager.fail(String.valueOf(result.getThrowable()), screenshot);
        LOG.error("FAIL  {} : {}", result.getMethod().getMethodName(), result.getThrowable().getMessage());
        ExtentTestManager.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // A test that failed and is retried is reported as skipped first; ExtentTestManager keeps both
        ExtentTestManager.skip(result.getThrowable() == null ? "Skipped" : "Skipped: " + result.getThrowable().getMessage());
        LOG.warn("SKIP  {}", result.getMethod().getMethodName());
        ExtentTestManager.remove();
    }
}
