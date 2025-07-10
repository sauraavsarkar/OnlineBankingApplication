package org.example.online_banking.repository;

import org.example.online_banking.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findByFullNameContainingIgnoreCase(String keyword);

    List<Customer> findByDateOfBirthAfter(LocalDate date);

    @Query(value = "SELECT * FROM customers WHERE DATE(created_at) = CURDATE()", nativeQuery = true)
    List<Customer> findAllCreatedToday();

    @Query("SELECT c FROM Customer c WHERE c.nationalId = :nid")
    Optional<Customer> searchByNationalId(@Param("nid") String nationalId);
}
