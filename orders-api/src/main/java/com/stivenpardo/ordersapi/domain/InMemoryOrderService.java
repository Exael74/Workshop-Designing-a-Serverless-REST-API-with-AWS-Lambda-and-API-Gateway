package com.stivenpardo.ordersapi.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory implementation of {@link OrderService}.
 *
 * AWS Lambda execution environments are ephemeral, so this
 * collection must never be treated as durable storage. It exists
 * only to let the application behave consistently during the
 * lifetime of a single execution environment and, more importantly,
 * to let the logic be tested without AWS.
 */
public class InMemoryOrderService implements OrderService {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(0);

    @Override
    public Order createOrder(CreateOrderRequest request) {
        validate(request);

        String id = nextId();
        Order order = new Order(id, request.getCustomerId(), request.getProduct(), request.getQuantity());
        orders.put(id, order);
        return order;
    }

    @Override
    public Order getOrder(String id) {
        Order order = orders.get(id);
        if (order == null) {
            throw new OrderNotFoundException(id);
        }
        return order;
    }

    @Override
    public List<Order> getOrdersByCustomer(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new InvalidOrderRequestException("customerId must not be blank");
        }

        List<Order> result = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getCustomerId().equals(customerId)) {
                result.add(order);
            }
        }
        return result;
    }

    @Override
    public void deleteOrder(String id) {
        Order removed = orders.remove(id);
        if (removed == null) {
            throw new OrderNotFoundException(id);
        }
    }

    private void validate(CreateOrderRequest request) {
        if (request == null) {
            throw new InvalidOrderRequestException("request body must not be empty");
        }
        if (request.getCustomerId() == null || request.getCustomerId().isBlank()) {
            throw new InvalidOrderRequestException("customerId must not be blank");
        }
        if (request.getProduct() == null || request.getProduct().isBlank()) {
            throw new InvalidOrderRequestException("product must not be blank");
        }
        if (request.getQuantity() <= 0) {
            throw new InvalidOrderRequestException("quantity must be greater than zero");
        }
    }

    private String nextId() {
        int next = sequence.incrementAndGet();
        return String.format("ORD-%03d", next);
    }
}
