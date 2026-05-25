package com.project.login.service;

import com.project.login.entity.Payment;
import com.project.login.entity.User;
import com.project.login.enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Scheduled service to auto-deactivate users when their subscription expires.
 * Runs daily at midnight.
 */
@Service
public class UserSchedulerService {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    // Auto-deactivation removed from cron job as per requirement.
    // Logic moved to login time in CustomUserDetailsService.
}
