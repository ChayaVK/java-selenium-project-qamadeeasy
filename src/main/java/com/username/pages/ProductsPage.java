package com.username.pages;

import com.username.utils.WaitUtils;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsPage extends BasePage {

    private final By pageTitle = By.cssSelector("header .app-title");
    private final By productNames = By.cssSelector(".inventory-item .item-name");
    private final By productPrices = By.cssSelector(".inventory-item .item-price");
    private final By sortDropdown = By.id("sort");
    private final By cartBadge = By.id("cart-badge");
    private final By cartLink = By.id("cart-link");
    private final By logoutLink = By.id("logout-link");

    /** Products are identified by a slug: "Laptop Bag" becomes "laptop-bag". */
    private static String slug(String productName) {
        return productName.trim().toLowerCase().replace(' ', '-');
    }

    public boolean isLoaded() {
        return WaitUtils.textPresent(pageTitle, "Products");
    }

    public List<String> getProductNames() {
        return getTexts(productNames);
    }

    /** Prices as numbers, for example 49.99. */
    public List<Double> getProductPrices() {
        return getTexts(productPrices).stream()
                .map(p -> Double.parseDouble(p.replace("$", "")))
                .collect(Collectors.toList());
    }

    public ProductsPage sortBy(String value) {
        selectByValue(sortDropdown, value);
        return this;
    }

    public ProductsPage addToCart(String productName) {
        By addButton = By.id("add-" + slug(productName));
        click(addButton);
        // the page reloads after a click: wait until the button has turned into "Remove"
        WaitUtils.visible(By.id("remove-" + slug(productName)));
        return this;
    }

    public ProductsPage removeFromCart(String productName) {
        click(By.id("remove-" + slug(productName)));
        WaitUtils.visible(By.id("add-" + slug(productName)));
        return this;
    }

    /** The number on the cart icon; 0 when the badge is not shown. */
    public int getCartCount() {
        return isDisplayed(cartBadge) ? Integer.parseInt(getText(cartBadge)) : 0;
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage();
    }

    public LoginPage logout() {
        click(logoutLink);
        return new LoginPage();
    }
}
