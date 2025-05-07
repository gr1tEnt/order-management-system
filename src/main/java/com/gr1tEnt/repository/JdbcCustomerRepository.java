package com.gr1tEnt.repository;

import com.gr1tEnt.models.Customer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcCustomerRepository implements ICustomerRepository {
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

            try (ResultSet rs = stm.executeQuery()) {
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCustomerPassword(UUID customerId, String newPassword) {
        String sql = "UPDATE customers " +
                "SET password_hash = ? " +
                "WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, String.valueOf(customerId));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAllCustomers() {
        List<Customer> currentCustomers = new ArrayList<>();
        String sql = "SELECT customer_id, first_name, last_name, email, password_hash, address " +
                "FROM customers";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer(
                        UUID.fromString(rs.getString("customer_id")),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("address")
                );
                currentCustomers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currentCustomers;
    }

    @Override
    public Map<UUID, BigDecimal> findAllCustomersWithTotalSpendingAbove(BigDecimal minTotalSpent) {
        Map<UUID, BigDecimal> matchingCustomers = new HashMap<>();

        String sql = "SELECT c.customer_id, " +
                "SUM(o.total_amount) AS total_customer_spending " +
                "FROM customers c " +
                "INNER JOIN orders o ON c.customer_id = o.customer_id " +
                "GROUP BY c.customer_id " +
                "HAVING SUM(o.total_amount) > ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, minTotalSpent);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matchingCustomers.put(UUID.fromString(rs.getString("customer_id")),
                            rs.getBigDecimal("total_customer_spending"));
                }
            }
            return matchingCustomers;
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

    @Override
    public List<Customer> findCustomersWithoutOrders() {
        List<Customer> customersWithoutOrders = new ArrayList<>();

        String sql = "SELECT c.customer_id, c.first_name, c.last_name, c.email, c.password_hash, c.address " +
                "FROM customers c " +
                "LEFT JOIN orders o ON o.customer_id = c.customer_id " +
                "WHERE o.order_id IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer(
                        UUID.fromString(rs.getString("customer_id")),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("address")
                );
                customersWithoutOrders.add(customer);
            }
            return customersWithoutOrders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findCustomersByAddressLike(String addressPattern) {
        List<Customer> matchingCustomers = new ArrayList<>();

        String sql = "SELECT customer_id, first_name, last_name, email, password_hash, address " +
                "FROM customers " +
                "WHERE address LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + addressPattern + "%";

            stmt.setString(1, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer(
                            UUID.fromString(rs.getString("customer_id")),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("address")
                    );
                    matchingCustomers.add(customer);
                }
            }
            return matchingCustomers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findCustomersByLastNameLike(String lastNamePattern) {
        List<Customer> matchingCustomers = new ArrayList<>();

        String sql = "SELECT customer_id, first_name, last_name, email, password_hash, address " +
                "FROM customers " +
                "WHERE last_name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + lastNamePattern + "%";

            stmt.setString(1, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer(
                            UUID.fromString(rs.getString("customer_id")),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("address")
                    );
                    matchingCustomers.add(customer);
                }
            }
            return matchingCustomers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findCustomersWithOrderCountGreaterThan(int minOrderCount) {
        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT c.customer_id, c.first_name, c.last_name, c.email, c.password_hash, c.address " +
                "FROM customers c " +
                "INNER JOIN orders o ON c.customer_id = o.customer_id " +
                "GROUP BY c.customer_id, c.first_name, c.last_name, c.email, c.password_hash, c.address" +
                "HAVING COUNT(o.order_id) > ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, minOrderCount);

            try (ResultSet rs = stmt.executeQuery()) {
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
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findCustomersWithAverageOrderValueLessThan(BigDecimal maxAverageValue) {
        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT c.customer_id, c.first_name, c.last_name, c.email, c.password_hash, c.address " +
                "FROM customers c " +
                "INNER JOIN orders o ON c.customer_id = o.customer_id " +
                "GROUP BY c.customer_id, c.first_name, c.last_name, c.email, c.password_hash, c.address " +
                "HAVING AVG(o.total_amount) < ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, maxAverageValue);

            try (ResultSet rs = stmt.executeQuery()) {
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
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<UUID, BigDecimal> calculateAverageOrderAmountPerCustomer() {
        Map<UUID, BigDecimal> customersWithAverageOrderAmount = new HashMap<>();

        String sql = "SELECT c.customer_id, AVG(o.total_amount) AS average_order_value " +
                "FROM customers c " +
                "INNER JOIN orders o ON c.customer_id = o.customer_id " +
                "GROUP BY c.customer_id ";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customersWithAverageOrderAmount.put(
                            UUID.fromString(rs.getString("customer_id")),
                            rs.getBigDecimal("average_order_value")
                    );
                }
            }
            return customersWithAverageOrderAmount;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<UUID, Long> countOrdersPerCustomer() {
        Map<UUID, Long> ordersPerCustomer = new HashMap<>();

        String sql = "SELECT c.customer_id, COUNT(o.order_id) AS ordersQuantity " +
                "FROM customers c " +
                // LEFT JOIN - for all customers, INNER JOIN - only with orders
                "LEFT JOIN orders o ON c.customer_id = o.customer_id " +
                "GROUP BY c.customer_id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ordersPerCustomer.put(
                            UUID.fromString(rs.getString("customer_id")),
                            rs.getLong("ordersQuantity")
                    );
                }
            }
            return ordersPerCustomer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
