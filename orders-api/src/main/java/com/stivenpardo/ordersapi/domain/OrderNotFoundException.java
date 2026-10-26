package com.stivenpardo.ordersapi.domain;

/**
 * Thrown when a requested order does not exist. Kept free of any
 * HTTP or AWS concept; the adapter layer decides how this maps to a
 * status code.
 */
public class OrderNotFoundException extends RuntimeException {

    private final String orderId;

    public OrderNotFoundException(String orderId) {
        super("Order " + orderId + " does not exist");
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
