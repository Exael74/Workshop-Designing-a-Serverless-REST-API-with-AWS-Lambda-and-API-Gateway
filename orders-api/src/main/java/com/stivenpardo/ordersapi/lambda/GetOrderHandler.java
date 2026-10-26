package com.stivenpardo.ordersapi.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.stivenpardo.ordersapi.domain.InMemoryOrderService;
import com.stivenpardo.ordersapi.domain.Order;
import com.stivenpardo.ordersapi.domain.OrderService;

/**
 * Part III/IV of the workshop: used behind an API Gateway
 * non-proxy integration for GET /orders/{id}.
 *
 * API Gateway is responsible for extracting {id} from the path and
 * building the {@link GetOrderInput} JSON document through a
 * mapping template; this class never sees the raw HTTP request.
 *
 * {@link com.stivenpardo.ordersapi.domain.OrderNotFoundException} is
 * allowed to propagate on purpose: API Gateway's integration
 * response uses a selection pattern on the resulting errorMessage to
 * translate it into HTTP 404.
 */
public class GetOrderHandler implements RequestHandler<GetOrderInput, Order> {

    private final OrderService orderService = new InMemoryOrderService();

    public GetOrderHandler() {
        SampleData.seed(orderService);
    }

    @Override
    public Order handleRequest(GetOrderInput input, Context context) {
        return orderService.getOrder(input.getId());
    }
}
