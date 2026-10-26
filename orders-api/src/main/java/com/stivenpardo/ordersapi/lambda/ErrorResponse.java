package com.stivenpardo.ordersapi.lambda;

/**
 * Consistent JSON error representation returned by the HTTP
 * adapter, e.g.:
 * {"error": "ORDER_NOT_FOUND", "message": "Order ORD-999 does not exist"}
 */
public class ErrorResponse {

    private final String error;
    private final String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
