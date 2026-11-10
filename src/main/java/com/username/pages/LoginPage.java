package com.username.pages;

import org.openqa.selenium.By;

/** The login page. Locators are private: tests use the methods, never the locators. */
public class LoginPage extends BasePage {

    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage open() {
        openPath("/");
        return this;
    }

    /** Logs in and returns the next page, so a test reads as a chain of steps. */
    public ProductsPage loginAs(String user, String pass) {
        enterCredentials(user, pass);
        return new ProductsPage();
    }

    /** For negative tests: tries to log in and stays on the login page. */
    public LoginPage attemptLogin(String user, String pass) {
        enterCredentials(user, pass);
        return this;
    }

    private void enterCredentials(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }
}
