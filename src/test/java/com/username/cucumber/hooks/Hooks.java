package com.username.cucumber.hooks;

import com.username.demoapp.DemoAppServer;
import com.username.driver.DriverFactory;
import com.username.driver.DriverManager;
import com.username.reports.ExtentManager;
import com.username.reports.ExtentTestManager;
import com.username.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

/**
 * Cucumber's version of BaseTest and TestListener: a browser per scenario,
 * a screenshot on failure (in the Cucumber and Extent reports), and the
 * Extent report written at the end.
 */
public class Hooks {

    @BeforeAll
    public static void beforeAll() {
        DemoAppServer.startIfNeeded();
    }

    @Before
    public void startBrowser(Scenario scenario) {
        DriverFactory.initDriver();
        ExtentTestManager.start("Scenario: " + scenario.getName(), scenario.getUri().toString(),
                scenario.getSourceTagNames().toArray(new String[0]));
    }

    @After
    public void closeBrowser(Scenario scenario) {
        if (DriverManager.hasDriver()) {
            if (scenario.isFailed()) {
                byte[] png = ScreenshotUtils.asBytes();
                scenario.attach(png, "image/png", "Screenshot on failure");
                ExtentTestManager.fail("Scenario failed: " + scenario.getName(), ScreenshotUtils.asBase64());
            } else {
                ExtentTestManager.pass("Scenario passed");
            }
            DriverManager.quitDriver();
        }
        ExtentTestManager.remove();
    }

    @AfterAll
    public static void afterAll() {
        ExtentManager.flush();
    }
}
