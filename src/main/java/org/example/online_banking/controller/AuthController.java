package org.example.online_banking.controller;

import jakarta.transaction.Transactional;
import org.example.online_banking.dto.JwtResponse;
import org.example.online_banking.dto.LoginRequest;
import org.example.online_banking.dto.RegisterRequest;
import org.example.online_banking.dto.RegisterResponse;
import org.example.online_banking.model.Customer;
import org.example.online_banking.model.User;
import org.example.online_banking.repository.CustomerRepository;
import org.example.online_banking.repository.UserRepository;
import org.example.online_banking.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public AuthController(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .map(user -> {
                    if (user.getPasswordHash().equals(request.getPassword())) { // use hash in real apps
                        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
                        String token = JwtUtil.generateToken(user.getUsername());
                        return ResponseEntity.ok(new JwtResponse(token));
                    } else {
                        return ResponseEntity.status(401).body("Invalid password");
                    }
                })
                .orElse(ResponseEntity.status(404).body("User not found"));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setAddress(request.getAddress());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setNationalId(request.getNationalId());
        Customer savedCustomer = customerRepository.save(customer);


        String[] nameParts = savedCustomer.getFullName().trim().toLowerCase().split("\\s+");
        String baseName = nameParts.length >= 2
                ? nameParts[0] + nameParts[1]
                : nameParts[0];

        String username = baseName + savedCustomer.getCustomerId();

        String password = generateRandomPassword();

        User user = new User();
        user.setCustomerId(savedCustomer.getCustomerId());
        user.setUsername(username);
        user.setPasswordHash(password); // Hash in production
        user.setRole(User.Role.customer);
        userRepository.save(user);

        return ResponseEntity.ok(new RegisterResponse(username, password));
    }


    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
