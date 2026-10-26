package com.stivenpardo.ordersapi.domain;

/**
 * Plain Java representation of the data required to create an order.
 * This is the object the AWS Lambda Java runtime deserializes the
 * incoming JSON document into.
 */
public class CreateOrderRequest {

    private String customerId;
    private String product;
    private int quantity;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String customerId, String product, int quantity) {
        this.customerId = customerId;
        this.product = product;
        this.quantity = quantity;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "customerId='" + customerId + '\'' +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
