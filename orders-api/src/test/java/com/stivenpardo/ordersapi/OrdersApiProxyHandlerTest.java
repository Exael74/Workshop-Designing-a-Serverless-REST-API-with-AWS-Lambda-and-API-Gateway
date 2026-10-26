package com.stivenpardo.ordersapi;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.stivenpardo.ordersapi.lambda.OrdersApiProxyHandler;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The HTTP adapter can be exercised with plain {@link APIGatewayProxyRequestEvent}
 * objects, without deploying anything to AWS.
 */
class OrdersApiProxyHandlerTest {

    private final OrdersApiProxyHandler handler = new OrdersApiProxyHandler();

    @Test
    void createsAnOrderAndReturns201WithLocationHeader() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withHttpMethod("POST")
                .withPath("/orders")
                .withBody("{\"customerId\":\"CUST-42\",\"product\":\"MacBook Air\",\"quantity\":1}");

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);

        assertEquals(201, response.getStatusCode());
        assertTrue(response.getHeaders().get("Location").startsWith("/orders/"));
        assertTrue(response.getBody().contains("MacBook Air"));
    }

    @Test
    void getsAnExistingOrderWithPathParameter() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withHttpMethod("GET")
                .withPath("/orders/ORD-001")
                .withPathParameters(Map.of("id", "ORD-001"));

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("ORD-001"));
    }

    @Test
    void returns404WhenOrderDoesNotExist() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withHttpMethod("GET")
                .withPath("/orders/ORD-999")
                .withPathParameters(Map.of("id", "ORD-999"));

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);

        assertEquals(404, response.getStatusCode());
        assertTrue(response.getBody().contains("ORDER_NOT_FOUND"));
    }

    @Test
    void searchesOrdersByCustomerIdQueryParameter() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withHttpMethod("GET")
                .withPath("/orders")
                .withQueryStringParameters(Map.of("customerId", "CUST-42"));

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("CUST-42"));
    }

    @Test
    void returns400OnInvalidCreateRequest() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withHttpMethod("POST")
                .withPath("/orders")
                .withBody("{\"customerId\":\"\",\"product\":\"MacBook Air\",\"quantity\":1}");

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);

        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().contains("INVALID_ORDER_REQUEST"));
    }

    @Test
    void deletesAnExistingOrderAndReturns204() {
        APIGatewayProxyRequestEvent deleteRequest = new APIGatewayProxyRequestEvent()
                .withHttpMethod("DELETE")
                .withPath("/orders/ORD-001")
                .withPathParameters(Map.of("id", "ORD-001"));

        APIGatewayProxyResponseEvent response = handler.handleRequest(deleteRequest, null);

        assertEquals(204, response.getStatusCode());
    }

    @Test
    void returns404WhenDeletingAnOrderThatDoesNotExist() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withHttpMethod("DELETE")
                .withPath("/orders/ORD-999")
                .withPathParameters(Map.of("id", "ORD-999"));

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);

        assertEquals(404, response.getStatusCode());
        assertTrue(response.getBody().contains("ORDER_NOT_FOUND"));
    }
}
