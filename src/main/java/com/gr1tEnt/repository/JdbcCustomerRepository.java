package com.gr1tEnt.repository;

import com.gr1tEnt.models.Customer;
import com.gr1tEnt.models.CustomerDto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Customer updateCustomer(UUID customerId, CustomerDto customerDto) {
        return null;
    }

    @Override
    public List<Customer> findAllCustomers() {
        return List.of();
    }

    @Override
    public List<Customer> findAllCustomersWithTotalSpendingAbove(BigDecimal minTotalSpent) {
        return List.of();
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
