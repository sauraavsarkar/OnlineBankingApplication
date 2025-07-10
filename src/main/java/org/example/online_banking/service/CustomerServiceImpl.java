package org.example.online_banking.service;

import lombok.RequiredArgsConstructor;
import org.example.online_banking.model.Customer;
import org.example.online_banking.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    public CustomerServiceImpl(CustomerRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Customer> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Customer> getById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Customer create(Customer customer) {
        return repo.save(customer);
    }

    @Override
    public Optional<Customer> update(Integer id, Customer updatedCustomer) {
        return repo.findById(id).map(existing -> {
            existing.setFullName(updatedCustomer.getFullName());
            existing.setEmail(updatedCustomer.getEmail());
            existing.setPhoneNumber(updatedCustomer.getPhoneNumber());
            existing.setAddress(updatedCustomer.getAddress());
            existing.setDateOfBirth(updatedCustomer.getDateOfBirth());
            existing.setNationalId(updatedCustomer.getNationalId());

            return repo.save(existing);
        });
    }


    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public List<Customer> searchByName(String keyword) {
        return repo.findByFullNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Customer> findByDobAfter(LocalDate date) {
        return repo.findByDateOfBirthAfter(date);
    }

    @Override
    public List<Customer> findCreatedToday() {
        return repo.findAllCreatedToday();
    }

    @Override
    public Optional<Customer> findByNationalId(String nid) {
        return repo.searchByNationalId(nid);
    }


}
