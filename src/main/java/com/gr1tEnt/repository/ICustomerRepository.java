package com.gr1tEnt.repository;

import com.gr1tEnt.models.Customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// service classes will convert Customer into required DTO
public interface ICustomerRepository {
    boolean registerCustomer(Customer customer);
    Optional<Customer> findCustomerById(UUID customerId);
    boolean updateCustomerPassword(UUID customerId, String email);
    List<Customer> findAllCustomers();
    Map<UUID, BigDecimal> findAllCustomersWithTotalSpendingAbove(BigDecimal minTotalSpent);
    boolean deleteCustomerById(UUID customerId);
    List<Customer> findCustomersWithoutOrders();
    List<Customer> findCustomersByAddressLike(String addressPattern);
    List<Customer> findCustomersByLastNameLike(String lastNamePattern);
}