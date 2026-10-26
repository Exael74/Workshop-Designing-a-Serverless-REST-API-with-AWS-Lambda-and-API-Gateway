package com.stivenpardo.ordersapi.domain;

import java.util.List;

/**
 * Application logic contract for the Order Management API. This
 * interface and its implementations are ordinary Java: no HTTP, no
 * AWS Lambda classes and no web server involved.
 */
public interface OrderService {

    Order createOrder(CreateOrderRequest request);

    Order getOrder(String id);

    List<Order> getOrdersByCustomer(String customerId);

    void deleteOrder(String id);
}
