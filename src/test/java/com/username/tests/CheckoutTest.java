package com.username.tests;

import com.username.base.BaseTest;
import com.username.dataproviders.TestDataProviders;
import com.username.models.Customer;
import com.username.pages.CheckoutOverviewPage;
import com.username.pages.CheckoutPage;
import com.username.pages.LoginPage;
import com.username.pages.OrderCompletePage;
import com.username.pages.ProductsPage;
import com.username.utils.RandomDataUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CheckoutTest extends BaseTest {

    private static final double TAX_RATE = 0.08;

    @Test(groups = {"smoke", "regression"}, dataProvider = "customerData", dataProviderClass = TestDataProviders.class,
            description = "End-to-end order for each customer in customers.json, with the totals checked")
    public void placeOrder(Customer customer) {
        ProductsPage products = new LoginPage().open().loginAs("standard_user", "Test@123");
        List<String> names = products.getProductNames();
        List<Double> prices = products.getProductPrices();
        double expectedSubtotal = 0;
        for (String item : customer.getProducts()) {
            products.addToCart(item);
            expectedSubtotal += prices.get(names.indexOf(item));
        }

        CheckoutOverviewPage overview = products.openCart().checkout()
                .fillDetails(customer.getFirstName(), customer.getLastName(), customer.getPostalCode())
                .continueToOverview();

        Assert.assertEquals(overview.getItemNames(), customer.getProducts());
        double expectedTax = Math.round(expectedSubtotal * TAX_RATE * 100) / 100.0;
        Assert.assertEquals(overview.getSubtotal(), expectedSubtotal, 0.001, "Item total");
        Assert.assertEquals(overview.getTax(), expectedTax, 0.001, "Tax");
        Assert.assertEquals(overview.getTotal(), expectedSubtotal + expectedTax, 0.001, "Total");

        OrderCompletePage complete = overview.finish();
        Assert.assertEquals(complete.getConfirmationMessage(), "Thank you for your order!");
        Assert.assertEquals(complete.backToProducts().getCartCount(), 0, "Cart should be empty after the order");
    }

    @Test(groups = "regression", description = "Checkout asks for the postal code")
    public void postalCodeIsRequired() {
        CheckoutPage checkout = new LoginPage().open().loginAs("standard_user", "Test@123")
                .addToCart("Water Bottle").openCart().checkout()
                .fillDetails(RandomDataUtils.firstName(), RandomDataUtils.lastName(), "")
                .continueExpectingError();
        Assert.assertEquals(checkout.getErrorMessage(), "Postal Code is required");
    }
}
