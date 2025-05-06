package com.gr1tEnt.repository;

import com.gr1tEnt.models.Order;
import com.gr1tEnt.models.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcOrderRepository implements IOrderRepository {
    private final Connection conn;

    public JdbcOrderRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean createOrder(Order order) {
        String sql = "INSERT INTO orders (order_id, customer_id, order_date, status, total_amount, shipping_address) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, String.valueOf(order.getOrder_id()));
            stmt.setString(2, String.valueOf(order.getCustomer_id()));
            stmt.setDate(3, Date.valueOf(order.getOrder_date()));
            stmt.setString(4, order.getStatus().name());
            stmt.setBigDecimal(5, order.getTotal_amount());
            stmt.setString(6, order.getShipping_address());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Order> findOrderById(UUID orderId) {
        String sql = "SELECT order_id, customer_id, order_date, status, total_amount, shipping_address " +
                "FROM orders " +
                "WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(orderId));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Order order = new Order(
                        UUID.fromString(rs.getString("order_id")),
                        UUID.fromString(rs.getString("customer_id")),
                        rs.getDate("order_date").toLocalDate(),
                        OrderStatus.valueOf(rs.getString("status").toUpperCase()),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("shipping_address")
                );
                return Optional.of(order);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findOrdersByCustomerId(UUID customerId) {
        List<Order> orders = new ArrayList<>();

        String sql = "SELECT order_id, customer_id, order_date, status, total_amount, shipping_address " +
                "FROM orders " +
                "WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(customerId));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                        UUID.fromString(rs.getString("order_id")),
                        UUID.fromString(rs.getString("customer_id")),
                        rs.getDate("order_date").toLocalDate(),
                        OrderStatus.valueOf(rs.getString("status").toUpperCase()),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("shipping_address")
                );
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findOrdersByStatus(OrderStatus status) {
        List<Order> orders = new ArrayList<>();

        String sql = "SELECT order_id, customer_id, order_date, status, total_amount, shipping_address " +
                "FROM orders " +
                "WHERE status = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(status));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                        UUID.fromString(rs.getString("order_id")),
                        UUID.fromString(rs.getString("customer_id")),
                        rs.getDate("order_date").toLocalDate(),
                        OrderStatus.valueOf(rs.getString("status").toUpperCase()),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("shipping_address")
                );
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
