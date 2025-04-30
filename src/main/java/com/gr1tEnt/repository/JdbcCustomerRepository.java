package com.gr1tEnt.repository;

import com.gr1tEnt.models.Customer;
import com.gr1tEnt.models.CustomerDto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
        String sql = "INSERT INTO customers (customer_id, first_name, last_name, email, password_hash, address) VALUES (?, ?, ?, ?, ?, ?)";
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
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findCustomerByEmailInternal(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<CustomerDto> findCustomerByEmailDto(String email) {
        return Optional.empty();
    }

    @Override
    public CustomerDto updateCustomer(UUID customerId, CustomerDto customerDto) {
        return null;
    }

    @Override
    public List<CustomerDto> findAllCustomers() {
        return List.of();
    }

    @Override
    public List<CustomerDto> findAllCustomersWithTotalSpendingAbove(BigDecimal minTotalSpent) {
        return List.of();
    }
}
