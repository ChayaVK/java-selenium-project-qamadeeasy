package com.username.utils;

import com.username.constants.FrameworkConstants;
import com.username.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Screenshots as a file (reports/screenshots) or as Base64 (embedded in the HTML report). */
public final class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    public static String asBase64() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    public static byte[] asBytes() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    /** Saves a PNG named after the test and the time, and returns its path. */
    public static String saveToFile(String testName) {
        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        Path target = Paths.get(FrameworkConstants.SCREENSHOTS_DIR, testName.replaceAll("[^A-Za-z0-9_-]", "_") + "_" + stamp + ".png");
        try {
            Files.createDirectories(target.getParent());
            Files.write(target, asBytes());
        } catch (IOException e) {
            throw new IllegalStateException("Could not save screenshot " + target, e);
        }
        return target.toString();
    }
}
