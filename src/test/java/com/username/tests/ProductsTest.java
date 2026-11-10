package com.username.tests;

import com.username.base.BaseTest;
import com.username.dataproviders.TestDataProviders;
import com.username.pages.CartPage;
import com.username.pages.LoginPage;
import com.username.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductsTest extends BaseTest {

    private ProductsPage login() {
        return new LoginPage().open().loginAs("standard_user", "Test@123");
    }

    @Test(groups = {"smoke", "regression"}, description = "The shop lists 6 products")
    public void allProductsAreListed() {
        Assert.assertEquals(login().getProductNames().size(), 6);
    }

    @Test(groups = "regression", description = "Sort by price, low to high")
    public void sortByPriceLowToHigh() {
        List<Double> prices = login().sortBy("lohi").getProductPrices();
        List<Double> expected = new ArrayList<>(prices);
        Collections.sort(expected);
        Assert.assertEquals(prices, expected, "Prices should be in ascending order");
    }

    @Test(groups = "regression", description = "Sort by name, Z to A")
    public void sortByNameZToA() {
        List<String> names = login().sortBy("za").getProductNames();
        List<String> expected = new ArrayList<>(names);
        expected.sort(Comparator.reverseOrder());
        Assert.assertEquals(names, expected, "Names should be in reverse alphabetical order");
    }

    @Test(groups = "regression", dataProvider = "productData", dataProviderClass = TestDataProviders.class,
            description = "Every product in products.csv shows its price and can be added to the cart")
    public void productPriceAndAddToCart(String product, double price) {
        ProductsPage products = login();
        List<String> names = products.getProductNames();
        int index = names.indexOf(product);
        Assert.assertTrue(index >= 0, product + " should be listed");
        Assert.assertEquals(products.getProductPrices().get(index), price, 0.001, "Price of " + product);

        products.addToCart(product);
        Assert.assertEquals(products.getCartCount(), 1, "Cart badge after adding " + product);
    }

    @Test(groups = "regression", description = "Items can be removed from the cart")
    public void removeItemFromCart() {
        CartPage cart = login().addToCart("Laptop Bag").addToCart("Desk Lamp").openCart();
        Assert.assertEquals(cart.getItemNames(), List.of("Laptop Bag", "Desk Lamp"));
        cart.removeItem("Laptop Bag");
        Assert.assertEquals(cart.getItemNames(), List.of("Desk Lamp"));
    }
}
