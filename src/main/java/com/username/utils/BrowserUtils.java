package com.username.utils;

import com.username.driver.DriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Set;

/** Alerts, frames and windows: the parts of Selenium that need switching. */
public final class BrowserUtils {

    private BrowserUtils() {
    }

    private static WebDriver driver() {
        return DriverManager.getDriver();
    }

    /** Waits for an alert, reads its text, accepts it and returns the text. */
    public static String acceptAlert() {
        Alert alert = WaitUtils.alert();
        String text = alert.getText();
        alert.accept();
        return text;
    }

    public static String dismissAlert() {
        Alert alert = WaitUtils.alert();
        String text = alert.getText();
        alert.dismiss();
        return text;
    }

    public static void typeInPromptAndAccept(String value) {
        Alert alert = WaitUtils.alert();
        alert.sendKeys(value);
        alert.accept();
    }

    public static void switchToFrame(By frameLocator) {
        driver().switchTo().frame(WaitUtils.visible(frameLocator));
    }

    public static void switchToMainPage() {
        driver().switchTo().defaultContent();
    }

    /**
     * Switches to the window that is not the current one and returns the
     * handle of the original window, so the caller can come back to it.
     */
    public static String switchToNewWindow(int expectedWindows) {
        String original = driver().getWindowHandle();
        WaitUtils.numberOfWindows(expectedWindows);
        Set<String> handles = driver().getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(original)) {
                driver().switchTo().window(handle);
                break;
            }
        }
        return original;
    }

    public static void closeAndSwitchTo(String windowHandle) {
        driver().close();
        driver().switchTo().window(windowHandle);
    }
}
