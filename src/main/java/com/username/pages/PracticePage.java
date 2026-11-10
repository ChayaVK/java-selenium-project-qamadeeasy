package com.username.pages;

import com.username.utils.BrowserUtils;
import com.username.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/** Alerts, frames, windows, waits, dropdowns, checkboxes, uploads and web tables. */
public class PracticePage extends BasePage {

    private final By alertButton = By.id("alert-btn");
    private final By confirmButton = By.id("confirm-btn");
    private final By promptButton = By.id("prompt-btn");
    private final By alertResult = By.id("alert-result");
    private final By frame = By.id("practice-frame");
    private final By frameInput = By.id("frame-input");
    private final By frameText = By.id("frame-text");
    private final By newWindowLink = By.id("new-window-link");
    private final By windowHeading = By.id("window-heading");
    private final By startLoading = By.id("start-loading");
    private final By loadedText = By.id("loaded-text");
    private final By country = By.id("country");
    private final By terms = By.id("terms");
    private final By fileUpload = By.id("file-upload");
    private final By fileName = By.id("file-name");
    private final By tableRows = By.cssSelector("#employees tbody tr");

    public PracticePage open() {
        openPath("/practice.html");
        return this;
    }

    public String acceptSimpleAlert() {
        click(alertButton);
        return BrowserUtils.acceptAlert();
    }

    public void clickConfirmAndDismiss() {
        click(confirmButton);
        BrowserUtils.dismissAlert();
    }

    public void answerPrompt(String value) {
        click(promptButton);
        BrowserUtils.typeInPromptAndAccept(value);
    }

    public String getAlertResult() {
        return getText(alertResult);
    }

    /** Switches into the frame, types, reads the result and switches back. */
    public String typeInsideFrame(String value) {
        BrowserUtils.switchToFrame(frame);
        type(frameInput, value);
        String result = getText(frameText);
        BrowserUtils.switchToMainPage();
        return result;
    }

    /** Opens the link in a new tab, reads its heading, closes it and comes back. */
    public String readNewWindowHeading() {
        click(newWindowLink);
        String original = BrowserUtils.switchToNewWindow(2);
        String heading = getText(windowHeading);
        BrowserUtils.closeAndSwitchTo(original);
        return heading;
    }

    /** The text appears after 2 seconds: an explicit wait handles it, no Thread.sleep. */
    public String startLoadingAndWait() {
        click(startLoading);
        return getText(loadedText);
    }

    public PracticePage selectCountry(String visibleText) {
        selectByVisibleText(country, visibleText);
        return this;
    }

    public String getSelectedCountry() {
        return selectedOption(country);
    }

    public boolean acceptTerms() {
        WebElement box = WaitUtils.clickable(terms);
        if (!box.isSelected()) {
            box.click();
        }
        return box.isSelected();
    }

    /** sendKeys with a full file path uploads the file: no window dialog is opened. */
    public String uploadFile(String absolutePath) {
        driver().findElement(fileUpload).sendKeys(absolutePath);
        return getText(fileName);
    }

    /** Finds a row by the name in column 1 and returns the value of another column (1-based). */
    public String getTableValue(String name, int column) {
        List<WebElement> rows = WaitUtils.allVisible(tableRows);
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.get(0).getText().trim().equals(name)) {
                return cells.get(column - 1).getText().trim();
            }
        }
        throw new IllegalArgumentException("No row for '" + name + "' in the employees table");
    }

    public long countRowsWhere(int column, String value) {
        return WaitUtils.allVisible(tableRows).stream()
                .filter(r -> r.findElements(By.tagName("td")).get(column - 1).getText().trim().equals(value))
                .count();
    }
}
