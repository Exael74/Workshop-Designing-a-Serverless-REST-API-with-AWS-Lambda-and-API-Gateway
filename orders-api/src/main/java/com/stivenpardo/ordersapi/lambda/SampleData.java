package com.stivenpardo.ordersapi.lambda;

import com.stivenpardo.ordersapi.domain.CreateOrderRequest;
import com.stivenpardo.ordersapi.domain.OrderService;

/**
 * No database is used in this workshop. Because every Lambda
 * function may run in its own execution environment, each deployed
 * function seeds the same predefined orders so that GET and search
 * operations can be exercised even when CREATE happened in a
 * different (or since-recycled) execution environment.
 */
final class SampleData {

    private SampleData() {
    }

    static void seed(OrderService orderService) {
        orderService.createOrder(new CreateOrderRequest("CUST-42", "MacBook Air", 1));
        orderService.createOrder(new CreateOrderRequest("CUST-7", "Keyboard", 2));
    }
}
