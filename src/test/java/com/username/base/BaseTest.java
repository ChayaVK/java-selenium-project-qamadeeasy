package com.username.base;

import com.username.driver.DriverFactory;
import com.username.driver.DriverManager;
import com.username.listeners.TestListener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Every TestNG test class extends BaseTest: a fresh browser before each test
 * and quit after it, so tests never depend on each other.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    /**
     * The browser comes from config.properties, unless a suite XML passes
     * a "browser" parameter (see suites/cross-browser.xml).
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional String browser) {
        DriverFactory.initDriver(browser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
