package com.username.tests;

import com.username.base.BaseTest;
import com.username.config.ConfigReader;
import com.username.driver.DriverManager;
import com.username.pages.LoginPage;
import com.username.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "A valid user logs in and sees the products")
    public void validUserCanLogIn() {
        ProductsPage products = new LoginPage().open().loginAs("standard_user", "Test@123");
        Assert.assertTrue(products.isLoaded(), "Products page should open after login");
    }

    @Test(groups = "regression", description = "A locked user sees the locked-out message")
    public void lockedUserIsRejected() {
        LoginPage login = new LoginPage().open().attemptLogin("locked_user", "Test@123");
        Assert.assertEquals(login.getErrorMessage(), "This user has been locked out");
    }

    @Test(groups = "regression", description = "Opening a page without logging in sends you back to login")
    public void pagesNeedLogin() {
        DriverManager.getDriver().get(ConfigReader.get("base.url") + "/cart.html");
        LoginPage login = new LoginPage();
        Assert.assertEquals(login.getErrorMessage(), "You must log in to view this page");
    }

    @Test(groups = {"smoke", "regression"}, description = "Logout returns to the login page")
    public void userCanLogOut() {
        LoginPage login = new LoginPage().open().loginAs("standard_user", "Test@123").logout();
        Assert.assertTrue(login.isLoginButtonDisplayed(), "Login button should be visible after logout");
    }
}
