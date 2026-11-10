package com.username.pages;

import org.openqa.selenium.By;

/** Step 1 of checkout: the customer's information. */
public class CheckoutPage extends BasePage {

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutPage fillDetails(String first, String last, String postal) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, postal);
        return this;
    }

    public CheckoutOverviewPage continueToOverview() {
        click(continueButton);
        return new CheckoutOverviewPage();
    }

    /** For negative tests: clicks Continue and stays on this page. */
    public CheckoutPage continueExpectingError() {
        click(continueButton);
        return this;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
