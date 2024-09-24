package com.example.payment.service;
import com.example.payment.dto.UserRegistrationDto;
import com.example.payment.entity.User;
import com.example.payment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final StripeService stripeService;

    @Autowired
    public UserService(UserRepository userRepository, StripeService stripeService) {
        this.userRepository = userRepository;

        this.stripeService = stripeService;
    }

    public User registerUser(UserRegistrationDto registrationDto) throws Exception {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new Exception("Username already exists");
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new Exception("Email already exists");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());

        // Create Stripe customer
        String stripeCustomerId = stripeService.createCustomer(user.getEmail());
        user.setStripeCustomerId(stripeCustomerId);

        return userRepository.save(user);
    }
}
