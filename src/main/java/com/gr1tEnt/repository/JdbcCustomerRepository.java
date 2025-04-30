package com.gr1tEnt.repository;

import com.gr1tEnt.models.Customer;
import com.gr1tEnt.models.CustomerDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcCustomerRepository implements ICustomerRepository {
    @Override
    public CustomerDto registerCustomer(Customer customer) {
        return null;
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
