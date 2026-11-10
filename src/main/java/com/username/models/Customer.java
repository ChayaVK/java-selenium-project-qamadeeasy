package com.username.models;

import java.util.List;

/**
 * One checkout customer from testdata/customers.json.
 * Jackson fills the fields by name, so the JSON keys match the field names.
 */
public class Customer {

    private String firstName;
    private String lastName;
    private String postalCode;
    private List<String> products;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " buys " + products;
    }
}
