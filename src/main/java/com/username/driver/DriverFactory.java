package com.username.driver;

import com.username.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

/**
 * Creates the browser chosen in config.properties (or with -Dbrowser=...),
 * locally or on a Selenium Grid (-Dremote=true). Selenium Manager, built into
 * Selenium 4, downloads the matching driver, so there is no driver setup.
 */
public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    /** Starts the browser from config.properties (or -Dbrowser=...). */
    public static void initDriver() {
        initDriver(null);
    }

    /**
     * Starts a given browser; null means "use config.properties".
     * The cross-browser suite passes the browser from the suite XML.
     */
    public static void initDriver(String browserName) {
        String browser = (browserName == null || browserName.isBlank() ? ConfigReader.get("browser") : browserName).toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");
        boolean remote = ConfigReader.getBoolean("remote");

        MutableCapabilities options = buildOptions(browser, headless);
        WebDriver driver = remote ? remoteDriver(options) : localDriver(browser, options);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout")));
        driver.manage().window().setSize(new Dimension(
                ConfigReader.getInt("window.width"), ConfigReader.getInt("window.height")));
        DriverManager.setDriver(driver);
        LOG.info("Started {} (headless={}, remote={})", browser, headless, remote);
    }

    private static MutableCapabilities buildOptions(String browser, boolean headless) {
        switch (browser) {
            case "chrome": {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications", "--remote-allow-origins=*");
                if (headless) {
                    // --no-sandbox and --disable-dev-shm-usage keep Chrome stable on Linux CI machines
                    options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                }
                return options;
            }
            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                return options;
            }
            case "edge": {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                }
                return options;
            }
            default:
                throw new IllegalArgumentException("Unsupported browser '" + browser + "'. Use chrome, firefox or edge.");
        }
    }

    private static WebDriver localDriver(String browser, MutableCapabilities options) {
        switch (browser) {
            case "firefox":
                return new FirefoxDriver((FirefoxOptions) options);
            case "edge":
                return new EdgeDriver((EdgeOptions) options);
            default:
                return new ChromeDriver((ChromeOptions) options);
        }
    }

    private static WebDriver remoteDriver(MutableCapabilities options) {
        String gridUrl = ConfigReader.get("grid.url");
        try {
            RemoteWebDriver driver = new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
            // lets file uploads send files from this computer to the Grid node
            driver.setFileDetector(new LocalFileDetector());
            return driver;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid grid.url: " + gridUrl, e);
        }
    }
}
