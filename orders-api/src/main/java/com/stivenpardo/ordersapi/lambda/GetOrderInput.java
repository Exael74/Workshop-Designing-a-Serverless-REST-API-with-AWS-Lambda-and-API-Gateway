package com.stivenpardo.ordersapi.lambda;

/**
 * Shape of the JSON document API Gateway is configured to build
 * (through a mapping template) from the {id} path parameter, for
 * the non-proxy integration of GET /orders/{id}.
 */
public class GetOrderInput {

    private String id;

    public GetOrderInput() {
    }

    public GetOrderInput(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
