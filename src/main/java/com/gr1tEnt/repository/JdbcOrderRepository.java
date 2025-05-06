package com.gr1tEnt.repository;

import com.gr1tEnt.models.Order;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
