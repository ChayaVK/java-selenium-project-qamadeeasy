package com.username.pages;

import com.username.config.ConfigReader;
import com.username.driver.DriverManager;
import com.username.reports.ExtentTestManager;
import com.username.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Parent of every page object. It holds the common actions (click, type,
 * read text, select) so each action waits properly and is logged once,
 * instead of repeating that code in every page.
 */
public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    /** Opens a path of the application, for example "/" or "/practice.html". */
    protected void openPath(String path) {
        String url = ConfigReader.get("base.url").replaceAll("/$", "") + path;
        driver().get(url);
        step("Opened " + url);
    }

    protected void click(By locator) {
        WaitUtils.clickable(locator).click();
        step("Clicked " + locator);
    }

    protected void type(By locator, String text) {
        WebElement field = WaitUtils.visible(locator);
        field.clear();
        field.sendKeys(text);
        step("Typed '" + text + "' into " + locator);
    }

    protected String getText(By locator) {
        return WaitUtils.visible(locator).getText().trim();
    }

    protected List<String> getTexts(By locator) {
        return WaitUtils.allVisible(locator).stream().map(e -> e.getText().trim()).collect(Collectors.toList());
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> found = driver().findElements(locator);
        return !found.isEmpty() && found.get(0).isDisplayed();
    }

    protected void selectByValue(By locator, String value) {
        new Select(WaitUtils.visible(locator)).selectByValue(value);
        step("Selected value '" + value + "' in " + locator);
    }

    protected void selectByVisibleText(By locator, String text) {
        new Select(WaitUtils.visible(locator)).selectByVisibleText(text);
        step("Selected '" + text + "' in " + locator);
    }

    protected String selectedOption(By locator) {
        return new Select(WaitUtils.visible(locator)).getFirstSelectedOption().getText().trim();
    }

    public String getTitle() {
        return driver().getTitle();
    }

    public String getCurrentUrl() {
        return driver().getCurrentUrl();
    }

    /** Writes one line to the log file and to the HTML report. */
    protected void step(String message) {
        log.info(message);
        ExtentTestManager.info(message);
    }
}
