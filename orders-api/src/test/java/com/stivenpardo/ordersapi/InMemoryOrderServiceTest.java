package com.stivenpardo.ordersapi;

import com.stivenpardo.ordersapi.domain.CreateOrderRequest;
import com.stivenpardo.ordersapi.domain.InMemoryOrderService;
import com.stivenpardo.ordersapi.domain.InvalidOrderRequestException;
import com.stivenpardo.ordersapi.domain.Order;
import com.stivenpardo.ordersapi.domain.OrderNotFoundException;
import com.stivenpardo.ordersapi.domain.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryOrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new InMemoryOrderService();
    }

    @Test
    void createsAValidOrder() {
        CreateOrderRequest request = new CreateOrderRequest("CUST-42", "MacBook Air", 1);

        Order created = orderService.createOrder(request);

        assertEquals("CUST-42", created.getCustomerId());
        assertEquals("MacBook Air", created.getProduct());
        assertEquals(1, created.getQuantity());
        assertTrue(created.getId().startsWith("ORD-"));
    }

    @Test
    void retrievesAnExistingOrder() {
        Order created = orderService.createOrder(new CreateOrderRequest("CUST-42", "MacBook Air", 1));

        Order fetched = orderService.getOrder(created.getId());

        assertEquals(created, fetched);
    }

    @Test
    void throwsWhenOrderDoesNotExist() {
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrder("ORD-999"));

        assertEquals("ORD-999", exception.getOrderId());
    }

    @Test
    void findsOrdersByCustomer() {
        orderService.createOrder(new CreateOrderRequest("CUST-42", "MacBook Air", 1));
        orderService.createOrder(new CreateOrderRequest("CUST-42", "Keyboard", 2));
        orderService.createOrder(new CreateOrderRequest("CUST-7", "Mouse", 1));

        List<Order> ordersForCust42 = orderService.getOrdersByCustomer("CUST-42");

        assertEquals(2, ordersForCust42.size());
    }

    @Test
    void returnsEmptyListWhenCustomerHasNoOrders() {
        List<Order> orders = orderService.getOrdersByCustomer("CUST-100");

        assertTrue(orders.isEmpty());
    }

    @Test
    void rejectsInvalidInputOnCreation() {
        assertThrows(InvalidOrderRequestException.class,
                () -> orderService.createOrder(new CreateOrderRequest("CUST-42", "MacBook Air", 0)));

        assertThrows(InvalidOrderRequestException.class,
                () -> orderService.createOrder(new CreateOrderRequest("", "MacBook Air", 1)));

        assertThrows(InvalidOrderRequestException.class,
                () -> orderService.createOrder(new CreateOrderRequest("CUST-42", "", 1)));

        assertThrows(InvalidOrderRequestException.class,
                () -> orderService.createOrder(null));
    }

    @Test
    void deletesAnExistingOrder() {
        Order created = orderService.createOrder(new CreateOrderRequest("CUST-42", "MacBook Air", 1));

        orderService.deleteOrder(created.getId());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(created.getId()));
    }

    @Test
    void throwsWhenDeletingAnOrderThatDoesNotExist() {
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder("ORD-999"));
    }
}
