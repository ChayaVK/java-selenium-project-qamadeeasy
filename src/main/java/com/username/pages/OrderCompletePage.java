package com.username.pages;

import org.openqa.selenium.By;

public class OrderCompletePage extends BasePage {

    private final By header = By.id("complete-header");
    private final By backHome = By.id("back-home");

    public String getConfirmationMessage() {
        return getText(header);
    }

    public ProductsPage backToProducts() {
        click(backHome);
        return new ProductsPage();
    }
}
