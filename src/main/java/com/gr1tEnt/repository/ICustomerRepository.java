package com.gr1tEnt.repository;

import com.gr1tEnt.models.Customer;
import com.gr1tEnt.models.CustomerDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICustomerRepository {
    boolean registerCustomer(Customer customer);
    Optional<Customer> findCustomerById(UUID customerId);
    Optional<Customer> findCustomerByEmailInternal(String email);
    Optional<CustomerDto> findCustomerByEmailDto(String email);
    CustomerDto updateCustomer(UUID customerId, CustomerDto customerDto);
    List<CustomerDto> findAllCustomers();
    List<CustomerDto> findAllCustomersWithTotalSpendingAbove(BigDecimal minTotalSpent);
}