package com.username.utils;

import com.username.config.ConfigReader;
import com.username.driver.DriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Explicit waits in one place. The framework uses no implicit wait:
 * every wait says exactly what it is waiting for.
 */
public final class WaitUtils {

    private WaitUtils() {
    }

    private static WebDriverWait waitFor() {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
    }

    public static WebElement visible(By locator) {
        return waitFor().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement clickable(By locator) {
        return waitFor().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static List<WebElement> allVisible(By locator) {
        return waitFor().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean invisible(By locator) {
        return waitFor().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean textPresent(By locator, String text) {
        return waitFor().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean urlContains(String fraction) {
        return waitFor().until(ExpectedConditions.urlContains(fraction));
    }

    public static Alert alert() {
        return waitFor().until(ExpectedConditions.alertIsPresent());
    }

    public static void numberOfWindows(int count) {
        waitFor().until(ExpectedConditions.numberOfWindowsToBe(count));
    }
}
