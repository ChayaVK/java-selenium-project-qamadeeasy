package com.username.constants;

/**
 * Fixed values used across the framework: folders and file names.
 * Settings that change per run (browser, URL, waits) live in config.properties instead.
 */
public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    public static final String PROJECT_DIR = System.getProperty("user.dir");
    public static final String CONFIG_FILE = "config.properties";
    public static final String ENV_FOLDER = "env/";
    public static final String REPORTS_DIR = PROJECT_DIR + "/reports";
    public static final String EXTENT_REPORT = REPORTS_DIR + "/ExtentReport.html";
    public static final String SCREENSHOTS_DIR = REPORTS_DIR + "/screenshots";
}
