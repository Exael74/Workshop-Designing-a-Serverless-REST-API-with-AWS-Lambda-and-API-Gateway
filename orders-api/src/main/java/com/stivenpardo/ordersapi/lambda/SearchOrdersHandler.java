package com.stivenpardo.ordersapi.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.stivenpardo.ordersapi.domain.InMemoryOrderService;
import com.stivenpardo.ordersapi.domain.Order;
import com.stivenpardo.ordersapi.domain.OrderService;

import java.util.List;

/**
 * Part III/IV of the workshop: used behind an API Gateway
 * non-proxy integration for GET /orders?customerId={customerId}.
 *
 * API Gateway is responsible for reading the customerId query
 * string parameter and building the {@link SearchOrdersInput} JSON
 * document through a mapping template.
 */
public class SearchOrdersHandler implements RequestHandler<SearchOrdersInput, List<Order>> {

    private final OrderService orderService = new InMemoryOrderService();

    public SearchOrdersHandler() {
        SampleData.seed(orderService);
    }

    @Override
    public List<Order> handleRequest(SearchOrdersInput input, Context context) {
        return orderService.getOrdersByCustomer(input.getCustomerId());
    }
}
