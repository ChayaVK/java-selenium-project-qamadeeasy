@login
Feature: Login
  As a shop user
  I want to log in with my username and password
  So that I can buy products

  Background:
    Given the user is on the login page

  @smoke
  Scenario: Successful login
    When the user logs in as "standard_user" with password "Test@123"
    Then the products page is displayed

  @regression
  Scenario Outline: Login is rejected with a clear message
    When the user logs in as "<username>" with password "<password>"
    Then the error message "<message>" is shown

    Examples:
      | username      | password   | message                            |
      | locked_user   | Test@123   | This user has been locked out      |
      | standard_user | wrong_pass | Username and password do not match |
      |               | Test@123   | Username is required               |
