package com.username.pages;

import org.openqa.selenium.By;

import java.util.List;

/** Step 2 of checkout: the order summary with item total, tax and total. */
public class CheckoutOverviewPage extends BasePage {

    private final By itemNames = By.cssSelector(".cart-item .item-name");
    private final By subtotal = By.id("subtotal");
    private final By tax = By.id("tax");
    private final By total = By.id("total");
    private final By finishButton = By.id("finish");

    public List<String> getItemNames() {
        return getTexts(itemNames);
    }

    public double getSubtotal() {
        return amount(subtotal);
    }

    public double getTax() {
        return amount(tax);
    }

    public double getTotal() {
        return amount(total);
    }

    /** "Item total: $69.98" becomes 69.98 */
    private double amount(By locator) {
        String text = getText(locator);
        return Double.parseDouble(text.substring(text.indexOf('$') + 1));
    }

    public OrderCompletePage finish() {
        click(finishButton);
        return new OrderCompletePage();
    }
}
