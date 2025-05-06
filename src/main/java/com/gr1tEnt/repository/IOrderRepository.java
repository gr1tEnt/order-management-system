package com.gr1tEnt.repository;

import com.gr1tEnt.models.Customer;
import com.gr1tEnt.models.Order;
import com.gr1tEnt.models.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderRepository {
    boolean createOrder(Order order);
    Optional<Order> findOrderById(UUID orderId);
    List<Order> findOrdersByCustomerId(UUID customerId);
    List<Order> findOrdersByStatus(OrderStatus status);
    boolean updateOrderStatus(UUID orderId, OrderStatus newStatus);
    List<Order> findOrdersWithTotalAmountAbove(BigDecimal minAmount);
    long countTotalOrders();
    List<Order> findRecentOrders(int limit);
    List<Customer> findCustomersWhoOrderedSpecificProduct(UUID productId);
}
