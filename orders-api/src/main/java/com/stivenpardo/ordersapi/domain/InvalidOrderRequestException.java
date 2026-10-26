package com.stivenpardo.ordersapi.domain;

/**
 * Thrown when the data supplied to create an order is not valid.
 * Kept free of any HTTP or AWS concept.
 */
public class InvalidOrderRequestException extends RuntimeException {

    public InvalidOrderRequestException(String message) {
        super(message);
    }
}
