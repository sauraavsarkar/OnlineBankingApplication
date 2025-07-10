package org.example.online_banking.service;

import org.example.online_banking.model.Customer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> getAll();

    Optional<Customer> getById(Integer id);

    Customer create(Customer customer);

    Optional<Customer> update(Integer id, Customer updatedCustomer);

    void delete(Integer id);

    Optional<Customer> findByEmail(String email);

    List<Customer> searchByName(String keyword);

    List<Customer> findByDobAfter(LocalDate date);

    List<Customer> findCreatedToday();

    Optional<Customer> findByNationalId(String nid);
}
