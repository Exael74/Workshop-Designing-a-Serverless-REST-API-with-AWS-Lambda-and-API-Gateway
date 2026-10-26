package com.stivenpardo.ordersapi.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.stivenpardo.ordersapi.domain.CreateOrderRequest;
import com.stivenpardo.ordersapi.domain.InMemoryOrderService;
import com.stivenpardo.ordersapi.domain.Order;
import com.stivenpardo.ordersapi.domain.OrderService;

/**
 * Part II of the workshop: a single Java operation deployed directly
 * to AWS Lambda, with no API Gateway and no HTTP adapter involved.
 *
 * The Lambda Java runtime deserializes the incoming JSON event
 * straight into a {@link CreateOrderRequest} instance and serializes
 * the returned {@link Order} straight back into JSON. This class
 * never touches HTTP concepts directly.
 */
public class CreateOrderLambdaHandler implements RequestHandler<CreateOrderRequest, Order> {

    private final OrderService orderService = new InMemoryOrderService();

    public CreateOrderLambdaHandler() {
        SampleData.seed(orderService);
    }

    @Override
    public Order handleRequest(CreateOrderRequest input, Context context) {
        return orderService.createOrder(input);
    }
}
