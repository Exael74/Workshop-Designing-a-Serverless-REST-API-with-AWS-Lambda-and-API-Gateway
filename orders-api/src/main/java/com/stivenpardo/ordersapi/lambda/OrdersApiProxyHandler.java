package com.stivenpardo.ordersapi.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stivenpardo.ordersapi.domain.CreateOrderRequest;
import com.stivenpardo.ordersapi.domain.InMemoryOrderService;
import com.stivenpardo.ordersapi.domain.InvalidOrderRequestException;
import com.stivenpardo.ordersapi.domain.Order;
import com.stivenpardo.ordersapi.domain.OrderNotFoundException;
import com.stivenpardo.ordersapi.domain.OrderService;

import java.util.List;
import java.util.Map;

/**
 * Part V of the workshop: a thin HTTP adapter sitting behind an API
 * Gateway Lambda proxy integration.
 *
 * This is the only class in the project allowed to know about
 * {@link APIGatewayProxyRequestEvent}, HTTP status codes and HTTP
 * headers. It extracts path/query parameters and the request body,
 * delegates to {@link OrderService} (ordinary Java, no AWS classes)
 * and translates the result (or exception) back into an HTTP
 * response.
 *
 * Supports:
 *   POST   /orders
 *   GET    /orders/{id}
 *   GET    /orders?customerId={customerId}
 *   DELETE /orders/{id}
 */
public class OrdersApiProxyHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Map<String, String> JSON_HEADERS = Map.of("Content-Type", "application/json");

    private final OrderService orderService = new InMemoryOrderService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrdersApiProxyHandler() {
        SampleData.seed(orderService);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            String method = request.getHttpMethod();
            String id = pathParameter(request, "id");

            if ("POST".equalsIgnoreCase(method)) {
                return handleCreate(request);
            }
            if ("GET".equalsIgnoreCase(method) && id != null) {
                return handleGetById(id);
            }
            if ("GET".equalsIgnoreCase(method)) {
                return handleSearchByCustomer(request);
            }
            if ("DELETE".equalsIgnoreCase(method) && id != null) {
                return handleDelete(id);
            }

            return jsonResponse(400, new ErrorResponse("UNSUPPORTED_OPERATION",
                    method + " " + request.getPath() + " is not supported"));

        } catch (InvalidOrderRequestException e) {
            return jsonResponse(400, new ErrorResponse("INVALID_ORDER_REQUEST", e.getMessage()));
        } catch (OrderNotFoundException e) {
            return jsonResponse(404, new ErrorResponse("ORDER_NOT_FOUND", e.getMessage()));
        } catch (Exception e) {
            return jsonResponse(500, new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
        }
    }

    private APIGatewayProxyResponseEvent handleCreate(APIGatewayProxyRequestEvent request) throws Exception {
        String body = request.getBody();
        if (body == null || body.isBlank()) {
            throw new InvalidOrderRequestException("request body must not be empty");
        }

        CreateOrderRequest createOrderRequest = objectMapper.readValue(body, CreateOrderRequest.class);
        Order created = orderService.createOrder(createOrderRequest);

        APIGatewayProxyResponseEvent response = jsonResponse(201, created);
        response.getHeaders().put("Location", "/orders/" + created.getId());
        return response;
    }

    private APIGatewayProxyResponseEvent handleGetById(String id) {
        Order order = orderService.getOrder(id);
        return jsonResponse(200, order);
    }

    private APIGatewayProxyResponseEvent handleSearchByCustomer(APIGatewayProxyRequestEvent request) {
        String customerId = queryParameter(request, "customerId");
        if (customerId == null || customerId.isBlank()) {
            throw new InvalidOrderRequestException("customerId query parameter is required");
        }

        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        return jsonResponse(200, orders);
    }

    private APIGatewayProxyResponseEvent handleDelete(String id) {
        orderService.deleteOrder(id);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(204)
                .withHeaders(JSON_HEADERS);
    }

    private String pathParameter(APIGatewayProxyRequestEvent request, String name) {
        Map<String, String> pathParameters = request.getPathParameters();
        return pathParameters == null ? null : pathParameters.get(name);
    }

    private String queryParameter(APIGatewayProxyRequestEvent request, String name) {
        Map<String, String> queryStringParameters = request.getQueryStringParameters();
        return queryStringParameters == null ? null : queryStringParameters.get(name);
    }

    private APIGatewayProxyResponseEvent jsonResponse(int statusCode, Object body) {
        try {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withHeaders(new java.util.HashMap<>(JSON_HEADERS))
                    .withBody(objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withHeaders(new java.util.HashMap<>(JSON_HEADERS))
                    .withBody("{\"error\":\"INTERNAL_ERROR\",\"message\":\"Unable to serialize response\"}");
        }
    }
}
