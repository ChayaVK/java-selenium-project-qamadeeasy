package com.username.utils;

import com.username.driver.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/** For the few cases where a normal Selenium action is not enough. */
public final class JavaScriptUtils {

    private JavaScriptUtils() {
    }

    private static JavascriptExecutor js() {
        return (JavascriptExecutor) DriverManager.getDriver();
    }

    public static void scrollIntoView(WebElement element) {
        js().executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public static void click(WebElement element) {
        js().executeScript("arguments[0].click();", element);
    }

    public static Object execute(String script, Object... args) {
        return js().executeScript(script, args);
    }

    /** Clears browser storage, for example to log out between tests that reuse a browser. */
    public static void clearStorage() {
        js().executeScript("window.localStorage.clear(); window.sessionStorage.clear();");
    }
}
