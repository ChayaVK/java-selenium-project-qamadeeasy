package com.username.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.username.config.ConfigReader;
import com.username.constants.FrameworkConstants;

/**
 * One ExtentReports object for the whole run. TestNG tests and Cucumber
 * scenarios both write to it; flush() writes reports/ExtentReport.html.
 */
public final class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter(FrameworkConstants.EXTENT_REPORT);
            spark.config().setDocumentTitle(ConfigReader.get("report.title"));
            spark.config().setReportName(ConfigReader.get("report.name"));
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setTimeStampFormat("dd MMM yyyy, HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Browser", ConfigReader.get("browser"));
            extent.setSystemInfo("Headless", ConfigReader.get("headless"));
            extent.setSystemInfo("Environment", System.getProperty("env", "default"));
            extent.setSystemInfo("Base URL", ConfigReader.get("base.url"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extent;
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
