@shop
Feature: Shopping and checkout

  Background:
    Given the user is logged in as "standard_user"

  @smoke @checkout
  Scenario: Place an order and check the totals
    When the user adds these products to the cart:
      | Laptop Bag  |
      | USB-C Cable |
    Then the cart badge shows 2
    When the user checks out as "Alex" "Smith" with postal code "560034"
    Then the order summary lists the added products
    And the total is the item total plus 8% tax
    When the user finishes the order
    Then the confirmation message "Thank you for your order!" is shown

  @regression
  Scenario: Sort products by price
    When the user sorts the products by "lohi"
    Then the prices are in ascending order
