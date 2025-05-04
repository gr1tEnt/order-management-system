package com.gr1tEnt.repository;

import com.gr1tEnt.models.Customer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcCustomerRepository implements ICustomerRepository {
    private static final Map<UUID, BigDecimal> customers_map = new HashMap<>();
    private static final List<Customer> customers = new ArrayList<>();
    private final Connection conn;

    public JdbcCustomerRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean registerCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, first_name, last_name, email, password_hash, address) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, customer.getCustomer_id().toString());
            stm.setString(2, customer.getFirst_name());
            stm.setString(3, customer.getLast_name());
            stm.setString(4, customer.getEmail());
            stm.setString(5, customer.getPassword());
            stm.setString(6, customer.getAddress());

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Customer> findCustomerById(UUID customerId) {
        String sql = "SELECT customer_id, first_name, last_name, email, password_hash, address " +
                "FROM customers " +
                "WHERE customer_id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, String.valueOf(customerId));
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer(
                        UUID.fromString(rs.getString("customer_id")),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("address")
                );
                return Optional.of(customer);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCustomerPassword(UUID customerId, String email) {
        String sql = "UPDATE customers " +
                "SET password_hash = ? " +
                "WHERE email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(customerId));
            stmt.setString(2, email);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAllCustomers() {
        String sql = "SELECT customer_id, first_name, last_name, email, password_hash, address " +
                "FROM customers";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer(
                        UUID.fromString(rs.getString("customer_id")),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("address")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    @Override
    public Map<UUID, BigDecimal> findAllCustomersWithTotalSpendingAbove(BigDecimal minTotalSpent) {
        String sql = "SELECT c.customer_id, " +
                "SUM(o.total_amount) AS total_customer_spending " +
                "FROM customers c " +
                "INNER JOIN orders o ON c.customer_id = o.customer_id " +
                "GROUP BY c.customer_id " +
                "HAVING SUM(o.total_amount) > ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, minTotalSpent);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                customers_map.put(UUID.fromString(rs.getString("customer_id")),
                        rs.getBigDecimal("total_customer_spending"));
            }

            return customers_map;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteCustomerById(UUID customerId) {
        String sql = "DELETE " +
                "FROM customers " +
                "WHERE customer_id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, String.valueOf(customerId));

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
