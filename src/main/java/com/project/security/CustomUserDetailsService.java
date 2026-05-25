package com.project.security;

import com.project.login.entity.User;
import com.project.login.enums.PaymentStatus;
import com.project.login.repository.UserRepository;
import com.project.login.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

    	User user = userRepository.findByEmail(email.trim().toLowerCase()).orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );

        // CHECK SUBSCRIPTION STATUS ON LOGIN
        if (user.isActive()) {
            PaymentStatus status = paymentService.getPaymentStatus(email);
            if (status != PaymentStatus.APPROVED) {
                user.setActive(false);
                userRepository.save(user);
            }
        }

    	return new CustomUserDetails(user);

    }
}
