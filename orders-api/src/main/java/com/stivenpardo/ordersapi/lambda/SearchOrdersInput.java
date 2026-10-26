package com.stivenpardo.ordersapi.lambda;

/**
 * Shape of the JSON document API Gateway is configured to build
 * (through a mapping template) from the customerId query string
 * parameter, for the non-proxy integration of
 * GET /orders?customerId={customerId}.
 */
public class SearchOrdersInput {

    private String customerId;

    public SearchOrdersInput() {
    }

    public SearchOrdersInput(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
