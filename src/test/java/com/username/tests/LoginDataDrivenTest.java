package com.username.tests;

import com.username.base.BaseTest;
import com.username.dataproviders.TestDataProviders;
import com.username.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/** One test method, run once per row of testdata/LoginData.xlsx. */
public class LoginDataDrivenTest extends BaseTest {

    @Test(groups = "regression", dataProvider = "loginData", dataProviderClass = TestDataProviders.class,
            description = "Login with every user in the Excel file")
    public void loginWithExcelData(String username, String password, String expectedResult, String expectedMessage) {
        LoginPage login = new LoginPage().open();
        if ("success".equals(expectedResult)) {
            Assert.assertTrue(login.loginAs(username, password).isLoaded(), "Login should succeed for " + username);
        } else {
            Assert.assertEquals(login.attemptLogin(username, password).getErrorMessage(), expectedMessage);
        }
    }
}
