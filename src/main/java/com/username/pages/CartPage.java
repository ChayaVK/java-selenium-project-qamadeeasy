package com.username.pages;

import com.username.utils.WaitUtils;
import org.openqa.selenium.By;

import java.util.List;

public class CartPage extends BasePage {

    private final By itemNames = By.cssSelector(".cart-item .item-name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShopping = By.id("continue-shopping");
    private final By emptyMessage = By.id("empty-cart");

    public List<String> getItemNames() {
        WaitUtils.visible(checkoutButton);
        return isDisplayed(emptyMessage) ? List.of() : getTexts(itemNames);
    }

    public CartPage removeItem(String productName) {
        click(By.id("remove-" + productName.trim().toLowerCase().replace(' ', '-')));
        WaitUtils.visible(checkoutButton);
        return this;
    }

    public CheckoutPage checkout() {
        click(checkoutButton);
        return new CheckoutPage();
    }

    public ProductsPage continueShopping() {
        click(continueShopping);
        return new ProductsPage();
    }
}
