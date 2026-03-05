package com.project.login.service;

import com.project.login.entity.Payment;
import com.project.login.enums.PaymentStatus;
import com.project.login.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Save payment details
     */
    public Payment savePayment(String email, String userName,
                               String mobileNumber, String utrNumber) {

        Payment payment = new Payment();
        payment.setEmail(email.trim().toLowerCase());
        payment.setUserName(userName);
        payment.setMobileNumber(mobileNumber);
        payment.setUtrNumber(utrNumber.trim().toUpperCase());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);

        return paymentRepository.save(payment);
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getAllPaymentsOrderByPaymentDate() {
        return paymentRepository.findAllByOrderByPaymentDateDesc();
    }

    public boolean isUtrExists(String utr) {
        return paymentRepository.existsByUtrNumber(utr.trim().toUpperCase());
    }

    /**
     * Get latest payment by email
     */
    public Payment getLatestPaymentByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        List<Payment> payments =
                paymentRepository.findByEmailIgnoreCaseOrderByPaymentDateDesc(email.trim());

        if (payments == null || payments.isEmpty()) {
            return null;
        }

        return payments.get(0);
    }

    /**
     * Approve payment
     */
    public Payment approvePayment(Long paymentId,
                                  LocalDate startDate,
                                  LocalDate endDate) {

        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        if (payment == null) return null;

        payment.setStatus(PaymentStatus.APPROVED);
        payment.setSubscriptionStartDate(startDate);
        payment.setSubscriptionEndDate(endDate);
        payment.setApprovalDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    /**
     * Reject payment
     */
    public Payment rejectPayment(Long paymentId, String notes) {

        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        if (payment == null) return null;

        payment.setStatus(PaymentStatus.REJECTED);
        payment.setAdminNotes(notes);
        payment.setApprovalDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    /**
     * Check if subscription is valid
     */
    public boolean hasValidSubscription(String email) {

        Payment payment = getLatestPaymentByEmail(email);

        if (payment == null) return false;

        if (payment.getStatus() != PaymentStatus.APPROVED) return false;

        LocalDate today = LocalDate.now();

        if (payment.getSubscriptionEndDate() != null &&
                today.isAfter(payment.getSubscriptionEndDate())) {
            return false;
        }

        return true;
    }

    /**
     * Get payment status safely
     */
    public PaymentStatus getPaymentStatus(String email) {

        Payment payment = getLatestPaymentByEmail(email);

        if (payment == null) {
            return PaymentStatus.NOT_FOUND; // <-- Add this enum
        }

        return payment.getStatus();
    }

    /**
     * Delete payment
     */
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}