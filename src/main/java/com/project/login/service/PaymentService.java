package com.project.login.service;

import com.project.login.entity.Payment;
import com.project.login.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Save payment details
     */
    public Payment savePayment(String email, String mobileNumber, String utrNumber) {
        Payment payment = new Payment();
        payment.setEmail(email);
        payment.setMobileNumber(mobileNumber);
        payment.setUtrNumber(utrNumber);
        payment.setPaymentDate(LocalDateTime.now());
        
        return paymentRepository.save(payment);
    }

    /**
     * Get all payments
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Get payment by ID
     */
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    /**
     * Get payments by mobile number
     */
    public List<Payment> getPaymentsByMobileNumber(String mobileNumber) {
        return paymentRepository.findByMobileNumber(mobileNumber);
    }

    /**
     * Delete payment
     */
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
