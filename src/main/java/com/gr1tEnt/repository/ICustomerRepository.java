package com.gr1tEnt.repository;

import com.gr1tEnt.models.Customer;
import com.gr1tEnt.models.CustomerDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// service classes will convert Customer into required DTO
public interface ICustomerRepository {
    boolean registerCustomer(Customer customer);
    Optional<Customer> findCustomerById(UUID customerId);
    Customer updateCustomer(UUID customerId, CustomerDto customerDto);
    List<Customer> findAllCustomers();
    List<Customer> findAllCustomersWithTotalSpendingAbove(BigDecimal minTotalSpent);
}