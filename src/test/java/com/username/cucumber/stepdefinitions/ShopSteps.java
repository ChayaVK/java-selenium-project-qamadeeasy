package com.username.cucumber.stepdefinitions;

import com.username.cucumber.context.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopSteps {

    private final ScenarioContext context;
    private final List<String> added = new ArrayList<>();

    public ShopSteps(ScenarioContext context) {
        this.context = context;
    }

    /** A one-column data table arrives as a List of Strings. */
    @When("the user adds these products to the cart:")
    public void theUserAddsTheseProducts(List<String> products) {
        for (String product : products) {
            context.productsPage.addToCart(product);
            added.add(product);
        }
    }

    @When("the user sorts the products by {string}")
    public void theUserSortsTheProductsBy(String value) {
        context.productsPage.sortBy(value);
    }

    @Then("the cart badge shows {int}")
    public void theCartBadgeShows(int count) {
        Assert.assertEquals(context.productsPage.getCartCount(), count);
    }

    @Then("the prices are in ascending order")
    public void thePricesAreInAscendingOrder() {
        List<Double> prices = context.productsPage.getProductPrices();
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);
        Assert.assertEquals(prices, sorted);
    }

    @When("the user checks out as {string} {string} with postal code {string}")
    public void theUserChecksOut(String first, String last, String postal) {
        context.overviewPage = context.productsPage.openCart().checkout()
                .fillDetails(first, last, postal).continueToOverview();
    }

    @Then("the order summary lists the added products")
    public void theOrderSummaryListsTheAddedProducts() {
        Assert.assertEquals(context.overviewPage.getItemNames(), added);
    }

    @Then("the total is the item total plus {int}% tax")
    public void theTotalIncludesTax(int percent) {
        double subtotal = context.overviewPage.getSubtotal();
        double tax = Math.round(subtotal * percent) / 100.0;
        Assert.assertEquals(context.overviewPage.getTax(), tax, 0.001, "Tax");
        Assert.assertEquals(context.overviewPage.getTotal(), subtotal + tax, 0.001, "Total");
    }

    @When("the user finishes the order")
    public void theUserFinishesTheOrder() {
        context.completePage = context.overviewPage.finish();
    }

    @Then("the confirmation message {string} is shown")
    public void theConfirmationMessageIsShown(String message) {
        Assert.assertEquals(context.completePage.getConfirmationMessage(), message);
    }
}
