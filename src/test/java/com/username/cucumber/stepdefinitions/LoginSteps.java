package com.username.cucumber.stepdefinitions;

import com.username.cucumber.context.ScenarioContext;
import com.username.pages.LoginPage;
import com.username.pages.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/** Steps call page objects: no locators or Selenium code in step definitions. */
public class LoginSteps {

    private final ScenarioContext context;

    public LoginSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        context.loginPage = new LoginPage().open();
    }

    @Given("the user is logged in as {string}")
    public void theUserIsLoggedInAs(String username) {
        context.productsPage = new LoginPage().open().loginAs(username, "Test@123");
        Assert.assertTrue(context.productsPage.isLoaded(), "Login should succeed");
    }

    @When("the user logs in as {string} with password {string}")
    public void theUserLogsIn(String username, String password) {
        context.loginPage.attemptLogin(username, password);
    }

    @Then("the products page is displayed")
    public void theProductsPageIsDisplayed() {
        context.productsPage = new ProductsPage();
        Assert.assertTrue(context.productsPage.isLoaded(), "Products page should be displayed");
    }

    @Then("the error message {string} is shown")
    public void theErrorMessageIsShown(String message) {
        Assert.assertEquals(context.loginPage.getErrorMessage(), message);
    }
}
