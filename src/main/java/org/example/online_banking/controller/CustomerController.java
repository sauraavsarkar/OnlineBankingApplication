package org.example.online_banking.controller;

import lombok.RequiredArgsConstructor;
import org.example.online_banking.model.Customer;
import org.example.online_banking.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Customer> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return service.create(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Integer id, @RequestBody Customer updated) {
        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping("/search/email")
    public ResponseEntity<Customer> findByEmail(@RequestParam String email) {
        return service.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dob-after")
    public List<Customer> getByDobAfter(@RequestParam String date) {
        return service.findByDobAfter(LocalDate.parse(date));
    }

    @GetMapping("/search/name")
    public List<Customer> searchByName(@RequestParam String keyword) {
        return service.searchByName(keyword);
    }

    @GetMapping("/created-today")
    public List<Customer> getCreatedToday() {
        return service.findCreatedToday();
    }

    @GetMapping("/search/national-id")
    public ResponseEntity<Customer> findByNationalId(@RequestParam String nid) {
        return service.findByNationalId(nid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
