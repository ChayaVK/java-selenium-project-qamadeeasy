package com.username.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

/**
 * The report entry of the test running on this thread (safe in parallel runs).
 * Pages and steps call ExtentTestManager.info("...") to add a line to the report.
 */
public final class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private ExtentTestManager() {
    }

    public static ExtentTest start(String name, String description, String... categories) {
        ExtentTest test = ExtentManager.getInstance().createTest(name, description);
        if (categories.length > 0) {
            test.assignCategory(categories);
        }
        TEST.set(test);
        return test;
    }

    public static ExtentTest get() {
        return TEST.get();
    }

    public static void info(String message) {
        if (TEST.get() != null) {
            TEST.get().log(Status.INFO, message);
        }
    }

    public static void pass(String message) {
        if (TEST.get() != null) {
            TEST.get().log(Status.PASS, message);
        }
    }

    public static void fail(String message, String base64Screenshot) {
        if (TEST.get() == null) {
            return;
        }
        if (base64Screenshot == null) {
            TEST.get().log(Status.FAIL, message);
        } else {
            TEST.get().fail(message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        }
    }

    public static void attachScreenshot(String title, String base64Screenshot) {
        if (TEST.get() != null) {
            TEST.get().info(title, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        }
    }

    public static void skip(String message) {
        if (TEST.get() != null) {
            TEST.get().log(Status.SKIP, message);
        }
    }

    public static void remove() {
        TEST.remove();
    }
}
