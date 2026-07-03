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
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email.trim().toLowerCase()).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email)
        );

        // Compute effective enabled state in memory — NEVER write to DB here.
        // A user can login only if:
        //   1. Admin has set their account as active, AND
        //   2. They have an APPROVED (non-expired) payment subscription.
        boolean effectiveEnabled = false;
        if (user.isActive()) {
            PaymentStatus status = paymentService.getPaymentStatus(email);
            effectiveEnabled = (status == PaymentStatus.APPROVED);
        }

        return new CustomUserDetails(user, effectiveEnabled);
    }
}
