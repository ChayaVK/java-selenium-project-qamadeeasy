package com.username.cucumber.context;

import com.username.pages.CheckoutOverviewPage;
import com.username.pages.LoginPage;
import com.username.pages.OrderCompletePage;
import com.username.pages.ProductsPage;

/**
 * Shared state for one scenario. PicoContainer (cucumber-picocontainer) creates
 * one ScenarioContext per scenario and passes the same object to every step
 * class that asks for it in its constructor. That is how LoginSteps can hand
 * the products page to CheckoutSteps.
 */
public class ScenarioContext {

    public LoginPage loginPage;
    public ProductsPage productsPage;
    public CheckoutOverviewPage overviewPage;
    public OrderCompletePage completePage;
}
